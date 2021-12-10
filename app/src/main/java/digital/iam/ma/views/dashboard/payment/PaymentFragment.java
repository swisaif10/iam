package digital.iam.ma.views.dashboard.payment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
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
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.orders.GetOrdersResponse;
import digital.iam.ma.models.orders.Order;
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
    private PreferenceManager preferenceManager;
    private int position = 0;

    public PaymentFragment() {
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

        assert getArguments() != null;
        position = getArguments().getInt("POSITION");
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

            Bundle bundle2 = new Bundle();
            CashPaymentFragment cashPaymentFragment = new CashPaymentFragment();
            bundle2.putInt(Constants.POSITION, position);
            cashPaymentFragment.setArguments(bundle2);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, cashPaymentFragment, Constants.MTCASH_PAYMENT)
                    .addToBackStack(null)
                    .commit();

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

}