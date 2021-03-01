package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.utilities.Resource;

public class HomeRepository {

    public void activateSIM(String token, String code, String lang, MutableLiveData<Resource<ResponseData>> mutableLiveData) {
        new ApiManager().activateSIM(token, code, lang, mutableLiveData);
    }
}
