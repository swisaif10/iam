package digital.iam.ma.repository;

import androidx.lifecycle.MutableLiveData;

import digital.iam.ma.datamanager.ApiManager;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Resource;

public class AuthenticationRepository {

    public void resetPassword(String email, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        new ApiManager().resetPassword(email, lang, mutableLiveData);
    }

    public void addNewPassword(String email, String token, String newPassword, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        new ApiManager().addNewPassword(email, token, newPassword, lang, mutableLiveData);
    }

    public void login(String username, String password, Boolean rememberMe, String lang, MutableLiveData<Resource<LoginData>> mutableLiveData) {

/*        new ApiManager().login(username, password, rememberMe, lang, new onDataLoaded<ResponseData>() {
            @Override
            public void onResponse(ResponseData data) {
                mutableLiveData.setValue(Resource.success(data));
            }

            @Override
            public void onFailure(String error) {
                mutableLiveData.setValue(Resource.error(error, null));
            }

            @Override
            public void onInvalidToken() {

            }
        });*/

        new ApiManager().login(username, password, rememberMe, lang, mutableLiveData);
    }
}
