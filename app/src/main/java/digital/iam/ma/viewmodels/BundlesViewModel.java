package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.repository.BundlesRepository;
import digital.iam.ma.utilities.Resource;

public class BundlesViewModel extends AndroidViewModel {

    private final BundlesRepository repository;
    private final MutableLiveData<Resource<BundlesData>> getBundlesLiveData;
    private final MutableLiveData<Resource<CMIPaymentData>> switchBundleLiveData;

    public BundlesViewModel(@NonNull Application application) {
        super(application);

        this.repository = new BundlesRepository();
        getBundlesLiveData = new MutableLiveData<>();
        switchBundleLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<BundlesData>> getGetBundlesLiveData() {
        return getBundlesLiveData;
    }

    public MutableLiveData<Resource<CMIPaymentData>> getSwitchBundleLiveData() {
        return switchBundleLiveData;
    }

    public void getBundles(String lang) {
        repository.getBundles(lang, getBundlesLiveData);
    }


    public void switchBundle(String token, String msisdn, String sku, String lang) {
        repository.switchBundle(token, msisdn, sku, lang, switchBundleLiveData);
    }
}
