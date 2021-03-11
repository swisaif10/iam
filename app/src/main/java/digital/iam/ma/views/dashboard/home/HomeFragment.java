package digital.iam.ma.views.dashboard.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import digital.iam.ma.databinding.FragmentHomeBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.consumption.MyConsumptionResponse;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.HomeViewModel;
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
                            v -> startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS)));
            }
        }

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getMyConsumptionLiveData().observe(this, this::handleMyConsumptionData);

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

        getMyConsumption();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            fragmentBinding.activateSimBtn.setVisibility(View.GONE);
        }
    }

    private void init(MyConsumptionResponse response) {

        fragmentBinding.paymentsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentBinding.paymentsList.setAdapter(new PaymentsAdapter());
        fragmentBinding.paymentsList.setNestedScrollingEnabled(false);

        fragmentBinding.activateSimBtn.setOnClickListener(v -> Utilities.showActivateSimDialog(requireContext(), v1 -> {
            ActivateSimFragment addNewAddressFragment = new ActivateSimFragment();
            addNewAddressFragment.setTargetFragment(HomeFragment.this, REQUEST_CODE);
            ((DashboardActivity) requireActivity()).addFragment(addNewAddressFragment);
        }));

        fragmentBinding.rechargeBtn.setOnClickListener(v -> Utilities.showRechargeDialog(requireContext(), v12 -> Utilities.showConfirmRechargeDialog(requireContext())));

        fragmentBinding.dataConsumptionReview.setValue(Float.parseFloat(response.getMyConsumption().getInternet().getPercent()));
        fragmentBinding.percentData.setText(String.format("%s%%", response.getMyConsumption().getInternet().getPercent()));
        fragmentBinding.consumedData.setText(String.format("%sGO", response.getMyConsumption().getInternet().getConsumed()));

        fragmentBinding.voiceConsumptionReview.setValue(Float.parseFloat(response.getMyConsumption().getVoice().getPercent()));
        fragmentBinding.percentVoice.setText(String.format("%s%%", response.getMyConsumption().getVoice().getPercent()));
        fragmentBinding.consumedVoice.setText(String.format("%sh", response.getMyConsumption().getVoice().getConsumed()));
    }

    private void getMyConsumption() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getMyConsumption(preferenceManager.getValue(Constants.TOKEN, ""), "fr");
    }

    private void handleMyConsumptionData(Resource<MyConsumptionData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                init(responseData.data.getMyConsumptionResponse());
                break;
            case LOADING:
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }
}