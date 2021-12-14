package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.fatourati.FatouratiResponse;
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

    public void getFatourati(String total_amount, String order_id, String client_email, String client_name, String client_tel, String client_id, String msisdn, String payment_type, String cart_id, String client_address, String token, String _locale, MutableLiveData<Resource<FatouratiResponse>> mutableLiveData) {
        new ApiManager().getFatourati(total_amount, order_id, client_email, client_name, client_tel, client_id, msisdn, payment_type, cart_id, client_address, token, _locale, mutableLiveData);
    }


}
