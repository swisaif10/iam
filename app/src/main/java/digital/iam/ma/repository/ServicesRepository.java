package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.models.services.get.ServicesListData;
import digital.iam.ma.models.services.update.UpdateServicesData;
import digital.iam.ma.utilities.Resource;

public class ServicesRepository {

    public void getServices(String token, String msisdn, String lang, MutableLiveData<Resource<ServicesListData>> mutableLiveData) {
        new ApiManager().getServices(token, msisdn, lang, mutableLiveData);
    }

    public void updateServices(UpdateServicesData updateServicesData, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        new ApiManager().updateServices(updateServicesData, lang, mutableLiveData);
    }
}
