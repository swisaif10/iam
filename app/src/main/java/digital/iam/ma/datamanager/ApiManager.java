package digital.iam.ma.datamanager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import digital.iam.ma.datamanager.retrofit.RetrofitClient;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.services.ServicesData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import digital.iam.ma.utilities.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {

    private void HandleThrowableException(Throwable e, MutableLiveData mutableLiveData) {
        if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException) {
            mutableLiveData.setValue(Resource.error("Connexion réseau indisponible. Assurez-vous que votre connexion réseau est active et réessayez.", null));
        } else {
            mutableLiveData.setValue(Resource.error(e.getMessage(), null));
        }
    }

    public void resetPassword(String email, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        Call<UpdatePasswordData> call = RetrofitClient.getInstance().endpoint().resetPassword(email, lang);
        call.enqueue(new Callback<UpdatePasswordData>() {
            @Override
            public void onResponse(@NonNull Call<UpdatePasswordData> call, @NonNull Response<UpdatePasswordData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<UpdatePasswordData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void addNewPassword(String email, String token, String newPassword, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        Call<UpdatePasswordData> call = RetrofitClient.getInstance().endpoint().addNewPassword(email, token, newPassword, lang);
        call.enqueue(new Callback<UpdatePasswordData>() {
            @Override
            public void onResponse(@NonNull Call<UpdatePasswordData> call, @NonNull Response<UpdatePasswordData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<UpdatePasswordData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void login(String username, String password, Boolean rememberMe, String lang, MutableLiveData<Resource<LoginData>> mutableLiveData) {
        Call<LoginData> call = RetrofitClient.getInstance().endpoint().login(username, password, rememberMe, lang);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(@NonNull Call<LoginData> call, @NonNull Response<LoginData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<LoginData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void activateSIM(String token, String code, String lang, MutableLiveData<Resource<ResponseData>> mutableLiveData) {
        Call<ResponseData> call = RetrofitClient.getInstance().endpoint().activateSIM(token, code, lang);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<ResponseData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void updatePassword(String token, String currentPassword, String newPassword, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        Call<UpdatePasswordData> call = RetrofitClient.getInstance().endpoint().updatePassword(token, currentPassword, newPassword, lang);
        call.enqueue(new Callback<UpdatePasswordData>() {
            @Override
            public void onResponse(@NonNull Call<UpdatePasswordData> call, @NonNull Response<UpdatePasswordData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<UpdatePasswordData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getServices(String token, String lang, MutableLiveData<Resource<ServicesData>> mutableLiveData) {
        Call<ServicesData> call = RetrofitClient.getInstance().endpoint().getServices(token, lang);
        call.enqueue(new Callback<ServicesData>() {
            @Override
            public void onResponse(@NonNull Call<ServicesData> call, @NonNull Response<ServicesData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<ServicesData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

}
