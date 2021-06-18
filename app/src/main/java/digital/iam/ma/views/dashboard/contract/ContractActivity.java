package digital.iam.ma.views.dashboard.contract;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityContractBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnConfirmClickListener;
import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.models.login.Line;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.ContractViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.base.BaseActivity;

public class ContractActivity extends BaseActivity implements OnConfirmClickListener {

    private ActivityContractBinding activityBinding;
    private PreferenceManager preferenceManager;
    private ContractViewModel viewModel;
    private Line line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_contract);
        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        viewModel = ViewModelProviders.of(this).get(ContractViewModel.class);
        viewModel.getSuspendContractLiveData().observe(this, this::handleSuspendContractData);
        viewModel.getSendOTPLiveData().observe(this, this::handleSendOTPData);
        viewModel.getEndContractLiveData().observe(this, this::handleEndContractData);
        viewModel.getChangeSIMLiveData().observe(this, this::handleChangeSIMData);
        viewModel.getResendPUKLiveData().observe(this, this::handleResendPUKData);

        init();
    }

    @Override
    public void onConfirmClick(String reason, String code, String type) {
        switch (type) {
            case "suspend":
                suspendContract(reason);
                break;
            case "renew":
                break;
            case "end":
                endContract(reason, code);
                break;
            case "change_sim":
                changeSIM();
                break;
            case "resend_puk":
                resendPUK(code);
                break;
        }
    }

    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar"))
            activityBinding.backImage.setRotation(180f);
        line = preferenceManager.getValue(Constants.LINE_DETAILS);
        activityBinding.backBtn.setOnClickListener(v -> finish());
        activityBinding.suspendContractBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.suspend_contract_title), getString(R.string.suspend_contract_message), "suspend", this));
        activityBinding.renewContractBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.renew_contract_title), "Votre nouveau contrat débutera le " + line.getExpireDate().replace("-", "/") + "\nLe forfait Gray sélectionné est de "
                + line.getBundleName() + " à " + line.getPrice() + " DH", "renew", this));
        activityBinding.endContractBtn.setOnClickListener(v -> sendOTP());
        activityBinding.changeSIMBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.change_sim_title), getString(R.string.change_sim_message), "change_sim", this));
        activityBinding.resendCodePUKBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.resend_puk_title), getString(R.string.resend_puk_message), "resend_puk", this));
    }

    private void suspendContract(String reason) {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.suspendContract(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), reason, "fr");
    }

    private void handleSuspendContractData(Resource<SuspendContractData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                Utilities.showErrorPopup(this, responseData.data.getHeader().getMessage());
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    private void sendOTP() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.sendOTP(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), "fr");
    }

    private void handleSendOTPData(Resource<SuspendContractData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                Utilities.showContractDialog(this, getString(R.string.end_contract_title), "Si vous confirmez et mettez fin à votre contrat maintenant, vous continuerez d’en bénéficier jusqu’au " + line.getExpireDate().replace("-", "/"), "end", this);
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    private void endContract(String reason, String code) {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.endContract(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), reason, code, "fr");
    }

    private void handleEndContractData(Resource<SuspendContractData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                Utilities.showErrorPopup(this, responseData.data.getHeader().getMessage());
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    private void changeSIM() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.changeSIM(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), "fr");
    }

    private void handleChangeSIMData(Resource<SuspendContractData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                Utilities.showErrorPopup(this, responseData.data.getHeader().getMessage());
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    private void resendPUK(String msisdn) {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.resendPUK(preferenceManager.getValue(Constants.TOKEN, ""), msisdn, "fr");
    }

    private void handleResendPUKData(Resource<SuspendContractData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                Utilities.showErrorPopup(this, responseData.data.getHeader().getMessage());
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
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