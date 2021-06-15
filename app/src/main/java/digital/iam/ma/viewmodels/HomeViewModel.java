package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.repository.HomeRepository;
import digital.iam.ma.utilities.Resource;

public class HomeViewModel extends AndroidViewModel {

    private final HomeRepository repository;
    private final MutableLiveData<Resource<ResponseData>> activateSIMLiveData;
    private final MutableLiveData<Resource<MyConsumptionData>> myConsumptionLiveData;
    private final MutableLiveData<Resource<GetOrdersData>> getOrdersLiveData;
    private final MutableLiveData<Resource<RechargeListData>> rechargeListLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> renewBundleLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        this.repository = new HomeRepository();
        activateSIMLiveData = new MutableLiveData<>();
        myConsumptionLiveData = new MutableLiveData<>();
        getOrdersLiveData = new MutableLiveData<>();
        rechargeListLiveData = new MutableLiveData<>();
        renewBundleLiveData = new MutableLiveData<>();
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

    public MutableLiveData<Resource<RechargeListData>> getRechargeListLiveData() {
        return rechargeListLiveData;
    }

    public MutableLiveData<Resource<CMIPaymentData>> getRenewBundleLiveData() {
        return renewBundleLiveData;
    }

    public void activateSIM(String token, String msisdn, String code, String lang) {
        repository.activateSIM(token, msisdn, code, lang, activateSIMLiveData);
    }

    public void getMyConsumption(String token, String msisdn, String lang) {
        repository.getMyConsumption(token, msisdn, lang, myConsumptionLiveData);
    }

    public void getOrders(String token, String lang) {
        repository.getOrders(token, lang, getOrdersLiveData);
    }

    public void getRechargesList(String lang) {
        repository.getRechargesList(lang, rechargeListLiveData);
    }

    public void renewBundle(String token, String msisdn, String lang) {
        repository.renewBundle(token, msisdn, lang, renewBundleLiveData);
    }
}
