package digital.iam.ma.datamanager.retrofit;

import digital.iam.ma.models.bundles.BundlesData;
import digital.iam.ma.models.cart.add.AddItemData;
import digital.iam.ma.models.cart.get.GetItemsData;
import digital.iam.ma.models.cmi.CMIPaymentData;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.consumption.MyConsumptionData;
import digital.iam.ma.models.contract.Contract;
import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.models.controlversion.ControlVersionData;
import digital.iam.ma.models.fatourati.FatouratiResponse;
import digital.iam.ma.models.help.HelpData;
import digital.iam.ma.models.lines.Lines;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.models.orders.GetOrdersData;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.models.profile.UpdateProfileData;
import digital.iam.ma.models.recharge.RechargeListData;
import digital.iam.ma.models.recharge.RechargePurchase;
import digital.iam.ma.models.services.get.ServicesListData;
import digital.iam.ma.models.services.update.UpdateServicesData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
                                   @Field("msisdn") String msisdn,
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
    Call<ServicesListData> getServices(@Field("token") String token,
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

    @GET(ApiEndpoints.GET_HELP_URL)
    Call<HelpData> getHelpData(@Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.CMI_URL)
    Call<CMIPaymentData> payOrder(@Field("token") String token,
                                  @Field("order_id") String orderId,
                                  @Field("msisdn") String msisdn,
                                  @Path("locale") String lang);

    @FormUrlEncoded
    @PUT(ApiEndpoints.UPDATE_PROFILE_URL)
    Call<UpdateProfileData> updateProfile(@Field("token") String token,
                                          @Field("firstname") String firstname,
                                          @Field("lastname") String lastname,
                                          @Field("phone_number") String phoneNumber,
                                          @Field("address") String address,
                                          @Field("city") String city,
                                          @Field("shipping_postcode") String postcode,
                                          @Field("gender") int gender,
                                          @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.SUSPEND_CONTRACT_URL)
    Call<SuspendContractData> suspendContract(@Field("token") String token,
                                              @Field("msisdn") String msisdn,
                                              @Field("motif") String reason,
                                              @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.SEND_OTP_URL)
    Call<SuspendContractData> sendOTP(@Field("token") String token,
                                      @Field("msisdn") String msisdn,
                                      @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.END_CONTRACT_URL)
    Call<SuspendContractData> endContract(@Field("token") String token,
                                          @Field("msisdn") String msisdn,
                                          @Field("motif") String reason,
                                          @Field("code") String code,
                                          @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.CHANGE_SIM_URL)
    Call<SuspendContractData> changeSIM(@Field("token") String token,
                                        @Field("msisdn") String msisdn,
                                        @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.RESEND_PUK_URL)
    Call<SuspendContractData> resendPUK(@Field("token") String token,
                                        @Field("msisdn") String msisdn,
                                        @Path("locale") String lang);

    @POST(ApiEndpoints.UPDATE_SERVICES_URL)
    Call<SuspendContractData> updateServices(@Body UpdateServicesData updateServicesData,
                                             @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.RECHARGE_PURCHASE)
    Call<RechargePurchase> purchaseRecharge(
            @Field("token") String token,
            @Field("sku") String sku,
            @Field("msisdn") String msisdn,
            @Field("method_payment") String method_payment,
            @Path("locale") String _locale
    );

    @FormUrlEncoded
    @POST(ApiEndpoints.FATOURATY_URL)
    Call<FatouratiResponse> getFatouraty(
            @Field("total_amount") String total_amount,
            @Field("order_id") String order_id,
            @Field("client_email") String client_email,
            @Field("client_name") String client_name,
            @Field("client_tel") String client_tel,
            @Field("client_id") String client_id,
            @Field("msisdn") String msisdn,
            @Field("payment_type") String payment_type,
            @Field("cart_id") String cart_id,
            @Field("client_address") String client_address,
            @Field("token") String token,
            @Path("locale") String locale
    );

    @GET(ApiEndpoints.GET_PAYMENT_LIST_URL)
    Call<PaymentData> getPaymentListData(@Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiEndpoints.CONTRACT_URL)
    Call<Contract> changeContract(
            @Field("token") String token,
            @Field("msisdn") String msisdn,
            @Path("locale") String locale,
            @Path("status") String status
    );

    @FormUrlEncoded
    @POST(ApiEndpoints.LINES_URL)
    Call<Lines> getLines(
            @Field("token") String token,
            @Path("locale") String locale
    );
}
