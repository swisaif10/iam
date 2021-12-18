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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.databinding.FragmentHomeBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnRechargeSelectedListener;
import digital.iam.ma.listener.OnRenew;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.consumption.MyConsumptionResponse;
import digital.iam.ma.models.fatourati.FatouratiResponse;
import digital.iam.ma.models.login.Line;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.orders.GetOrdersResponse;
import digital.iam.ma.models.orders.Order;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.models.recharge.RechargeItem;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.models.recharge.RechargePurchase;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.BundlesViewModel;
import digital.iam.ma.viewmodels.HomeViewModel;
import digital.iam.ma.viewmodels.PaymentViewModel;
import digital.iam.ma.viewmodels.RechargeViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;
import digital.iam.ma.views.dashboard.payment.paymentmode.CashPaymentFragment;
import digital.iam.ma.views.dashboard.payment.paymentmode.MobilePaymentFragment;

public class HomeFragment extends Fragment {

    private static final String REQUEST_CODE = "100";

    FragmentHomeBinding fragmentBinding;
    private HomeViewModel viewModel;
    private PreferenceManager preferenceManager;
    private RechargeViewModel rechargeViewModel;
    private BundlesViewModel bundlesViewModel;
    private PaymentData responsePaymentData;
    private PaymentViewModel paymentViewModel;
    private Line line;
    private int position = 0;
    private int checkedMode;

    public HomeFragment() {

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

       /* if (getArguments() != null && getArguments().getString(Constants.QR_CODE) != null){
            ActivateSimFragment fragment = new ActivateSimFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.QR_CODE, getArguments().getString(Constants.QR_CODE));
            fragment.setArguments(bundle);
            ((DashboardActivity)requireActivity()).replaceFragment(fragment, "QR_FRAGMENT");
        }*/

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        rechargeViewModel = ViewModelProviders.of(this).get(RechargeViewModel.class);
        paymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
        bundlesViewModel = ViewModelProviders.of(this).get(BundlesViewModel.class);
        viewModel.getMyConsumptionLiveData().observe(this, this::handleMyConsumptionData);
        viewModel.getGetOrdersLiveData().observe(this, this::handleGetOrdersData);
        viewModel.getRechargeListLiveData().observe(this, this::handleGetRechargesListData);
        viewModel.getRenewBundleLiveData().observe(this, this::handleRenewBundleData);
        paymentViewModel.getFatouratiLiveData().observe(this, this::handleFatouratiData);
        rechargeViewModel.getRechargePurchase().observe(this, this::handleRechargePurchase);
        bundlesViewModel.getPaymentListLiveData().observe(this, this::handlePaymentListData);

        //assert getArguments() != null;
        if (getArguments() != null)
            position = getArguments().getInt(Constants.POSITION);

        line = ((DashboardActivity) requireActivity()).getList().get(position);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
    }

