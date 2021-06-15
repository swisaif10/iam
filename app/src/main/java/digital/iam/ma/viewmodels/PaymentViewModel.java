package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.repository.PaymentRepository;
import digital.iam.ma.utilities.Resource;

public class PaymentViewModel extends AndroidViewModel {

    private final PaymentRepository repository;
    private final MutableLiveData<Resource<GetOrdersData>> getOrdersLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> renewBundleLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> orderPaymentLiveData;

    public PaymentViewModel(@NonNull Application application) {
        super(application);

        this.repository = new PaymentRepository();
        getOrdersLiveData = new MutableLiveData<>();
        renewBundleLiveData = new MutableLiveData<>();
        orderPaymentLiveData = new MutableLiveData<>();
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

    public void getOrders(String token, String lang) {
        repository.getOrders(token, lang, getOrdersLiveData);
    }

    public void renewBundle(String token, String msisdn, String lang) {
        repository.renewBundle(token, msisdn, lang, renewBundleLiveData);
    }

    public void payOrder(String token, String orderId, String msisdn, String lang) {
        repository.payOrder(token, orderId, msisdn, lang, orderPaymentLiveData);
    }
}
