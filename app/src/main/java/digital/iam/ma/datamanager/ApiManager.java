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
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.models.controlversion.ControlVersionData;
import digital.iam.ma.models.help.HelpData;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.profile.UpdateProfileData;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.models.recharge.RechargePurchase;
import digital.iam.ma.models.services.get.ServicesListData;
import digital.iam.ma.models.services.update.UpdateServicesData;
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

    public void controlVersion(String lang, MutableLiveData<Resource<ControlVersionData>> mutableLiveData) {
        Call<ControlVersionData> call = RetrofitClient.getInstance().endpoint().controlVersion(lang);
        call.enqueue(new Callback<ControlVersionData>() {
            @Override
            public void onResponse(@NonNull Call<ControlVersionData> call, @NonNull Response<ControlVersionData> response) {
                if (response.body() != null && response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<ControlVersionData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void resetPassword(String email, String lang, MutableLiveData<Resource<UpdatePasswordData>> mutableLiveData) {
        Call<UpdatePasswordData> call = RetrofitClient.getInstance().endpoint().resetPassword(email, lang);
        call.enqueue(new Callback<UpdatePasswordData>() {
            @Override
            public void onResponse(@NonNull Call<UpdatePasswordData> call, @NonNull Response<UpdatePasswordData> response) {
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

    public void activateSIM(String token, String msisdn, String code, String lang, MutableLiveData<Resource<ResponseData>> mutableLiveData) {
        Call<ResponseData> call = RetrofitClient.getInstance().endpoint().activateSIM(token, msisdn, code, lang);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
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
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<UpdatePasswordData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getServices(String token, String msisdn, String lang, MutableLiveData<Resource<ServicesListData>> mutableLiveData) {
        Call<ServicesListData> call = RetrofitClient.getInstance().endpoint().getServices(token, msisdn, lang);
        call.enqueue(new Callback<ServicesListData>() {
            @Override
            public void onResponse(@NonNull Call<ServicesListData> call, @NonNull Response<ServicesListData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<ServicesListData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void logout(String token, String lang, MutableLiveData<Resource<LogoutData>> mutableLiveData) {
        Call<LogoutData> call = RetrofitClient.getInstance().endpoint().logout(token, lang);
        call.enqueue(new Callback<LogoutData>() {
            @Override
            public void onResponse(@NonNull Call<LogoutData> call, @NonNull Response<LogoutData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<LogoutData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getMyConsumption(String token, String msisdn, String lang, MutableLiveData<Resource<MyConsumptionData>> mutableLiveData) {
        Call<MyConsumptionData> call = RetrofitClient.getInstance().endpoint().getMyConsumption(token, msisdn, lang);
        call.enqueue(new Callback<MyConsumptionData>() {
            @Override
            public void onResponse(@NonNull Call<MyConsumptionData> call, @NonNull Response<MyConsumptionData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401 || response.body().getHeader().getCode() == 404)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
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
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<GetOrdersData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getItems(String token, String lang, MutableLiveData<Resource<GetItemsData>> mutableLiveData) {
        Call<GetItemsData> call = RetrofitClient.getInstance().endpoint().getItems(token, lang);
        call.enqueue(new Callback<GetItemsData>() {
            @Override
            public void onResponse(@NonNull Call<GetItemsData> call, @NonNull Response<GetItemsData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<GetItemsData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getBundles(String lang, MutableLiveData<Resource<BundlesData>> mutableLiveData) {
        Call<BundlesData> call = RetrofitClient.getInstance().endpoint().getBundles(lang);
        call.enqueue(new Callback<BundlesData>() {
            @Override
            public void onResponse(@NonNull Call<BundlesData> call, @NonNull Response<BundlesData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<BundlesData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getRechargesList(String lang, MutableLiveData<Resource<RechargeListData>> mutableLiveData) {
        Call<RechargeListData> call = RetrofitClient.getInstance().endpoint().getRechargesList(lang);
        call.enqueue(new Callback<RechargeListData>() {
            @Override
            public void onResponse(@NonNull Call<RechargeListData> call, @NonNull Response<RechargeListData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<RechargeListData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void renewBundle(String token, String msisdn, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        Call<CMIPaymentData> call = RetrofitClient.getInstance().endpoint().renewBundle(token, msisdn, lang);
        call.enqueue(new Callback<CMIPaymentData>() {
            @Override
            public void onResponse(@NonNull Call<CMIPaymentData> call, @NonNull Response<CMIPaymentData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<CMIPaymentData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void switchBundle(String token, String msisdn, String sku, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        Call<CMIPaymentData> call = RetrofitClient.getInstance().endpoint().switchBundle(token, msisdn, sku, lang);
        call.enqueue(new Callback<CMIPaymentData>() {
            @Override
            public void onResponse(@NonNull Call<CMIPaymentData> call, @NonNull Response<CMIPaymentData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<CMIPaymentData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void addItem(String token, String sku, String lang, MutableLiveData<Resource<AddItemData>> mutableLiveData) {
        Call<AddItemData> call = RetrofitClient.getInstance().endpoint().addItem(token, sku, lang);
        call.enqueue(new Callback<AddItemData>() {
            @Override
            public void onResponse(@NonNull Call<AddItemData> call, @NonNull Response<AddItemData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<AddItemData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void getHelp(MutableLiveData<Resource<HelpData>> mutableLiveData) {
        Call<HelpData> call = RetrofitClient.getInstance().endpoint().getHelpData();
        call.enqueue(new Callback<HelpData>() {
            @Override
            public void onResponse(@NonNull Call<HelpData> call, @NonNull Response<HelpData> response) {

                mutableLiveData.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<HelpData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void payOrder(String token, String orderId, String msisdn, String lang, MutableLiveData<Resource<CMIPaymentData>> mutableLiveData) {
        Call<CMIPaymentData> call = RetrofitClient.getInstance().endpoint().payOrder(token, orderId, msisdn, lang);
        call.enqueue(new Callback<CMIPaymentData>() {
            @Override
            public void onResponse(@NonNull Call<CMIPaymentData> call, @NonNull Response<CMIPaymentData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<CMIPaymentData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void updateProfile(String token, String firstname, String lastname, String phoneNumber, String address, String city, String postcode, int gender, String lang, MutableLiveData<Resource<UpdateProfileData>> mutableLiveData) {
        Call<UpdateProfileData> call = RetrofitClient.getInstance().endpoint().updateProfile(token, firstname, lastname, phoneNumber, address, city, postcode, gender, lang);
        call.enqueue(new Callback<UpdateProfileData>() {
            @Override
            public void onResponse(@NonNull Call<UpdateProfileData> call, @NonNull Response<UpdateProfileData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<UpdateProfileData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void suspendContract(String token, String msisdn, String reason, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        Call<SuspendContractData> call = RetrofitClient.getInstance().endpoint().suspendContract(token, msisdn, reason, lang);
        call.enqueue(new Callback<SuspendContractData>() {
            @Override
            public void onResponse(@NonNull Call<SuspendContractData> call, @NonNull Response<SuspendContractData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<SuspendContractData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void sendOTP(String token, String msisdn, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        Call<SuspendContractData> call = RetrofitClient.getInstance().endpoint().sendOTP(token, msisdn, lang);
        call.enqueue(new Callback<SuspendContractData>() {
            @Override
            public void onResponse(@NonNull Call<SuspendContractData> call, @NonNull Response<SuspendContractData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<SuspendContractData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void endContract(String token, String msisdn, String reason, String code, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        Call<SuspendContractData> call = RetrofitClient.getInstance().endpoint().endContract(token, msisdn, reason, code, lang);
        call.enqueue(new Callback<SuspendContractData>() {
            @Override
            public void onResponse(@NonNull Call<SuspendContractData> call, @NonNull Response<SuspendContractData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<SuspendContractData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void changeSIM(String token, String msisdn, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        Call<SuspendContractData> call = RetrofitClient.getInstance().endpoint().changeSIM(token, msisdn, lang);
        call.enqueue(new Callback<SuspendContractData>() {
            @Override
            public void onResponse(@NonNull Call<SuspendContractData> call, @NonNull Response<SuspendContractData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<SuspendContractData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void resendPUK(String token, String msisdn, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        Call<SuspendContractData> call = RetrofitClient.getInstance().endpoint().resendPUK(token, msisdn, lang);
        call.enqueue(new Callback<SuspendContractData>() {
            @Override
            public void onResponse(@NonNull Call<SuspendContractData> call, @NonNull Response<SuspendContractData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<SuspendContractData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void updateServices(UpdateServicesData updateServicesData, String lang, MutableLiveData<Resource<SuspendContractData>> mutableLiveData) {
        Call<SuspendContractData> call = RetrofitClient.getInstance().endpoint().updateServices(updateServicesData, lang);
        call.enqueue(new Callback<SuspendContractData>() {
            @Override
            public void onResponse(@NonNull Call<SuspendContractData> call, @NonNull Response<SuspendContractData> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));
                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(@NonNull Call<SuspendContractData> call, @NonNull Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }

    public void rechargePurchase(String token, String sku, String msisdn, String _locale, MutableLiveData<Resource<RechargePurchase>> mutableLiveData) {
        Call<RechargePurchase> call = RetrofitClient.getInstance().endpoint().purchaseRecharge(token, sku, msisdn, _locale);
        call.enqueue(new Callback<RechargePurchase>() {
            @Override
            public void onResponse(Call<RechargePurchase> call, Response<RechargePurchase> response) {
                if (response.body().getHeader().getCode() == 200)
                    mutableLiveData.setValue(Resource.success(response.body()));

                else if (response.body().getHeader().getCode() == 401)
                    mutableLiveData.setValue(Resource.invalidToken(response.body()));
                else
                    mutableLiveData.setValue(Resource.error(response.body().getHeader().getMessage(), null));
            }

            @Override
            public void onFailure(Call<RechargePurchase> call, Throwable t) {
                HandleThrowableException(t, mutableLiveData);
            }
        });
    }


}
