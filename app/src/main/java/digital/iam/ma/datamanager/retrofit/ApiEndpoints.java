package digital.iam.ma.datamanager.retrofit;

public interface ApiEndpoints {

    String RESET_PASSWORD_URL = "/api/{locale}/fo/token-reset-password";
    String ADD_NEW_PASSWORD_URL = "/api/{locale}/fo/set-new-password";
    String LOGIN_URL = "api/{locale}/login";
    String SIM_ACTIVATION_URL = "/api/{locale}/sim-activation";
    String UPDATE_PASSWORD_URL = "/api/{locale}/bo/reset-password";
    String GET_SERVICES_URL = "/api/{locale}/services";

}
