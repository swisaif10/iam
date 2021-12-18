package digital.iam.ma.views.dashboard.contract;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityContractBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnConfirmClickListener;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.consumption.MyConsumptionResponse;
import digital.iam.ma.models.contract.Contract;
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
    private int position = 0;

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
        viewModel.getMyConsumptionLiveData().observe(this, this::handleMyConsumptionData);
        viewModel.getChangeContract().observe(this, this::handleChangeContarct);
        Intent intent = getIntent();
        position = intent.getIntExtra(Constants.LINE, 0);
        getMyConsumption();
    }

    private void handleChangeContarct(Resource<Contract> contractResource) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (contractResource.status) {
            case SUCCESS:
                if (contractResource.data != null)
                    Toast.makeText(ContractActivity.this, contractResource.data.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                Utilities.showErrorPopup(this, contractResource.message);
                break;
            case INVALID_TOKEN:
                if (contractResource.data != null)
                    Utilities.showErrorPopupWithClick(this, contractResource.data.getHeader().getMessage(), v -> {
                        preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                        startActivity(new Intent(this, AuthenticationActivity.class));
                        finishAffinity();
                    });
                break;
        }

    }

    @Override
    public void onConfirmClick(String reason, String code, String type) {
        Log.d("CONTRACT", "onConfirmClick: " + type);
        String status = "";
        switch (type) {
            case "suspend":
                status += "suspension";
                changeContract(status);
                break;
                /*
                suspendContract(reason);
                break;*/
            case "renew":
                status += "reactivation";
                changeContract(status);
                break;
            case "end":
                status += "resiliation";
                changeContract(status);
                //endContract(reason, code);
                break;
            case "change_sim":
                changeSIM();
                break;
            case "resend_puk":
                resendPUK(code);
                break;
        }
    }

    public void changeContract(String type) {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.changeContract(preferenceManager.getValue(Constants.TOKEN, ""), getList().get(position).getMsisdn(), preferenceManager.getValue(Constants.LANGUAGE, "fr"), type);
    }

    private void getMyConsumption() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getMyConsumption(preferenceManager.getValue(Constants.TOKEN, ""), getList().get(position).getMsisdn(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleMyConsumptionData(Resource<MyConsumptionData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                init(responseData.data.getMyConsumptionResponse());
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

    private void init(MyConsumptionResponse response) {
        if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar"))
            activityBinding.backImage.setRotation(180f);
        line = getList().get(position);
        activityBinding.bundleName.setText(line.getBundleName());
        activityBinding.msisdn.setText(line.getMsisdn());
        activityBinding.phoneNumber.setText(preferenceManager.getValue(Constants.PHONE_NUMBER, ""));
        activityBinding.activationDate.setText(line.getStartDate().replace("-", "/"));
        activityBinding.expirationDate.setText(line.getExpireDate().replace("-", "/"));

        activityBinding.dataConsumptionReview.setValue(Float.parseFloat(response.getInternet().getPercent()));
        activityBinding.percentData.setText(String.format("%s%%", response.getInternet().getPercent()));
        activityBinding.consumedData.setText(String.valueOf(response.getInternet().getBundle()));

        activityBinding.voiceConsumptionReview.setValue(Float.parseFloat(response.getCalls().getPercent()));
        activityBinding.percentVoice.setText(String.format("%s%%", response.getCalls().getPercent()));
        activityBinding.consumedVoice.setText(String.valueOf(response.getCalls().getBundle()));

        activityBinding.backBtn.setOnClickListener(v -> finish());
        activityBinding.suspendContractBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.suspend_contract_title), getString(R.string.suspend_contract_message), "suspend", this));
        activityBinding.renewContractBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.renew_contract_title), getString(R.string.renew_contract_dialog_description)
                + line.getExpireDate().replace("-", "/") + getString(R.string.bundle_name_label)
                + line.getBundleName() + getString(R.string.to_label) + line.getPrice() + getString(R.string.dh_label), "renew", this));
        //activityBinding.endContractBtn.setOnClickListener(v -> sendOTP());
        activityBinding.endContractBtn.setOnClickListener( v ->                 Utilities.showContractDialog(this, getString(R.string.end_contract_title), getString(R.string.end_contract_dialog_description), "end", this));
        activityBinding.changeSIMBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.change_sim_title), getString(R.string.change_sim_message), "change_sim", this));
        activityBinding.resendCodePUKBtn.setOnClickListener(v -> Utilities.showContractDialog(this, getString(R.string.resend_puk_title), getString(R.string.resend_puk_message), "resend_puk", this));

        activityBinding.body.setVisibility(View.VISIBLE);
    }

    private void suspendContract(String reason) {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.suspendContract(preferenceManager.getValue(Constants.TOKEN, ""), getList().get(position).getMsisdn(), reason, preferenceManager.getValue(Constants.LANGUAGE, "fr"));
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
        viewModel.sendOTP(preferenceManager.getValue(Constants.TOKEN, ""), getList().get(position).getMsisdn(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleSendOTPData(Resource<SuspendContractData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                Utilities.showContractDialog(this, getString(R.string.end_contract_title), getString(R.string.end_contract_dialog_description) + line.getExpireDate().replace("-", "/"), "end", this);
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
        viewModel.endContract(preferenceManager.getValue(Constants.TOKEN, ""), getList().get(position).getMsisdn(), reason, code, preferenceManager.getValue(Constants.LANGUAGE, "fr"));
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
        viewModel.changeSIM(preferenceManager.getValue(Constants.TOKEN, ""), getList().get(position).getMsisdn(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
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
        viewModel.resendPUK(preferenceManager.getValue(Constants.TOKEN, ""), msisdn, preferenceManager.getValue(Constants.LANGUAGE, "fr"));
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

    public List<Line> getList() {
        List<Line> arrayItems;
        String serializedObject = preferenceManager.getValue(Constants.LINE_DETAILS, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Line>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            return arrayItems;
        }
        return null;
    }
}