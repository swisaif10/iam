package digital.iam.ma.views.dashboard.payment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import digital.iam.ma.R;
import digital.iam.ma.databinding.FragmentPaymentBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnItemSelectedListener;
import digital.iam.ma.listener.OnRenew;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.fatourati.FatouratiResponse;
import digital.iam.ma.models.login.Line;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.orders.GetOrdersResponse;
import digital.iam.ma.models.orders.Order;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.PaymentViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;
import digital.iam.ma.views.dashboard.payment.paymentmode.CashPaymentFragment;
import digital.iam.ma.views.dashboard.payment.paymentmode.MobilePaymentFragment;

public class PaymentFragment extends Fragment implements OnItemSelectedListener {

    private FragmentPaymentBinding fragmentBinding;
    private PaymentViewModel viewModel;
    private PaymentData paymentData;
    private PreferenceManager preferenceManager;
    private int position = 0;
    private Line line;

    public PaymentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
        viewModel.getGetOrdersLiveData().observe(this, this::handleGetOrdersData);
        viewModel.getRenewBundleLiveData().observe(this, this::handleRenewBundleData);
        viewModel.getOrderPaymentLiveData().observe(this, this::handleOrderPaymentData);
        viewModel.getPaymentListLiveData().observe(this, this::handlePaymentListData);
        viewModel.getFatouratiLiveData().observe(this, this::handleFatouratiData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        if (getArguments() != null){
            position = getArguments().getInt("POSITION");
        }

        line = ((DashboardActivity) requireActivity()).getList().get(position);

    }

