package digital.iam.ma.views.dashboard.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.databinding.FragmentHomeBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnRadioChecked;
import digital.iam.ma.listener.OnRechargeSelectedListener;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.consumption.MyConsumptionResponse;
import digital.iam.ma.models.login.Line;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.orders.GetOrdersResponse;
import digital.iam.ma.models.orders.Order;
import digital.iam.ma.models.recharge.RechargeItem;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.models.recharge.RechargeListResponse;
import digital.iam.ma.models.recharge.RechargePurchase;
import digital.iam.ma.models.recharge.RechargeSubItem;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.HomeViewModel;
import digital.iam.ma.viewmodels.RechargeViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class HomeFragment extends Fragment {

    private static final String REQUEST_CODE = "100";

    private FragmentHomeBinding fragmentBinding;
    private HomeViewModel viewModel;
    private PreferenceManager preferenceManager;
    private RechargeViewModel rechargeViewModel;
    private int position;

    public HomeFragment(int position) {
        this.position = position;
    }


    public static HomeFragment newInstance(Boolean isFirstLogin) {
        HomeFragment fragment = new HomeFragment(0);
        Bundle args = new Bundle();
        args.putBoolean("is_first_login", isFirstLogin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getBoolean("is_first_login")) {
            Utilities.showErrorPopupWithClick(requireContext(), "Connecté avec succès à Gray !", v -> {
                BiometricManager biometricManager = BiometricManager.from(requireContext());
                switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Utilities.showErrorPopup(requireContext(), getString(R.string.biometrics_message_description));
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Log.e("MY_APP_TAG", "No biometric features available on this device.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Utilities.showBiometricsPromptPopup(requireContext(), getString(R.string.biometrics_message_description),
                                v1 -> startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS)));
                }
            });
        }

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        rechargeViewModel = ViewModelProviders.of(this).get(RechargeViewModel.class);
        viewModel.getMyConsumptionLiveData().observe(this, this::handleMyConsumptionData);
        viewModel.getGetOrdersLiveData().observe(this, this::handleGetOrdersData);
        viewModel.getRechargeListLiveData().observe(this, this::handleGetRechargesListData);
        viewModel.getRenewBundleLiveData().observe(this, this::handleRenewBundleData);
        rechargeViewModel.getRechargePurchase().observe(this, this::handleRechargePurchase);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
    }

    private void handleRechargePurchase(Resource<RechargePurchase> rechargePurchaseResource) {
        switch (rechargePurchaseResource.status) {
            case SUCCESS:
                fragmentBinding.loader.setVisibility(View.GONE);
                ((DashboardActivity) requireActivity()).activateUserInteraction();
                assert rechargePurchaseResource.data != null;
                Uri uri = Uri.parse(rechargePurchaseResource.data.getResponse().getUrl());
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setStartAnimations(requireContext(), android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                builder.setExitAnimations(requireContext(), android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(requireContext(), uri);
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), rechargePurchaseResource.message);
                break;
            case INVALID_TOKEN:
                assert rechargePurchaseResource.data != null;
                Utilities.showErrorPopupWithClick(requireContext(), rechargePurchaseResource.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getMyConsumption();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) requireActivity()).showHideTabLayout(View.VISIBLE);
    }

    private void init() {
        fragmentBinding.activateSimBtn.setOnClickListener(v -> Utilities.showActivateSimDialog(requireContext(), v1 -> {
            ((DashboardActivity) requireActivity()).addFragment(new ActivateSimFragment());
            requireActivity().getSupportFragmentManager().setFragmentResultListener(REQUEST_CODE, this, (requestKey, result) -> {
                if (requestKey.equals(REQUEST_CODE)) {
                    fragmentBinding.activateSimBtn.setVisibility(View.GONE);
                    fragmentBinding.rechargeBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#204CCB")));
                    fragmentBinding.renewBundleBtn.setVisibility(View.VISIBLE);
                }
            });
        }));
        fragmentBinding.rechargeBtn.setOnClickListener(v -> getRechargesList());
        fragmentBinding.renewBundleBtn.setOnClickListener(v -> Utilities.showConfirmRenewPopup(requireContext(), v12 -> renewBundle()));

        fragmentBinding.showMoreBtn.setOnClickListener(v -> ((DashboardActivity) requireActivity()).selectPaymentsTab());

        if (preferenceManager.getValue(Constants.IS_LINE_ACTIVATED, "").equalsIgnoreCase("pending")) {
            fragmentBinding.activateSimBtn.setVisibility(View.VISIBLE);
            fragmentBinding.rechargeBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E1E1E1")));
            fragmentBinding.renewBundleBtn.setVisibility(View.GONE);
        } else if (preferenceManager.getValue(Constants.IS_LINE_ACTIVATED, "").equalsIgnoreCase("active")) {
            fragmentBinding.activateSimBtn.setVisibility(View.GONE);
        }
    }

    private void getMyConsumption() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        viewModel.getMyConsumption(preferenceManager.getValue(Constants.TOKEN, ""), ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleMyConsumptionData(Resource<MyConsumptionData> responseData) {
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                initConsumption(responseData.data.getMyConsumptionResponse());
                getOrders();
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(requireContext(), responseData.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
            case ERROR:
                fragmentBinding.loader.setVisibility(View.GONE);
                ((DashboardActivity) requireActivity()).activateUserInteraction();
                Utilities.showErrorPopup(requireContext(), responseData.message);
                getOrders();
                break;
        }
    }

    private void initConsumption(MyConsumptionResponse response) {
        fragmentBinding.dataConsumptionReview.setValue(Float.parseFloat(response.getInternet().getPercent()));
        fragmentBinding.percentData.setText(String.format("%s%%", response.getInternet().getPercent()));
        fragmentBinding.consumedData.setText(String.valueOf(response.getInternet().getBundle()));

        fragmentBinding.voiceConsumptionReview.setValue(Float.parseFloat(response.getCalls().getPercent()));
        fragmentBinding.percentVoice.setText(String.format("%s%%", response.getCalls().getPercent()));
        fragmentBinding.consumedVoice.setText(String.valueOf(response.getCalls().getBundle()));

        fragmentBinding.body.setVisibility(View.VISIBLE);
    }

    private void getOrders() {
        viewModel.getOrders(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleGetOrdersData(Resource<GetOrdersData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();

        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                initOrders(responseData.data.getResponse());
                break;
            case INVALID_TOKEN:
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

    private void initOrders(GetOrdersResponse response) {
        fragmentBinding.paymentsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        List<Order> orders;
        int size = response.getPaidOrders().size();
        if (size >= 5)
            orders = response.getPaidOrders().subList(size - 5, size);
        else
            orders = response.getPaidOrders();
        fragmentBinding.paymentsList.setAdapter(new PaymentsAdapter(orders, preferenceManager.getValue(Constants.MSISDN,"")));
        fragmentBinding.paymentsList.setNestedScrollingEnabled(false);
    }

    private void getRechargesList() {
        viewModel.getRechargesList(preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleGetRechargesListData(Resource<RechargeListData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                List<RechargeItem> list = responseData.data.getResponse().getRecharges();
                for (RechargeItem item : list) {
                    Log.d("TAG", "handleGetRechargesListData: " + item.getType().getSku());
                }
                Utilities.showRechargeDialog(requireContext(), ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(), responseData.data.getResponse(), new OnRechargeSelectedListener() {

                    @Override
                    public void onPurchaseRecharge(String sku) {
                        fragmentBinding.loader.setVisibility(View.VISIBLE);
                        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
                        rechargeViewModel.rechargePurchase(preferenceManager.getValue(Constants.TOKEN, ""), sku, ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(), "fr");
                    }
                });
                break;
            case INVALID_TOKEN:
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

    private void renewBundle() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        viewModel.renewBundle(preferenceManager.getValue(Constants.TOKEN, ""), ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleRenewBundleData(Resource<CMIPaymentData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                Uri uri = Uri.parse(responseData.data.getResponse().getUrl());
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                intentBuilder.setStartAnimations(requireContext(), android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                intentBuilder.setExitAnimations(requireContext(), android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent = intentBuilder.build();
                customTabsIntent.launchUrl(requireActivity(), uri);
                break;
            case INVALID_TOKEN:
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