package digital.iam.ma.datamanager.retrofit;

import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cart.add.AddItemData;
import digital.iam.ma.models.cart.get.GetItemsData;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.controlversion.ControlVersionData;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.models.services.ServicesData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {

    @POST(ApiEndpoints.CONTROL_VERSION_URL)
    Call<ControlVersionData> controlVersion(@Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.RESET_PASSWORD_URL)
    Call<UpdatePasswordData> resetPassword(@Field("user_email") String userEmail,
                                           @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.ADD_NEW_PASSWORD_URL)
    Call<UpdatePasswordData> addNewPassword(@Field("user_email") String userEmail,
                                            @Field("reset_token") String resetToken,
                                            @Field("new_password") String newPassword,
                                            @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.LOGIN_URL)
    Call<LoginData> login(@Field("username") String username,
                          @Field("password") String password,
                          @Field("rememberMe") Boolean rememberMe,
                          @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.SIM_ACTIVATION_URL)
    Call<ResponseData> activateSIM(@Field("token") String token,
                                   @Field("activation_code") String activationCode,
                                   @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.UPDATE_PASSWORD_URL)
    Call<UpdatePasswordData> updatePassword(@Field("token") String token,
                                            @Field("current_pwd") String currentPassword,
                                            @Field("new_pwd") String newPassword,
                                            @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.GET_SERVICES_URL)
    Call<ServicesData> getServices(@Field("token") String token,
                                   @Field("msisdn") String msisdn,
                                   @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.LOGOUT_URL)
    Call<LogoutData> logout(@Field("token") String token,
                            @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.GET_MY_CONSUMPTION_URL)
    Call<MyConsumptionData> getMyConsumption(@Field("token") String token,
                                             @Field("msisdn") String msisdn,
                                             @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.GET_ORDERS_URL)
    Call<GetOrdersData> getOrders(@Field("token") String token,
                                  @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.GET_ITEMS_URL)
    Call<GetItemsData> getItems(@Field("token") String token,
                                @Path("locale") String lang);

    @POST(ApiEndpoints.GET_BUNDLES_URL)
    Call<BundlesData> getBundles(@Path("locale") String lang);

    @POST(ApiEndpoints.RECHARGE_LIST_URL)
    Call<RechargeListData> getRechargesList(@Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.RENEW_BUNDLE_URL)
    Call<CMIPaymentData> renewBundle(@Field("token") String token,
                                     @Field("msisdn") String msisdn,
                                     @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.SWITCH_BUNDLE_URL)
    Call<CMIPaymentData> switchBundle(@Field("token") String token,
                                      @Field("msisdn") String msisdn,
                                      @Field("sku") String sku,
                                      @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.ADD_ITEM_URL)
    Call<AddItemData> addItem(@Field("token") String token,
                              @Field("sku") String sku,
                              @Path("locale") String lang);

}
