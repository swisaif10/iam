package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.repository.BundlesRepository;
import digital.iam.ma.utilities.Resource;

public class BundlesViewModel extends AndroidViewModel {

    private final BundlesRepository repository;
    private final MutableLiveData<Resource<BundlesData>> getBundlesLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> switchBundleLiveData;
    private final MutableLiveData<Resource<PaymentData>> paymentListLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> renewBundleLiveData;

    public BundlesViewModel(@NonNull Application application) {
        super(application);

        this.repository = new BundlesRepository();
        getBundlesLiveData = new MutableLiveData<>();
        switchBundleLiveData = new MutableLiveData<>();
        paymentListLiveData = new MutableLiveData<>();
        renewBundleLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<BundlesData>> getGetBundlesLiveData() {
        return getBundlesLiveData;
    }

    public MutableLiveData<Resource<CMIPaymentData>> getSwitchBundleLiveData() {
        return switchBundleLiveData;
    }

    public MutableLiveData<Resource<PaymentData>> getPaymentListLiveData() {
        return paymentListLiveData;
    }

    public MutableLiveData<Resource<CMIPaymentData>> getRenewBundleLiveData() {
        return renewBundleLiveData;
    }

    public void getBundles(String lang) {
        repository.getBundles(lang, getBundlesLiveData);
    }


    public void switchBundle(String token, String msisdn, String sku, String lang) {
        repository.switchBundle(token, msisdn, sku, lang, switchBundleLiveData);
    }

    public void getPaymentList(String lang) {
        repository.getPaymentList(lang, paymentListLiveData);
    }

    public void renewBundle(String token, String msisdn, String lang) {
        repository.renewBundle(token, msisdn, lang, renewBundleLiveData);
    }
}
