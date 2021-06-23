package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.models.services.get.ServicesListData;
import digital.iam.ma.models.services.update.UpdateServicesData;
import digital.iam.ma.repository.ServicesRepository;
import digital.iam.ma.utilities.Resource;

public class ServicesViewModel extends AndroidViewModel {

    private final ServicesRepository repository;
    private final MutableLiveData<Resource<ServicesListData>> servicesLiveData;
    private final MutableLiveData<Resource<SuspendContractData>> updateServicesLiveData;

    public ServicesViewModel(@NonNull Application application) {
        super(application);

        this.repository = new ServicesRepository();
        servicesLiveData = new MutableLiveData<>();
        updateServicesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<ServicesListData>> getServicesLiveData() {
        return servicesLiveData;
    }

    public MutableLiveData<Resource<SuspendContractData>> getUpdateServicesLiveData() {
        return updateServicesLiveData;
    }

    public void getServices(String token, String msisdn, String lang) {
        repository.getServices(token, msisdn, lang, servicesLiveData);
    }

    public void updateServices(UpdateServicesData updateServicesData, String lang) {
        repository.updateServices(updateServicesData, lang, updateServicesLiveData);
    }
}
