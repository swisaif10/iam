package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.utilities.Resource;

public class ContractRepository {

    public void suspendContract(String token, String msisdn, String reason, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        new ApiManager().suspendContract(token, msisdn, reason, lang, mutableLiveData);
    }

    public void sendOTP(String token, String msisdn, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        new ApiManager().sendOTP(token, msisdn, lang, mutableLiveData);
    }

    public void endContract(String token, String msisdn, String reason, String code, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        new ApiManager().endContract(token, msisdn, reason, code, lang, mutableLiveData);
    }

    public void changeSIM(String token, String msisdn, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        new ApiManager().changeSIM(token, msisdn, lang, mutableLiveData);
    }

    public void resendPUK(String token, String msisdn, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        new ApiManager().resendPUK(token, msisdn, lang, mutableLiveData);
    }
}