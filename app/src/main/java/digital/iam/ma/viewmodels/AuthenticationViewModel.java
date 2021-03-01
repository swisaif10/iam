package digital.iam.ma.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.repository.AuthenticationRepository;
import digital.iam.ma.utilities.Resource;

public class AuthenticationViewModel extends AndroidViewModel {

    private final AuthenticationRepository repository;
    private final MutableLiveData<Resource<LoginData>> loginLiveData;
    private final MutableLiveData<Resource<UpdatePasswordData>> resetPasswordLiveData;
    private final MutableLiveData<Resource<UpdatePasswordData>> addNewPasswordLiveData;

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AuthenticationRepository();
        loginLiveData = new MutableLiveData<>();
        resetPasswordLiveData = new MutableLiveData<>();
        addNewPasswordLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<LoginData>> getLoginLiveData() {
        return loginLiveData;
    }

    public MutableLiveData<Resource<UpdatePasswordData>> getResetPasswordLiveData() {
        return resetPasswordLiveData;
    }

    public MutableLiveData<Resource<UpdatePasswordData>> getAddNewPasswordLiveData() {
        return addNewPasswordLiveData;
    }

    public void login(String username, String password, Boolean rememberMe, String lang) {
        repository.login(username, password, rememberMe, lang, loginLiveData);
    }

    public void resetPassword(String email, String lang) {
        repository.resetPassword(email, lang, resetPasswordLiveData);
    }

    public void addNewPassword(String email, String token, String newPassword, String lang) {
        repository.addNewPassword(email, token, newPassword, lang, addNewPasswordLiveData);
    }
}
