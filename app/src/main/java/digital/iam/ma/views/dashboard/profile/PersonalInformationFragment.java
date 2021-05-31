package digital.iam.ma.views.dashboard.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.databinding.FragmentPersonalInformationsBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.ProfileViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;

public class PersonalInformationFragment extends Fragment {

    private FragmentPersonalInformationsBinding fragmentBinding;
    private ProfileViewModel viewModel;
    private PreferenceManager preferenceManager;

    public PersonalInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.getUpdatePasswordLiveData().observe(this, this::handleUpdatePasswordData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentPersonalInformationsBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        fragmentBinding.container.setOnClickListener(v -> Utilities.hideSoftKeyboard(requireContext(), requireView()));

        fragmentBinding.updatePasswordBtn.setOnClickListener(v -> Utilities.showUpdatePasswordDialog(requireContext(), this::updatePassword));

        fragmentBinding.womenBtn.setChecked(true);
    }

    private void updatePassword(String currentPassword, String newPassword) {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.updatePassword(preferenceManager.getValue(Constants.TOKEN, ""), currentPassword, newPassword, "fr");
    }

    private void handleUpdatePasswordData(Resource<UpdatePasswordData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                if (responseData.data.getResponse())
                    Utilities.showErrorPopup(requireContext(), "Votre mot de passe est modifié avec succés.");
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