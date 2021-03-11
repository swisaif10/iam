package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.repository.HomeRepository;
import digital.iam.ma.utilities.Resource;

public class HomeViewModel extends AndroidViewModel {

    private final HomeRepository repository;
    private final MutableLiveData<Resource<ResponseData>> activateSIMLiveData;
    private final MutableLiveData<Resource<MyConsumptionData>> myConsumptionLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        this.repository = new HomeRepository();
        activateSIMLiveData = new MutableLiveData<>();
        myConsumptionLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<ResponseData>> getActivateSIMLiveData() {
        return activateSIMLiveData;
    }

    public MutableLiveData<Resource<MyConsumptionData>> getMyConsumptionLiveData() {
        return myConsumptionLiveData;
    }

    public void activateSIM(String token, String code, String lang) {
        repository.activateSIM(token, code, lang, activateSIMLiveData);
    }

    public void getMyConsumption(String token, String lang) {
        repository.getMyConsumption(token, lang, myConsumptionLiveData);
    }
}
