package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cart.add.AddItemData;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.utilities.Resource;

public class BundlesRepository {


    public void getBundles(String lang, MutableLiveData<Resource<BundlesData>> mutableLiveData) {
        new ApiManager().getBundles(lang, mutableLiveData);
    }

    public void addItem(String token, String sku, String lang, MutableLiveData<Resource<AddItemData>> mutableLiveData) {
        new ApiManager().addItem(token, sku, lang, mutableLiveData);
    }

    public void switchBundle(String token, String msisdn, String sku, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        new ApiManager().switchBundle(token, msisdn, sku, lang, mutableLiveData);
    }

    public void getPaymentList(String lang, MutableLiveData<Resource<PaymentData>> mutableLiveData) {
        new ApiManager().getPaymentList(lang, mutableLiveData);
    }
    public void renewBundle(String token, String msisdn, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        new ApiManager().renewBundle(token, msisdn, lang, mutableLiveData);
    }
}
