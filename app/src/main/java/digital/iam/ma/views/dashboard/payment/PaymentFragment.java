package digital.iam.ma.views.dashboard.payment;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import digital.iam.ma.databinding.FragmentPaymentBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnItemSelectedListener;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.login.Line;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.orders.GetOrdersResponse;
import digital.iam.ma.models.orders.Order;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.PaymentViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;
import digital.iam.ma.views.webview.WebViewActivity;

public class PaymentFragment extends Fragment implements OnItemSelectedListener {

    private FragmentPaymentBinding fragmentBinding;
    private PaymentViewModel viewModel;
    private PreferenceManager preferenceManager;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
        viewModel.getGetOrdersLiveData().observe(this, this::handleGetOrdersData);
        viewModel.getRenewBundleLiveData().observe(this, this::handleRenewBundleData);
        viewModel.getOrderPaymentLiveData().observe(this, this::handleOrderPaymentData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) requireActivity()).showHideTabLayout(View.VISIBLE);
    }

    @Override
    public void onItemSelected(Order order, Boolean payment) {
        if (payment)
            payOrder(String.valueOf(order.getOrderId()));
        else {
            Intent intent = new Intent(requireContext(), WebViewActivity.class);
            intent.putExtra("url", "http://www.orimi.com/pdf-test.pdf");
            startActivity(intent);
        }
    }

    private void getOrders() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getOrders(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleGetOrdersData(Resource<GetOrdersData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                init(responseData.data.getResponse());
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

    private void init(GetOrdersResponse response) {

        Line line = preferenceManager.getValue(Constants.LINE_DETAILS);
        fragmentBinding.bundleName.setText(line.getBundleName());
        fragmentBinding.msisdn.setText(line.getMsisdn());
        fragmentBinding.price.setText(line.getPrice());

        fragmentBinding.payBtn.setOnClickListener(v -> renewBundle());
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

        fragmentBinding.body.setVisibility(View.VISIBLE);
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

    private void payOrder(String id) {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.payOrder(preferenceManager.getValue(Constants.TOKEN, ""), id, preferenceManager.getValue(Constants.MSISDN, ""), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleOrderPaymentData(Resource<CMIPaymentData> responseData) {
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