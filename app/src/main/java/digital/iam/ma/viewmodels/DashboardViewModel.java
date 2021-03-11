package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.repository.DashboardRepository;
import digital.iam.ma.utilities.Resource;

public class DashboardViewModel extends AndroidViewModel {

    private final DashboardRepository repository;
    private final MutableLiveData<Resource<LogoutData>> logoutLiveData;

    public DashboardViewModel(@NonNull Application application) {
        super(application);

        this.repository = new DashboardRepository();
        logoutLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<LogoutData>> getLogoutLiveData() {
        return logoutLiveData;
    }

    public void logout(String token, String lang) {
        repository.logout(token, lang, logoutLiveData);
    }
}
