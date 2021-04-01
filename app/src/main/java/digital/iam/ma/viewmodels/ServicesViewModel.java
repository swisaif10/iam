package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.services.ServicesData;
import digital.iam.ma.repository.ServicesRepository;
import digital.iam.ma.utilities.Resource;

public class ServicesViewModel extends AndroidViewModel {

    private final ServicesRepository repository;
    private final MutableLiveData<Resource<ServicesData>> servicesLiveData;

    public ServicesViewModel(@NonNull Application application) {
        super(application);

        this.repository = new ServicesRepository();
        servicesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<ServicesData>> getServicesLiveData() {
        return servicesLiveData;
    }

    public void getServices(String token, String lang) {
        repository.getServices(token, lang, servicesLiveData);
    }
}
