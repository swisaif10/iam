package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cart.add.AddItemData;
import digital.iam.ma.models.mybundle.MyBundleData;
import digital.iam.ma.models.services.ServicesData;
import digital.iam.ma.utilities.Resource;

public class BundlesRepository {

    public void getMyBundle(String token, String lang, MutableLiveData<Resource<MyBundleData>> mutableLiveData) {
        new ApiManager().getMyBundle(token, lang, mutableLiveData);
    }

    public void getBundles(String lang, MutableLiveData<Resource<BundlesData>> mutableLiveData) {
        new ApiManager().getBundles(lang, mutableLiveData);
    }

    public void addItem(String token, String sku, String lang, MutableLiveData<Resource<AddItemData>> mutableLiveData) {
        new ApiManager().addItem(token, sku, lang, mutableLiveData);
    }
}
