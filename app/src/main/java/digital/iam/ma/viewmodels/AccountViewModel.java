package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.help.HelpData;
import digital.iam.ma.models.profile.UpdateProfileData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.repository.AccountRepository;
import digital.iam.ma.utilities.Resource;

public class AccountViewModel extends AndroidViewModel {

    private final AccountRepository repository;
    private final MutableLiveData<Resource<UpdatePasswordData>> updatePasswordLiveData;
    private final MutableLiveData<Resource<HelpData>> helpLiveData;
    private final MutableLiveData<Resource<UpdateProfileData>> updateProfileLiveData;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        this.repository = new AccountRepository();
        updatePasswordLiveData = new MutableLiveData<>();
        helpLiveData = new MutableLiveData<>();
        updateProfileLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<UpdatePasswordData>> getUpdatePasswordLiveData() {
        return updatePasswordLiveData;
    }

    public MutableLiveData<Resource<HelpData>> getHelpLiveData() {
        return helpLiveData;
    }

    public MutableLiveData<Resource<UpdateProfileData>> getUpdateProfileLiveData() {
        return updateProfileLiveData;
    }

    public void updatePassword(String token, String currentPassword, String newPassword, String lang) {
        repository.updatePassword(token, currentPassword, newPassword, lang, updatePasswordLiveData);
    }

    public void getHelp() {
        repository.getHelp(helpLiveData);
    }

    public void updateProfile(String token, String firstname, String lastname, String phoneNumber, String address, String city, String postcode, int gender, String lang) {
        repository.updateProfile(token, firstname, lastname, phoneNumber, address, city, postcode, gender, lang, updateProfileLiveData);
    }
}
