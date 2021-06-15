package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.repository.ContractRepository;
import digital.iam.ma.utilities.Resource;

public class ContractViewModel extends AndroidViewModel {

    private final ContractRepository repository;
    private final MutableLiveData<Resource<SuspendContractData>> suspendContractLiveData;
    private final MutableLiveData<Resource<SuspendContractData>> sendOTPLiveData;
    private final MutableLiveData<Resource<SuspendContractData>> endContractLiveData;
    private final MutableLiveData<Resource<SuspendContractData>> changeSIMLiveData;
    private final MutableLiveData<Resource<SuspendContractData>> resendPUKLiveData;

    public ContractViewModel(@NonNull Application application) {
        super(application);

        this.repository = new ContractRepository();
        suspendContractLiveData = new MutableLiveData<>();
        sendOTPLiveData = new MutableLiveData<>();
        endContractLiveData = new MutableLiveData<>();
        changeSIMLiveData = new MutableLiveData<>();
        resendPUKLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<SuspendContractData>> getSuspendContractLiveData() {
        return suspendContractLiveData;
    }

    public MutableLiveData<Resource<SuspendContractData>> getSendOTPLiveData() {
        return sendOTPLiveData;
    }

    public MutableLiveData<Resource<SuspendContractData>> getEndContractLiveData() {
        return endContractLiveData;
    }

    public MutableLiveData<Resource<SuspendContractData>> getChangeSIMLiveData() {
        return changeSIMLiveData;
    }

    public MutableLiveData<Resource<SuspendContractData>> getResendPUKLiveData() {
        return resendPUKLiveData;
    }

    public void suspendContract(String token, String msisdn, String reason, String lang) {
        repository.suspendContract(token, msisdn, reason, lang, suspendContractLiveData);
    }

    public void sendOTP(String token, String msisdn, String lang) {
        repository.sendOTP(token, msisdn, lang, sendOTPLiveData);
    }

    public void endContract(String token, String msisdn, String reason, String code, String lang) {
        repository.endContract(token, msisdn, reason, code, lang, endContractLiveData);
    }

    public void changeSIM(String token, String msisdn, String lang) {
        repository.changeSIM(token, msisdn, lang, changeSIMLiveData);
    }

    public void resendPUK(String token, String msisdn, String lang) {
        repository.resendPUK(token, msisdn, lang, resendPUKLiveData);
    }
}