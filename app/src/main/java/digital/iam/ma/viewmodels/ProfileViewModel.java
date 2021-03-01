package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.repository.ProfileRepository;
import digital.iam.ma.utilities.Resource;

public class ProfileViewModel extends AndroidViewModel {

    private final ProfileRepository repository;
    private final MutableLiveData<Resource<UpdatePasswordData>> updatePasswordLiveData;

    public ProfileViewModel(@NonNull Application application) {
        super(application);

        this.repository = new ProfileRepository();
        updatePasswordLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<UpdatePasswordData>> getUpdatePasswordLiveData() {
        return updatePasswordLiveData;
    }

    public void updatePassword(String token, String currentPassword, String newPassword, String lang) {
        repository.updatePassword(token, currentPassword, newPassword, lang, updatePasswordLiveData);
    }
}
