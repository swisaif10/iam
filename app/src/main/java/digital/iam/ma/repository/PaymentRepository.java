package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.help.HelpData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.utilities.Resource;

public class PaymentRepository {

    public void getOrders(String token, String lang, MutableLiveData<Resource<GetOrdersData>> mutableLiveData) {
        new ApiManager().getOrders(token, lang, mutableLiveData);
    }

    public void renewBundle(String token, String msisdn, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        new ApiManager().renewBundle(token, msisdn, lang, mutableLiveData);
    }

    public void payOrder(String token, String orderId, String msisdn, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        new ApiManager().payOrder(token, orderId, msisdn, lang, mutableLiveData);
    }

    public void getPaymentList(String lang, MutableLiveData<Resource<PaymentData>> mutableLiveData) {
        new ApiManager().getPaymentList(lang, mutableLiveData);
    }
}
