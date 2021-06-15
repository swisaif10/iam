package digital.iam.ma.views.dashboard.personalinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityPersonalInformationBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.profile.UpdateProfileData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.NumericKeyBoardTransformationMethod;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.AccountViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.base.BaseActivity;

public class PersonalInformationActivity extends BaseActivity {

    private ActivityPersonalInformationBinding activityBinding;
    private AccountViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_information);

        viewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        viewModel.getUpdatePasswordLiveData().observe(this, this::handleUpdatePasswordData);
        viewModel.getUpdateProfileLiveData().observe(this, this::handleUpdateProfileData);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        init();
    }

    private void init() {
        activityBinding.backBtn.setOnClickListener(v -> finish());

        activityBinding.container.setOnClickListener(v -> Utilities.hideSoftKeyboard(this, activityBinding.getRoot()));

        activityBinding.updatePasswordBtn.setOnClickListener(v -> Utilities.showUpdatePasswordDialog(this, this::updatePassword));

        activityBinding.womenBtn.setChecked(true);

        activityBinding.phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        activityBinding.phoneNumber.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        activityBinding.phoneNumber.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_semibold));
        activityBinding.postalCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        activityBinding.postalCode.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        activityBinding.postalCode.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_semibold));

        activityBinding.firstName.setText(preferenceManager.getValue(Constants.FIRSTNAME, ""));
        activityBinding.lastName.setText(preferenceManager.getValue(Constants.LASTNAME, ""));
        activityBinding.phoneNumber.setText(preferenceManager.getValue(Constants.PHONE_NUMBER, ""));
        activityBinding.email.setText(preferenceManager.getValue(Constants.EMAIL, ""));
        activityBinding.address.setText(preferenceManager.getValue(Constants.ADDRESS, ""));
        activityBinding.city.setText(preferenceManager.getValue(Constants.CITY, ""));
        activityBinding.postalCode.setText(preferenceManager.getValue(Constants.POSTAL_CODE, ""));
        activityBinding.password.setText(preferenceManager.getValue(Constants.PASSWORD, ""));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                activityBinding.saveBtn.setEnabled(checkForm());
            }
        };

        activityBinding.firstName.addTextChangedListener(textWatcher);
        activityBinding.lastName.addTextChangedListener(textWatcher);
        activityBinding.phoneNumber.addTextChangedListener(textWatcher);
        activityBinding.address.addTextChangedListener(textWatcher);
        activityBinding.city.addTextChangedListener(textWatcher);
        activityBinding.postalCode.addTextChangedListener(textWatcher);

        activityBinding.saveBtn.setOnClickListener(v -> updateProfile());
    }

    private void updatePassword(String currentPassword, String newPassword) {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.updatePassword(preferenceManager.getValue(Constants.TOKEN, ""), currentPassword, newPassword, "fr");
    }

    private void handleUpdatePasswordData(Resource<UpdatePasswordData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                if (responseData.data.getResponse())
                    Utilities.showErrorPopup(this, "Votre mot de passe est modifié avec succés.");
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    private Boolean checkForm() {
        return Utilities.isNotEmpty(activityBinding.firstName) &&
                Utilities.isNotEmpty(activityBinding.lastName) &&
                Utilities.isNotEmpty(activityBinding.phoneNumber) &&
                Utilities.isNotEmpty(activityBinding.address) &&
                Utilities.isNotEmpty(activityBinding.city) &&
                Utilities.isNotEmpty(activityBinding.postalCode);
    }

    private void updateProfile() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.updateProfile(preferenceManager.getValue(Constants.TOKEN, ""), activityBinding.firstName.getText().toString(), activityBinding.lastName.getText().toString(),
                activityBinding.phoneNumber.getText().toString(), activityBinding.address.getText().toString(), activityBinding.city.getText().toString(), activityBinding.postalCode.getText().toString(), "fr");
    }

    private void handleUpdateProfileData(Resource<UpdateProfileData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                if (responseData.data.getHeader().getMessage().equalsIgnoreCase("updated")) {
                    preferenceManager.putValue(Constants.FIRSTNAME, responseData.data.getResponse().getFirstname());
                    preferenceManager.putValue(Constants.LASTNAME, responseData.data.getResponse().getLastname());
                    preferenceManager.putValue(Constants.PHONE_NUMBER, responseData.data.getResponse().getPhoneNumber());
                    preferenceManager.putValue(Constants.ADDRESS, responseData.data.getResponse().getAddress());
                    preferenceManager.putValue(Constants.CITY, responseData.data.getResponse().getCity());
                    preferenceManager.putValue(Constants.POSTAL_CODE, responseData.data.getResponse().getPostcode());

                    Utilities.showErrorPopup(this, "vos informations ont été mises à jour avec succès.");
                }
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }
}