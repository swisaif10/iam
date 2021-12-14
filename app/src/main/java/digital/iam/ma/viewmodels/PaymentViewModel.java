package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.fatourati.FatouratiResponse;
import digital.iam.ma.models.help.HelpData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.repository.PaymentRepository;
import digital.iam.ma.utilities.Resource;

public class PaymentViewModel extends AndroidViewModel {

    private final PaymentRepository repository;
    private final MutableLiveData<Resource<GetOrdersData>> getOrdersLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> renewBundleLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> orderPaymentLiveData;
    private final MutableLiveData<Resource<PaymentData>> paymentListLiveData;
    private final MutableLiveData<Resource<FatouratiResponse>> fatouratiLiveData;


    public PaymentViewModel(@NonNull Application application) {
        super(application);

        this.repository = new PaymentRepository();
        getOrdersLiveData = new MutableLiveData<>();
        renewBundleLiveData = new MutableLiveData<>();
        orderPaymentLiveData = new MutableLiveData<>();
        paymentListLiveData = new MutableLiveData<>();
        fatouratiLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<GetOrdersData>> getGetOrdersLiveData() {
        return getOrdersLiveData;
    }

    public MutableLiveData<Resource<CMIPaymentData>> getRenewBundleLiveData() {
        return renewBundleLiveData;
    }

    public MutableLiveData<Resource<CMIPaymentData>> getOrderPaymentLiveData() {
        return orderPaymentLiveData;
    }

    public MutableLiveData<Resource<PaymentData>> getPaymentListLiveData() {
        return paymentListLiveData;
    }

    public MutableLiveData<Resource<FatouratiResponse>> getFatouratiLiveData() {
        return fatouratiLiveData;
    }

    public void getOrders(String token, String lang) {
        repository.getOrders(token, lang, getOrdersLiveData);
    }

    public void renewBundle(String token, String msisdn, String lang) {
        repository.renewBundle(token, msisdn, lang, renewBundleLiveData);
    }

    public void payOrder(String token, String orderId, String msisdn, String lang) {
        repository.payOrder(token, orderId, msisdn, lang, orderPaymentLiveData);
    }

    public void getPaymentList(String lang) {
        repository.getPaymentList(lang, paymentListLiveData);
    }

    public void getFatourati(String total_amount, String order_id, String client_email, String client_name, String client_tel, String client_id, String msisdn, String payment_type, String cart_id, String client_address, String token, String _locale) {
        repository.getFatourati(total_amount, order_id, client_email, client_name, client_tel, client_id, msisdn, payment_type, cart_id, client_address, token, _locale, fatouratiLiveData);
    }

}
