package digital.iam.ma.datamanager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import digital.iam.ma.datamanager.retrofit.RetrofitClient;
import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cart.add.AddItemData;
import digital.iam.ma.models.cart.get.GetItemsData;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.linestatus.LineStatusData;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.models.mybundle.MyBundleData;
import digital.iam.ma.models.orders.GetOrdersData;
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

    public void getMyBundle(String token, String lang, MutableLiveData<Resource<MyBundleData>> mutableLiveData) {
        Call<MyBundleData> call = RetrofitClient.getInstance().endpoint().getMyBundle(token, lang);
        call.enqueue(new Callback<MyBundleData>() {
            @Override
            public void onResponse(@NonNull Call<MyBundleData> call, @NonNull Response<MyBundleData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<MyBundleData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void logout(String token, String lang, MutableLiveData<Resource<LogoutData>> mutableLiveData) {
        Call<LogoutData> call = RetrofitClient.getInstance().endpoint().logout(token, lang);
        call.enqueue(new Callback<LogoutData>() {
            @Override
            public void onResponse(@NonNull Call<LogoutData> call, @NonNull Response<LogoutData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<LogoutData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getMyConsumption(String token, String lang, MutableLiveData<Resource<MyConsumptionData>> mutableLiveData) {
        Call<MyConsumptionData> call = RetrofitClient.getInstance().endpoint().getMyConsumption(token, lang);
        call.enqueue(new Callback<MyConsumptionData>() {
            @Override
            public void onResponse(@NonNull Call<MyConsumptionData> call, @NonNull Response<MyConsumptionData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<MyConsumptionData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getOrders(String token, String lang, MutableLiveData<Resource<GetOrdersData>> mutableLiveData) {
        Call<GetOrdersData> call = RetrofitClient.getInstance().endpoint().getOrders(token, lang);
        call.enqueue(new Callback<GetOrdersData>() {
            @Override
            public void onResponse(@NonNull Call<GetOrdersData> call, @NonNull Response<GetOrdersData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<GetOrdersData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getLineStatus(String token, String lang, MutableLiveData<Resource<LineStatusData>> mutableLiveData) {
        Call<LineStatusData> call = RetrofitClient.getInstance().endpoint().getLineStatus(token, lang);
        call.enqueue(new Callback<LineStatusData>() {
            @Override
            public void onResponse(@NonNull Call<LineStatusData> call, @NonNull Response<LineStatusData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<LineStatusData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getItems(String token, String lang, MutableLiveData<Resource<GetItemsData>> mutableLiveData) {
        Call<GetItemsData> call = RetrofitClient.getInstance().endpoint().getItems(token, lang);
        call.enqueue(new Callback<GetItemsData>() {
            @Override
            public void onResponse(@NonNull Call<GetItemsData> call, @NonNull Response<GetItemsData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<GetItemsData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    //**********************************************************************************************

    public void getBundles(String lang, MutableLiveData<Resource<BundlesData>> mutableLiveData) {
        Call<BundlesData> call = RetrofitClient.getInstance().endpoint().getBundles(lang);
        call.enqueue(new Callback<BundlesData>() {
            @Override
            public void onResponse(@NonNull Call<BundlesData> call, @NonNull Response<BundlesData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<BundlesData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void addItem(String token, String sku, String lang, MutableLiveData<Resource<AddItemData>> mutableLiveData) {
        Call<AddItemData> call = RetrofitClient.getInstance().endpoint().addItem(token, sku, lang);
        call.enqueue(new Callback<AddItemData>() {
            @Override
            public void onResponse(@NonNull Call<AddItemData> call, @NonNull Response<AddItemData> response) {
                assert response.body() != null;
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<AddItemData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }
}
