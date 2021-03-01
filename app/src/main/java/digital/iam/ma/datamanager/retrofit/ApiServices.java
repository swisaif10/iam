package digital.iam.ma.datamanager.retrofit;

import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.models.login.LoginData;
import digital.iam.ma.models.services.ServicesData;
import digital.iam.ma.models.updatepassword.UpdatePasswordData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {

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
                                   @Path("locale") String lang);

}
