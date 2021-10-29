package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.recharge.RechargePurchase;
import digital.iam.ma.models.services.get.ServicesListData;
import digital.iam.ma.repository.RechargeRepository;
import digital.iam.ma.utilities.Resource;

public class RechargeViewModel extends AndroidViewModel {
    private final RechargeRepository repository;
    private final MutableLiveData<Resource<RechargePurchase>> rechargePurchase;

    public RechargeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new RechargeRepository();
        rechargePurchase = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<RechargePurchase>> getRechargePurchase() {
        return rechargePurchase;
    }

    public void rechargePurchase(String token, String sku, String msisdn, String _locale) {
        repository.rechargePurchase(token, sku, msisdn, _locale, rechargePurchase);
    }
}
