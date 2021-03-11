package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cart.add.AddItemData;
import digital.iam.ma.models.mybundle.MyBundleData;
import digital.iam.ma.repository.BundlesRepository;
import digital.iam.ma.utilities.Resource;

public class BundlesViewModel extends AndroidViewModel {

    private final BundlesRepository repository;
    private final MutableLiveData<Resource<MyBundleData>> myBundleLiveData;
    private final MutableLiveData<Resource<BundlesData>> getBundlesLiveData;
    private final MutableLiveData<Resource<AddItemData>> addItemLiveData;

    public BundlesViewModel(@NonNull Application application) {
        super(application);

        this.repository = new BundlesRepository();
        myBundleLiveData = new MutableLiveData<>();
        getBundlesLiveData = new MutableLiveData<>();
        addItemLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<MyBundleData>> getMyBundleLiveData() {
        return myBundleLiveData;
    }

    public MutableLiveData<Resource<BundlesData>> getGetBundlesLiveData() {
        return getBundlesLiveData;
    }

    public MutableLiveData<Resource<AddItemData>> getAddItemLiveData() {
        return addItemLiveData;
    }

    public void getMyBundleDetails(String token, String lang) {
        repository.getMyBundle(token, lang, myBundleLiveData);
    }

    public void getBundles(String lang) {
        repository.getBundles(lang, getBundlesLiveData);
    }

    public void addItemToCart(String token, String sku, String lang) {
        repository.addItem(token, sku, lang, addItemLiveData);
    }
}
