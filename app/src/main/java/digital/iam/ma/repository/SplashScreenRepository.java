package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.controlversion.ControlVersionData;
import digital.iam.ma.utilities.Resource;

public class SplashScreenRepository {

    public void controlVersion(String lang, MutableLiveData<Resource<ControlVersionData>> mutableLiveData) {
        new ApiManager().controlVersion(lang, mutableLiveData);
    }
}
