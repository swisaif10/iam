package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.cart.get.GetItemsData;
import digital.iam.ma.repository.CartRepository;
import digital.iam.ma.utilities.Resource;

public class CartViewModel extends AndroidViewModel {

    private final CartRepository repository;
    private final MutableLiveData<Resource<GetItemsData>> getItemsLiveData;

    public CartViewModel(@NonNull Application application) {
        super(application);

        this.repository = new CartRepository();
        getItemsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<GetItemsData>> getGetItemsLiveData() {
        return getItemsLiveData;
    }

    public void getItems(String token, String lang) {
        repository.getItems(token, lang, getItemsLiveData);
    }
}
