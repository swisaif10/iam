package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.linestatus.LineStatusData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.repository.HomeRepository;
import digital.iam.ma.utilities.Resource;

public class HomeViewModel extends AndroidViewModel {

    private final HomeRepository repository;
    private final MutableLiveData<Resource<ResponseData>> activateSIMLiveData;
    private final MutableLiveData<Resource<MyConsumptionData>> myConsumptionLiveData;
    private final MutableLiveData<Resource<GetOrdersData>> getOrdersLiveData;
    private final MutableLiveData<Resource<LineStatusData>> lineStatusLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        this.repository = new HomeRepository();
        activateSIMLiveData = new MutableLiveData<>();
        myConsumptionLiveData = new MutableLiveData<>();
        getOrdersLiveData = new MutableLiveData<>();
        lineStatusLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<ResponseData>> getActivateSIMLiveData() {
        return activateSIMLiveData;
    }

    public MutableLiveData<Resource<MyConsumptionData>> getMyConsumptionLiveData() {
        return myConsumptionLiveData;
    }

    public MutableLiveData<Resource<GetOrdersData>> getGetOrdersLiveData() {
        return getOrdersLiveData;
    }

    public MutableLiveData<Resource<LineStatusData>> getLineStatusLiveData() {
        return lineStatusLiveData;
    }

    public void activateSIM(String token, String code, String lang) {
        repository.activateSIM(token, code, lang, activateSIMLiveData);
    }

    public void getMyConsumption(String token, String lang) {
        repository.getMyConsumption(token, lang, myConsumptionLiveData);
    }

    public void getOrders(String token, String lang) {
        repository.getOrders(token, lang, getOrdersLiveData);
    }

    public void getLineStatus(String token, String lang) {
        repository.getLineStatus(token, lang, lineStatusLiveData);
    }
}
