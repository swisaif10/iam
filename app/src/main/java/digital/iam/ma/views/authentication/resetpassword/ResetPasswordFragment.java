package digital.iam.ma.views.authentication.resetpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.databinding.FragmentResetPasswordBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.AuthenticationViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;

public class ResetPasswordFragment extends Fragment {

    FragmentResetPasswordBinding fragmentBinding;
    private PreferenceManager preferenceManager;
    private AuthenticationViewModel viewModel;
    private String resetToken;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance(String resetToken) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString("reset_token", resetToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        viewModel.getAddNewPasswordLiveData().observe(this, this::handleResetPasswordData);

        if (getArguments() != null)
            resetToken = getArguments().getString("reset_token");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        fragmentBinding.container.setOnClickListener(v -> Utilities.hideSoftKeyboard(requireContext(), requireView()));
        fragmentBinding.cancelBtn.setOnClickListener(v -> goToLogin());
        fragmentBinding.saveBtn.setOnClickListener(v -> {
            if (fragmentBinding.newPassword.getText().toString().equalsIgnoreCase(fragmentBinding.confirmPassword.getText().toString()))
                addNewPassword();
        });
        fragmentBinding.showNewPassword.setOnClickListener(v -> {
            if (fragmentBinding.newPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                fragmentBinding.newPassword.setTransformationMethod(null);
            } else {
                fragmentBinding.newPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
            fragmentBinding.newPassword.setSelection(fragmentBinding.newPassword.getText().length());
        });
        fragmentBinding.showConfirmedPassword.setOnClickListener(v -> {
            if (fragmentBinding.confirmPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                fragmentBinding.confirmPassword.setTransformationMethod(null);
            } else {
                fragmentBinding.confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
            fragmentBinding.confirmPassword.setSelection(fragmentBinding.confirmPassword.getText().length());
        });
    }

    private void goToLogin() {
        startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
        requireActivity().finish();
    }

    private void addNewPassword() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.addNewPassword(preferenceManager.getValue(Constants.RECOVERED_EMAIL, ""), resetToken, fragmentBinding.newPassword.getText().toString(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleResetPasswordData(Resource<UpdatePasswordData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                goToLogin();
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