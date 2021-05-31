package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.controlversion.ControlVersionData;
import digital.iam.ma.repository.SplashScreenRepository;
import digital.iam.ma.utilities.Resource;

public class SplashScreenViewModel extends AndroidViewModel {

    private final SplashScreenRepository repository;
    private final MutableLiveData<Resource<ControlVersionData>> controlVersionLiveData;

    public SplashScreenViewModel(@NonNull Application application) {
        super(application);

        this.repository = new SplashScreenRepository();
        controlVersionLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<ControlVersionData>> getControlVersionLiveData() {
        return controlVersionLiveData;
    }

    public void controlVersion(String lang) {
        repository.controlVersion(lang, controlVersionLiveData);
    }
}
