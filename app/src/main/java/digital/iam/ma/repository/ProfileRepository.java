package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Resource;

public class ProfileRepository {

    public void updatePassword(String token, String currentPassword, String newPassword, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        new ApiManager().updatePassword(token, currentPassword, newPassword, lang, mutableLiveData);
    }
}
