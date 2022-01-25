package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.recharge.RechargePurchase;
import digital.iam.ma.utilities.Resource;

public class RechargeRepository {
    public void rechargePurchase(String token, String sku, String msisdn,String method_payment, String _locale, MutableLiveData<Resource<RechargePurchase>> mutableLiveData) {
        new ApiManager().rechargePurchase(token, sku, msisdn, method_payment, _locale, mutableLiveData);
    }
}
