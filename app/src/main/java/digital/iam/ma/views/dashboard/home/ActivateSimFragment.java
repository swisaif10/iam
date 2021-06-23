package digital.iam.ma.views.dashboard.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.databinding.FragmentActivateSimBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.NumericKeyBoardTransformationMethod;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.HomeViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class ActivateSimFragment extends Fragment {

    private FragmentActivateSimBinding fragmentBinding;
    private HomeViewModel viewModel;
    private PreferenceManager preferenceManager;

    public ActivateSimFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getActivateSIMLiveData().observe(this, this::handleSIMActivationData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentActivateSimBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        fragmentBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        fragmentBinding.container.setOnClickListener(v -> Utilities.hideSoftKeyboard(requireContext(), requireView()));
        fragmentBinding.validateBtn.setOnClickListener(v -> activateSim());

        fragmentBinding.code.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        fragmentBinding.code.setTransformationMethod(new NumericKeyBoardTransformationMethod());
    }

    private void activateSim() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.activateSIM(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), fragmentBinding.code.getText().toString(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleSIMActivationData(Resource<ResponseData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                ((DashboardActivity) requireActivity()).getSupportFragmentManager().setFragmentResult("100", new Bundle());
                requireActivity().getSupportFragmentManager().popBackStack();
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(requireContext(), responseData.data.getHeader().getMessage(), v -> {
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