    private void handleFatouratiData(Resource<FatouratiResponse> fatouratiResponseResource) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (fatouratiResponseResource.status) {
            case SUCCESS:
                if (fatouratiResponseResource.data != null)
                    if (fatouratiResponseResource.data.response.code == 200) {
                        String ref = "";
                        ref += fatouratiResponseResource.data.getResponse().getRefFat();
                        Log.d("REF", "handleFatouratiData: " + ref);
                        MobilePaymentFragment mobilePaymentFragment = new MobilePaymentFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("REF", ref);
                        mobilePaymentFragment.setArguments(bundle);
                        ((DashboardActivity) requireActivity()).replaceFragment(mobilePaymentFragment, "MobilePayment");
                    }
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), fatouratiResponseResource.message);
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(requireContext(), fatouratiResponseResource.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
        }
    }

    // RECHARGE
    private void handleRechargePurchase(Resource<RechargePurchase> rechargePurchaseResource) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (rechargePurchaseResource.status) {
            case SUCCESS:
                switch (checkedMode) {
                    case 0:
                        if (rechargePurchaseResource.data != null) {
                            Uri uri = Uri.parse(rechargePurchaseResource.data.getResponse().getUrl());
                            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                            intentBuilder.setStartAnimations(requireContext(), android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            intentBuilder.setExitAnimations(requireContext(), android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right);
                            CustomTabsIntent customTabsIntent = intentBuilder.build();
                            customTabsIntent.launchUrl(requireActivity(), uri);
                        }
                        break;
                    case 1:
                        fragmentBinding.loader.setVisibility(View.VISIBLE);
                        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
                        String price = line.getPrice().trim().replaceAll("DH/mois", "");

                        String fullname =
                                preferenceManager.getValue(Constants.FIRSTNAME, "") +
                                        " " +
                                        preferenceManager.getValue(Constants.LASTNAME, "");

                        paymentViewModel.getFatourati(
                                String.valueOf((int) Float.parseFloat(price.replace(",", "."))),
                                String.valueOf(line.getOrderId()),
                                preferenceManager.getValue(Constants.EMAIL, ""),
                                fullname,
                                preferenceManager.getValue(Constants.PHONE_NUMBER, ""),
                                preferenceManager.getValue(Constants.ID, ""),
                                line.getMsisdn(),
                                "purchase",
                                "500",
                                preferenceManager.getValue(Constants.ADDRESS, ""),
                                preferenceManager.getValue(Constants.TOKEN, ""),
                                preferenceManager.getValue(Constants.LANGUAGE, "fr")
                        );
                        //redirectViaAppMobile();
                        break;
                    case 2:
                        redirectViaMtCash();
                        break;
                    default:
                        break;
                }

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
        getPaymentList();
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
        //fragmentBinding.renewBundleBtn.setOnClickListener(v -> Utilities.showConfirmRenewPopup(requireContext(), v12 -> renewBundle()));

        fragmentBinding.renewBundleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.showPaymentDialog(getContext(), responsePaymentData, new OnRenew() {
                    @Override
                    public void onPurchase(int mode) {
                        switch (mode) {
                            case 0:
                                renewBundle();
                                break;
                            case 1:
                                StringBuilder fullname = new StringBuilder();
                                fullname.append(preferenceManager.getValue(Constants.FIRSTNAME, ""));
                                fullname.append(" ");
                                fullname.append(preferenceManager.getValue(Constants.LASTNAME, ""));
                                fragmentBinding.loader.setVisibility(View.VISIBLE);
                                ((DashboardActivity) requireActivity()).deactivateUserInteraction();
                                String price = line.getPrice().trim().replaceAll("DH/mois", "");

                                paymentViewModel.getFatourati(
                                        String.valueOf((int) Float.parseFloat(price.replace(",", "."))),
                                        String.valueOf(line.getOrderId()),
                                        preferenceManager.getValue(Constants.EMAIL, ""),
                                        fullname.toString(),
                                        preferenceManager.getValue(Constants.PHONE_NUMBER, ""),
                                        preferenceManager.getValue(Constants.ID, ""),
                                        line.getMsisdn(),
                                        "purchase",
                                        "500",
                                        preferenceManager.getValue(Constants.ADDRESS, ""),
                                        preferenceManager.getValue(Constants.TOKEN, ""),
                                        preferenceManager.getValue(Constants.LANGUAGE, "fr")
                                );
                                break;
                            case 2:
                                ((DashboardActivity) requireActivity()).replaceFragment(new CashPaymentFragment(), "cashPayment");
                                break;


                        }
                    }
                });
            }
        });

        fragmentBinding.showMoreBtn.setOnClickListener(v -> ((DashboardActivity) requireActivity()).selectPaymentsTab());
        preferenceManager.putValue(Constants.IS_LINE_ACTIVATED, ((DashboardActivity) requireActivity()).getList().get(position).getState());
        if (preferenceManager.getValue(Constants.IS_LINE_ACTIVATED, "").equalsIgnoreCase("pending")) {
            fragmentBinding.activateSimBtn.setVisibility(View.VISIBLE);
            fragmentBinding.rechargeBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E1E1E1")));
            fragmentBinding.rechargeBtn.setEnabled(false);
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
                assert responseData.data != null;
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
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
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
        fragmentBinding.paymentsList.setAdapter(new PaymentsAdapter(orders, preferenceManager.getValue(Constants.MSISDN, "")));
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
                Utilities.showRechargeDialog(requireContext(), ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(), responseData.data.getResponse(), responsePaymentData, new OnRechargeSelectedListener() {

                    @Override
                    public void onPurchaseRecharge(String sku, int modePayment) {
                        checkedMode = modePayment;
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

    private void getPaymentList() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        bundlesViewModel.getPaymentList(preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handlePaymentListData(Resource<PaymentData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                responsePaymentData = responseData.data;
                break;
            case ERROR:
                responsePaymentData = null;
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

    private void redirectViaAppMobile() {
        Bundle bundle = new Bundle();
        MobilePaymentFragment mobilePaymentFragment = new MobilePaymentFragment();
        bundle.putInt(Constants.POSITION, position);
        mobilePaymentFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mobilePaymentFragment, Constants.MOBILE_PAYMENT)
                .addToBackStack(null)
                .commit();
    }

    private void redirectViaMtCash() {
        Bundle bundle2 = new Bundle();
        CashPaymentFragment cashPaymentFragment = new CashPaymentFragment();
        bundle2.putInt(Constants.POSITION, position);
        cashPaymentFragment.setArguments(bundle2);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, cashPaymentFragment, Constants.MTCASH_PAYMENT)
                .addToBackStack(null)
                .commit();
    }

}