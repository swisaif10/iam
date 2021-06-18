package digital.iam.ma.views.dashboard.home;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import digital.iam.ma.databinding.FragmentHomeBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.consumption.MyConsumptionResponse;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.orders.GetOrdersResponse;
import digital.iam.ma.models.orders.Order;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.HomeViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class HomeFragment extends Fragment {

    private static final int REQUEST_CODE = 100;

    private FragmentHomeBinding fragmentBinding;
    private HomeViewModel viewModel;
    private PreferenceManager preferenceManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(Boolean isFirstLogin) {
        HomeFragment fragment = new HomeFragment();
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
                        Utilities.showErrorPopup(requireContext(), "Vous pouvez utiliser vos informations d'identification biométriques pour la prochaine authentification.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Log.e("MY_APP_TAG", "No biometric features available on this device.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Utilities.showBiometricsPromptPopup(requireContext(), "Vous pouvez utiliser vos informations d'identification biométriques pour la prochaine authentification.",
                                v1 -> startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS)));
                }
            });
        }

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getMyConsumptionLiveData().observe(this, this::handleMyConsumptionData);
        viewModel.getGetOrdersLiveData().observe(this, this::handleGetOrdersData);
        viewModel.getRechargeListLiveData().observe(this, this::handleGetRechargesListData);
        viewModel.getRenewBundleLiveData().observe(this, this::handleRenewBundleData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            fragmentBinding.activateSimBtn.setVisibility(View.GONE);
            fragmentBinding.rechargeBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#204CCB")));
            fragmentBinding.renewBundleBtn.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        fragmentBinding.activateSimBtn.setOnClickListener(v -> Utilities.showActivateSimDialog(requireContext(), v1 -> {
            ActivateSimFragment activateSimFragment = new ActivateSimFragment();
            activateSimFragment.setTargetFragment(HomeFragment.this, REQUEST_CODE);
            ((DashboardActivity) requireActivity()).addFragment(activateSimFragment);
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
        viewModel.getMyConsumption(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
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
        fragmentBinding.paymentsList.setAdapter(new PaymentsAdapter(orders, preferenceManager.getValue(Constants.MSISDN, "")));
        fragmentBinding.paymentsList.setNestedScrollingEnabled(false);
    }

    private void getRechargesList() {
        viewModel.getRechargesList(preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleGetRechargesListData(Resource<RechargeListData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                Utilities.showRechargeDialog(requireContext(), preferenceManager.getValue(Constants.MSISDN, ""), responseData.data.getResponse(), (rechargeItem, rechargeSubItem) -> {
                    Utilities.showConfirmRechargeDialog(requireContext());
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
        viewModel.renewBundle(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleRenewBundleData(Resource<CMIPaymentData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                Uri uri = Uri.parse(responseData.data.getResponse().getUrl());
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                //intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                //intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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