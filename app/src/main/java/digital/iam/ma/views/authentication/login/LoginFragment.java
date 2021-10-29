package digital.iam.ma.views.authentication.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Executor;

import digital.iam.ma.databinding.FragmentLoginBinding;
import digital.iam.ma.databinding.FragmentLoginNewBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.AuthenticationViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.authentication.offers.DiscoverOffersFragment;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class LoginFragment extends Fragment {

    private FragmentLoginNewBinding fragmentBinding;
    private AuthenticationViewModel viewModel;
    private PreferenceManager preferenceManager;
    private Boolean isFirstLogin = true;
    private String recoveredEmail;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        viewModel.getLoginLiveData().observe(this, this::handleLoginData);
        viewModel.getResetPasswordLiveData().observe(this, this::handleResetPasswordData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentLoginNewBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {

        fragmentBinding.discoverOffersBtn.setPaintFlags(fragmentBinding.discoverOffersBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        fragmentBinding.forgottenPasswordBtn.setOnClickListener(v -> Utilities.showResetPasswordDialog(requireContext(), this::resetPassword));
        fragmentBinding.container.setOnClickListener(v -> Utilities.hideSoftKeyboard(requireContext(), requireView()));
        fragmentBinding.loginBtn.setOnClickListener(v -> {
            if (!fragmentBinding.username.getText().toString().equalsIgnoreCase("")
                    && !fragmentBinding.password.getText().toString().equalsIgnoreCase(""))
                login(fragmentBinding.username.getText().toString(), fragmentBinding.password.getText().toString());
        });
        fragmentBinding.empreinte.setOnClickListener(v -> enableTouchID());
        fragmentBinding.face.setOnClickListener(v -> enableTouchID());
        fragmentBinding.eye.setOnClickListener(v -> enableTouchID());

        if (!preferenceManager.getValue(Constants.EMAIL, "").equalsIgnoreCase("")
                && !preferenceManager.getValue(Constants.PASSWORD, "").equalsIgnoreCase("")) {
            BiometricManager biometricManager = BiometricManager.from(requireContext());
            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                fragmentBinding.empreinte.setVisibility(View.VISIBLE);
                fragmentBinding.face.setVisibility(View.VISIBLE);
                fragmentBinding.eye.setVisibility(View.VISIBLE);
            }
            isFirstLogin = false;
        }

        fragmentBinding.eye.setOnClickListener(v -> {
            if (fragmentBinding.password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                fragmentBinding.password.setTransformationMethod(null);
            } else {
                fragmentBinding.password.setTransformationMethod(new PasswordTransformationMethod());
            }
            fragmentBinding.password.setSelection(fragmentBinding.password.getText().length());
        });

        fragmentBinding.showPassword.setOnClickListener(v -> {
            if (fragmentBinding.password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                fragmentBinding.password.setTransformationMethod(null);
            } else {
                fragmentBinding.password.setTransformationMethod(new PasswordTransformationMethod());
            }
            fragmentBinding.password.setSelection(fragmentBinding.password.getText().length());
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fragmentBinding.loginBtn.setEnabled(checkInputs());
            }
        };

        fragmentBinding.username.addTextChangedListener(textWatcher);
        fragmentBinding.password.addTextChangedListener(textWatcher);

        fragmentBinding.discoverOffersBtn.setOnClickListener(v -> ((AuthenticationActivity) requireActivity()).addFragment(new DiscoverOffersFragment()));
    }

    private Boolean checkInputs() {
        return !fragmentBinding.username.getText().toString().equalsIgnoreCase("") && !fragmentBinding.password.getText().toString().equalsIgnoreCase("");
    }

    private void login(String username, String password) {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        //((DashboardActivity) requireActivity()).deactivateUserInteraction();
        viewModel.login(username, password, false, preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleLoginData(Resource<LoginData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        //((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                try {
                    preferenceManager.putValue(Constants.TOKEN, responseData.data.getResponse().getToken());
                    //preferenceManager.putValue(Constants.LINE_DETAILS, responseData.data.getResponse().getLines().get(0));
                    setList(Constants.LINE_DETAILS, responseData.data.getResponse().getLines());
                    preferenceManager.putValue(Constants.IS_LINE_ACTIVATED, responseData.data.getResponse().getLines().get(0).getState());
                    preferenceManager.putValue(Constants.MSISDN, responseData.data.getResponse().getLines().get(0).getMsisdn());
                    preferenceManager.putValue(Constants.FIRSTNAME, responseData.data.getResponse().getFirstname());
                    preferenceManager.putValue(Constants.LASTNAME, responseData.data.getResponse().getLastname());
                    preferenceManager.putValue(Constants.PHONE_NUMBER, responseData.data.getResponse().getPhoneNumber());
                    preferenceManager.putValue(Constants.ADDRESS, responseData.data.getResponse().getShippingAddress());
                    preferenceManager.putValue(Constants.CITY, responseData.data.getResponse().getShippingCity());
                    preferenceManager.putValue(Constants.POSTAL_CODE, responseData.data.getResponse().getShippingPostcode());
                    preferenceManager.putValue(Constants.CIN, responseData.data.getResponse().getCin());
                    preferenceManager.putValue(Constants.GENDER, responseData.data.getResponse().getGender());
                    System.out.println("Token : " + responseData.data.getResponse().getToken());
                    if (isFirstLogin) {
                        preferenceManager.putValue(Constants.EMAIL, fragmentBinding.username.getText().toString());
                        preferenceManager.putValue(Constants.PASSWORD, fragmentBinding.password.getText().toString());
                    }
                    preferenceManager.putValue(Constants.IS_LOGGED_IN, true);
                    Intent intent = new Intent(requireActivity(), DashboardActivity.class);
                    intent.putExtra("is_first_login", isFirstLogin);
                    startActivity(intent);
                    requireActivity().finish();
                } catch (Exception e) {
                    Utilities.showErrorPopup(requireContext(), "Une erreur est servenue");
                }
                break;
            case INVALID_TOKEN:
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

    private void enableTouchID() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Connexion biométrique")
                .setSubtitle("Connectez-vous à l’aide de vos informations d’identification biométriques")
                .setNegativeButtonText("Annuler")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build();


        Executor executor = ContextCompat.getMainExecutor(requireContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(requireActivity(),
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                login(preferenceManager.getValue(Constants.EMAIL, ""), preferenceManager.getValue(Constants.PASSWORD, ""));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        biometricPrompt.authenticate(promptInfo);

    }

    private void resetPassword(String email) {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        //((DashboardActivity) requireActivity()).deactivateUserInteraction();
        recoveredEmail = email;
        viewModel.resetPassword(email, preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleResetPasswordData(Resource<UpdatePasswordData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        //((DashboardActivity) requireActivity()).activateUserInteraction();
        switch (responseData.status) {
            case SUCCESS:
                Utilities.showErrorPopup(requireContext(), "Veuillez vérifier votre boite mail.");
                preferenceManager.putValue(Constants.RECOVERED_EMAIL, recoveredEmail);
                break;
            case INVALID_TOKEN:
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public void set(String key, String value) {
        preferenceManager.putValue(key, value);
    }

}