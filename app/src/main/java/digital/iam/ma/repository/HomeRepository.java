package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.linestatus.LineStatusData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.utilities.Resource;

public class HomeRepository {

    public void activateSIM(String token, String code, String lang, MutableLiveData<Resource<ResponseData>> mutableLiveData) {
        new ApiManager().activateSIM(token, code, lang, mutableLiveData);
    }

    public void getMyConsumption(String token, String lang, MutableLiveData<Resource<MyConsumptionData>> mutableLiveData) {
        new ApiManager().getMyConsumption(token, lang, mutableLiveData);
    }

    public void getOrders(String token, String lang, MutableLiveData<Resource<GetOrdersData>> mutableLiveData) {
        new ApiManager().getOrders(token, lang, mutableLiveData);
    }

    public void getLineStatus(String token, String lang, MutableLiveData<Resource<LineStatusData>> mutableLiveData) {
        new ApiManager().getLineStatus(token, lang, mutableLiveData);
    }
}
