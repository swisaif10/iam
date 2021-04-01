package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.cart.get.GetItemsData;
import digital.iam.ma.utilities.Resource;

public class CartRepository {

    public void getItems(String token, String lang, MutableLiveData<Resource<GetItemsData>> mutableLiveData) {
        new ApiManager().getItems(token, lang, mutableLiveData);
    }
}
