package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.services.ServicesData;
import digital.iam.ma.utilities.Resource;

public class ServicesRepository {

    public void getServices(String token, String lang, MutableLiveData<Resource<ServicesData>> mutableLiveData) {
        new ApiManager().getServices(token, lang, mutableLiveData);
    }
}