    private void handleFatouratiData(Resource<FatouratiResponse> fatouratiResponseResource) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (fatouratiResponseResource.status) {
            case SUCCESS:
                if (fatouratiResponseResource.data.response.code == 200){
                    String ref = "";
                    if (fatouratiResponseResource.data != null)
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentPaymentBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getOrders();
        getPaymentList();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) requireActivity()).showHideTabLayout(View.VISIBLE);
    }

    @Override
    public void onItemSelected(Order order, Boolean payment) {
        if (payment){
            Utilities.showPaymentDialog(getContext(), this.paymentData, new OnRenew() {
                @Override
                public void onPurchase(int mode) {
                    switch(mode){
                        case 0:
                            payOrder(String.valueOf(order.getOrderId()));
                            break;
                        case 1:
                            Log.d("ORDER", "onPurchase: " + order.getOrderTotal());
                            payOrderViaAppMobile(order);
                            break;
                        case 2:
                            ((DashboardActivity) requireActivity()).replaceFragment(new CashPaymentFragment(), "cashPayment");
                            break;
                    }
                }
            });
        }
        else {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(requireContext(), Uri.parse("http://www.orimi.com/pdf-test.pdf"));
            /*
            Intent intent = new Intent(requireContext(), WebViewActivity.class);
            intent.putExtra("url", "http://www.orimi.com/pdf-test.pdf");
            startActivity(intent);*/
        }
    }

    private void payOrderViaAppMobile(Order order){
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        String price = order.getOrderTotal().replace("MAD","");
        String fullname = preferenceManager.getValue(Constants.FIRSTNAME, "") +
                " " +
                preferenceManager.getValue(Constants.LASTNAME, "");
        viewModel.getFatourati(
                price.trim(),
                String.valueOf(order.getOrderId()),
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
                init(responseData.data.getResponse());
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

    private void init(GetOrdersResponse response) {

        fragmentBinding.bundleName.setText(((DashboardActivity) requireActivity()).getList().get(position).getBundleName());
        fragmentBinding.msisdn.setText(((DashboardActivity) requireActivity()).getList().get(position).getMsisdn());
        fragmentBinding.price.setText(((DashboardActivity) requireActivity()).getList().get(position).getPrice());

        fragmentBinding.payBtn.setOnClickListener(v -> {
            fragmentBinding.layoutModePayment.setVisibility(View.VISIBLE);
        });
        fragmentBinding.paidOrdersBtn.setOnClickListener(v -> {
            if (fragmentBinding.paidOrdersList.getVisibility() == View.VISIBLE) {
                fragmentBinding.paidOrdersList.setVisibility(View.GONE);
                fragmentBinding.paidArow.setRotation(90);
            } else {
                fragmentBinding.paidOrdersList.setVisibility(View.VISIBLE);
                fragmentBinding.paidArow.setRotation(270);
            }
        });
        fragmentBinding.unpaidOrdersBtn.setOnClickListener(v -> {
            if (fragmentBinding.unpaidOrdersList.getVisibility() == View.VISIBLE) {
                fragmentBinding.unpaidOrdersList.setVisibility(View.GONE);
                fragmentBinding.unpaidArrow.setRotation(90);
            } else {
                fragmentBinding.unpaidOrdersList.setVisibility(View.VISIBLE);
                fragmentBinding.unpaidArrow.setRotation(270);
            }
        });

        fragmentBinding.paidRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentBinding.paidRecycler.setAdapter(new PaymentsAdapter(requireContext(), response.getPaidOrders(), this));
        fragmentBinding.paidRecycler.setNestedScrollingEnabled(false);

        fragmentBinding.unpaidRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentBinding.unpaidRecycler.setAdapter(new PaymentsAdapter(requireContext(), response.getPendingOrders(), this));
        fragmentBinding.unpaidRecycler.setNestedScrollingEnabled(false);

        fragmentBinding.radioGroup.setOnCheckedChangeListener(this::doOnModePaymentCheckChanged);

        fragmentBinding.body.setVisibility(View.VISIBLE);
    }

    private void doOnModePaymentCheckChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        fragmentBinding.loader.setVisibility(View.VISIBLE);

        if (checkedRadioId == fragmentBinding.radio0.getId()) {

            ((DashboardActivity) requireActivity()).deactivateUserInteraction();
            viewModel.renewBundle(
                    preferenceManager.getValue(Constants.TOKEN, ""),
                    ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(),
                    preferenceManager.getValue(Constants.LANGUAGE, "fr")
            );

            updateRadioGroup(fragmentBinding.radio0);
        } else if (checkedRadioId == fragmentBinding.radio1.getId()) {

            Bundle bundle = new Bundle();
            MobilePaymentFragment mobilePaymentFragment = new MobilePaymentFragment();
            bundle.putInt(Constants.POSITION, position);
            mobilePaymentFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, mobilePaymentFragment, Constants.MOBILE_PAYMENT)
                    .addToBackStack(null)
                    .commit();

            updateRadioGroup(fragmentBinding.radio1);
        } else if (checkedRadioId == fragmentBinding.radio2.getId()) {

            ((DashboardActivity) requireActivity()).replaceFragment(new CashPaymentFragment(), "cashPayment");

            updateRadioGroup(fragmentBinding.radio2);
        }
    }

    private void updateRadioGroup(RadioButton selected) {

        fragmentBinding.radio0.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.radio_off));
        fragmentBinding.radio1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.radio_off));
        fragmentBinding.radio2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.radio_off));

        selected.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.radio_on));

        ColorStateList colorStateList = new ColorStateList(
                new int[][]
                        {
                                new int[]{-android.R.attr.state_enabled}, // Disabled
                                new int[]{android.R.attr.state_enabled}   // Enabled
                        },
                new int[]
                        {
                                ContextCompat.getColor(getContext(), R.color.grey), // disabled
                                ContextCompat.getColor(getContext(), R.color.radio_tint)   // enabled
                        }
        );
        fragmentBinding.radio0.setButtonTintList(colorStateList);
        fragmentBinding.radio1.setButtonTintList(colorStateList);
        fragmentBinding.radio2.setButtonTintList(colorStateList);
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

    private void payOrder(String id) {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        viewModel.payOrder(preferenceManager.getValue(Constants.TOKEN, ""), id, ((DashboardActivity) requireActivity()).getList().get(position).getMsisdn(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleOrderPaymentData(Resource<CMIPaymentData> responseData) {
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

    private void getPaymentList() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        ((DashboardActivity) requireActivity()).deactivateUserInteraction();
        viewModel.getPaymentList(preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handlePaymentListData(Resource<PaymentData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        ((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                this.paymentData = responseData.data;
                initPaymentList(responseData.data);
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

    private void initPaymentList(PaymentData response) {

        if (response.getCmi()) {
            fragmentBinding.radio0.setVisibility(View.VISIBLE);
        }
        if (response.getFatourati()) {
            fragmentBinding.radio1.setVisibility(View.VISIBLE);
        }
        if (response.getMtcash()) {
            fragmentBinding.radio2.setVisibility(View.VISIBLE);
        }

    }

}