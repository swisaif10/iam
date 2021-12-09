package digital.iam.ma.views.dashboard.bundles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import digital.iam.ma.R;
import digital.iam.ma.databinding.FragmentBundlesBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.bundles.BundleItem;
import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.BundlesViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class BundlesFragment extends Fragment {

    private FragmentBundlesBinding fragmentBinding;
    private BundlesViewModel viewModel;
    private PreferenceManager preferenceManager;
    private String selectedSku = "";
    private String actualSku = "";
    private Boolean canInternetSeek;
    private Boolean canCallSeek;
    private int position = 0;


    public BundlesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(BundlesViewModel.class);
        //viewModel.getMyBundleLiveData().observe(this, this::handleMyBundleDetailsData);
        viewModel.getGetBundlesLiveData().observe(this, this::handleBundlesData);
        viewModel.getSwitchBundleLiveData().observe(this, this::handleSwitchBundleData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        assert getArguments() != null;
        position = getArguments().getInt(Constants.POSITION);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentBundlesBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundlesList();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) requireActivity()).showHideTabLayout(View.VISIBLE);
        canInternetSeek = false;
        canCallSeek = false;
    }

    private void getBundlesList() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        viewModel.getBundles(preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleBundlesData(Resource<BundlesData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                init(responseData.data.getResponse().getBundles());
                break;
            case INVALID_TOKEN:
                assert responseData.data != null;
                Utilities.showErrorPopupWithClick(requireContext(), responseData.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(List<BundleItem> bundles) {
        //Line line = preferenceManager.getValue(Constants.LINE_DETAILS);
        fragmentBinding.bundleName.setText(((DashboardActivity) requireActivity()).getList().get(position).getBundleName());
        fragmentBinding.msisdn.setText(((DashboardActivity) requireActivity()).getList().get(position).getMsisdn());
        fragmentBinding.date.setText(String.format("%s %s\n%s %s", getString(R.string.end_at), ((DashboardActivity) requireActivity()).getList().get(position).getExpireDate().replace("-", "/"), getString(R.string.start_at), ((DashboardActivity) requireActivity()).getList().get(position).getStartDate().replace("-", "/")));
        if (((DashboardActivity) requireActivity()).getList().get(position).getPrice().contains("/")) {
            fragmentBinding.total.setText(((DashboardActivity) requireActivity()).getList().get(position).getPrice().substring(0, ((DashboardActivity) requireActivity()).getList().get(position).getPrice().indexOf(" ")));
            fragmentBinding.unit.setText(((DashboardActivity) requireActivity()).getList().get(position).getPrice().substring(((DashboardActivity) requireActivity()).getList().get(position).getPrice().indexOf(" ") + 1).replaceFirst(" ", "\n"));
        }

        actualSku = ((DashboardActivity) requireActivity()).getList().get(position).getSKu();

        Multimap<Integer, Integer> map = ArrayListMultimap.create();
        for (BundleItem item : bundles) {
            map.put(item.getInternet(), item.getCall());
        }
        ArrayList<String> internetBundlesList = new ArrayList<>();
        TreeSet<Integer> internet = new TreeSet<>(map.keySet());
        for (int item : internet) {
            internetBundlesList.add(item + " " + bundles.get(0).getInternetUnit());
        }
        ArrayList<String> callsBundlesList = new ArrayList<>();
        TreeSet<Integer> calls = new TreeSet<>(map.values());
        for (int item : calls) {
            callsBundlesList.add(item + bundles.get(0).getCallUnit());
        }

        fragmentBinding.internetSeekBar.setNestedScrollingEnabled(false);
        fragmentBinding.internetSeekBar.setTickCount(internetBundlesList.size());
        fragmentBinding.internetSeekBar.customTickTexts(internetBundlesList.toArray(new String[0]));
        fragmentBinding.internetSeekBar.customTickTextsTypeface(Objects.requireNonNull(ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)));
        fragmentBinding.callsSeekBar.setNestedScrollingEnabled(false);
        fragmentBinding.callsSeekBar.setTickCount(callsBundlesList.size());
        fragmentBinding.callsSeekBar.customTickTexts(callsBundlesList.toArray(new String[0]));
        fragmentBinding.callsSeekBar.customTickTextsTypeface(Objects.requireNonNull(ResourcesCompat.getFont(requireContext(), R.font.montserrat_bold)));

        fragmentBinding.internetSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (canInternetSeek) {
                    canCallSeek = false;
                    int key = Integer.parseInt(seekParams.tickText.substring(0, seekParams.tickText.indexOf(" ")));
                    TreeSet<Integer> call = new TreeSet<>(map.get(key));
                    fragmentBinding.callsSeekBar.setProgress(fragmentBinding.callsSeekBar.getMax() * callsBundlesList.indexOf(call.last() + "h") / (callsBundlesList.size() - 1));

                    BundleItem item = searchBundleByDetails(bundles, key, call.last());
                    selectedSku = item.getSku();
                    fragmentBinding.bundlePrice.setText(String.valueOf(item.getPrice()));
                    fragmentBinding.changeBundleBtn.setEnabled(!selectedSku.equalsIgnoreCase(actualSku));
                }
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
                canInternetSeek = true;
                canCallSeek = true;
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
                canCallSeek = true;
            }
        });

        fragmentBinding.callsSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (canCallSeek) {
                    canInternetSeek = false;
                    int value = Integer.parseInt(seekParams.tickText.replace("h", ""));
                    int key = 0;
                    for (Map.Entry<Integer, Collection<Integer>> entry : map.asMap().entrySet()) {
                        if (entry.getValue().contains(value)) {
                            key = entry.getKey();
                        }
                    }
                    fragmentBinding.internetSeekBar.setProgress(fragmentBinding.internetSeekBar.getMax() * internetBundlesList.indexOf(key + " Go") / (internetBundlesList.size() - 1));

                    BundleItem item = searchBundleByDetails(bundles, key, value);
                    selectedSku = item.getSku();
                    fragmentBinding.bundlePrice.setText(String.valueOf(item.getPrice()));
                    fragmentBinding.changeBundleBtn.setEnabled(!selectedSku.equalsIgnoreCase(actualSku));
                }
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
                canInternetSeek = true;
                canCallSeek = true;
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
                canInternetSeek = true;
            }
        });

        BundleItem item = searchBundleBySku(bundles, actualSku);
        fragmentBinding.internetSeekBar.setProgress(fragmentBinding.internetSeekBar.getMax() * internetBundlesList.indexOf(item.getInternet() + " Go") / (internetBundlesList.size() - 1));
        fragmentBinding.callsSeekBar.setProgress(fragmentBinding.callsSeekBar.getMax() * callsBundlesList.indexOf(item.getCall() + "h") / (callsBundlesList.size() - 1));
        fragmentBinding.bundlePrice.setText(String.valueOf(item.getPrice()));

        fragmentBinding.body.setVisibility(View.VISIBLE);
        fragmentBinding.changeBundleBtn.setOnClickListener(v -> switchBundle());
    }

    private BundleItem searchBundleByDetails(List<BundleItem> bundles, int internet, int call) {
        final Predicate<BundleItem> itemPredicate = item -> item.getInternet() == internet && item.getCall() == call;
        final List<BundleItem> results = Lists.newArrayList(Iterables.filter(bundles, itemPredicate));
        return results.get(0);
    }

    private BundleItem searchBundleBySku(List<BundleItem> bundles, String sku) {
        final Predicate<BundleItem> itemPredicate = item -> item.getSku().equals(sku);
        final List<BundleItem> results = Lists.newArrayList(Iterables.filter(bundles, itemPredicate));
        return results.get(0);
    }

    private void switchBundle() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        viewModel.switchBundle(preferenceManager.getValue(Constants.TOKEN, ""), ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(), selectedSku, preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleSwitchBundleData(Resource<CMIPaymentData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                Uri uri = Uri.parse(responseData.data.getResponse().getUrl());
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                intentBuilder.setStartAnimations(requireContext(), android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                intentBuilder.setExitAnimations(requireContext(), android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent = intentBuilder.build();
                customTabsIntent.launchUrl(requireActivity(), uri);
                break;
            case INVALID_TOKEN:
                assert responseData.data != null;
                Utilities.showErrorPopupWithClick(requireContext(), responseData.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }

    }
}