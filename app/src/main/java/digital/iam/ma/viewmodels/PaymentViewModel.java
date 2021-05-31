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

    public PaymentViewModel(@NonNull Application application) {
        super(application);

        this.repository = new PaymentRepository();
        getOrdersLiveData = new MutableLiveData<>();
        renewBundleLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<GetOrdersData>> getGetOrdersLiveData() {
        return getOrdersLiveData;
    }

    public MutableLiveData<Resource<CMIPaymentData>> getRenewBundleLiveData() {
        return renewBundleLiveData;
    }

    public void getOrders(String token, String lang) {
        repository.getOrders(token, lang, getOrdersLiveData);
    }

    public void renewBundle(String token, String msisdn, String lang) {
        repository.renewBundle(token, msisdn, lang, renewBundleLiveData);
    }
}
