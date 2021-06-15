package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.help.HelpData;
import digital.iam.ma.models.profile.UpdateProfileData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Resource;

public class AccountRepository {

    public void updatePassword(String token, String currentPassword, String newPassword, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        new ApiManager().updatePassword(token, currentPassword, newPassword, lang, mutableLiveData);
    }

    public void getHelp(MutableLiveData<Resource<HelpData>> mutableLiveData) {
        new ApiManager().getHelp(mutableLiveData);
    }

    public void updateProfile(String token, String firstname, String lastname, String phoneNumber, String address, String city, String postcode, String lang, MutableLiveData<Resource<UpdateProfileData>> mutableLiveData) {
        new ApiManager().updateProfile(token, firstname, lastname, phoneNumber, address, city, postcode, lang, mutableLiveData);
    }
}
