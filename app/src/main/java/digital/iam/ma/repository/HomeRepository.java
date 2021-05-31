package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.utilities.Resource;

public class HomeRepository {

    public void activateSIM(String token, String code, String lang, MutableLiveData<Resource<ResponseData>> mutableLiveData) {
        new ApiManager().activateSIM(token, code, lang, mutableLiveData);
    }

    public void getMyConsumption(String token, String msisdn, String lang, MutableLiveData<Resource<MyConsumptionData>> mutableLiveData) {
        new ApiManager().getMyConsumption(token, msisdn, lang, mutableLiveData);
    }

    public void getOrders(String token, String lang, MutableLiveData<Resource<GetOrdersData>> mutableLiveData) {
        new ApiManager().getOrders(token, lang, mutableLiveData);
    }

    public void getRechargesList(String lang, MutableLiveData<Resource<RechargeListData>> mutableLiveData) {
        new ApiManager().getRechargesList(lang, mutableLiveData);
    }

    public void renewBundle(String token, String msisdn, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        new ApiManager().renewBundle(token, msisdn, lang, mutableLiveData);
    }
}
