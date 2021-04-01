package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.utilities.Resource;

public class DashboardRepository {

    public void logout(String token, String lang, MutableLiveData<Resource<LogoutData>> mutableLiveData) {
        new ApiManager().logout(token, lang, mutableLiveData);
    }
}
