package digital.iam.ma.datamanager.retrofit;

public interface ApiEndpoints {

    String CONTROL_VERSION_URL = "api/{locale}/version-control/check";

    String LOGIN_URL = "api/{locale}/login";
    String LOGOUT_URL = "api/{locale}/logout";
    String RESET_PASSWORD_URL = "api/{locale}/fo/token-reset-password";
    String ADD_NEW_PASSWORD_URL = "api/{locale}/fo/set-new-password";
    String UPDATE_PASSWORD_URL = "api/{locale}/bo/reset-password";
    String UPDATE_PROFILE_URL = "/api/{locale}/user";

    String SIM_ACTIVATION_URL = "/api/{locale}/store/forfaits/enable";
    String GET_SERVICES_URL = "api/{locale}/store/services";
    String GET_BUNDLES_URL = "api/{locale}/store/liste-forfaits";
    String ADD_ITEM_URL = "api/{locale}/store/cart/add-item";
    String GET_ITEMS_URL = "api/{locale}/store/cart/get-items";
    String GET_MY_CONSUMPTION_URL = "api/{locale}/store/consumption";
    String GET_ORDERS_URL = "api/{locale}/store/order/history";
    String RECHARGE_LIST_URL = "api/{locale}/store/liste-recharges";
    String RENEW_BUNDLE_URL = "api/{locale}/store/forfaits/renew";
    String SWITCH_BUNDLE_URL = "api/{locale}/store/forfaits/switch";
    String CMI_URL = "api/{locale}/store/order-pay";
    String SUSPEND_CONTRACT_URL = "api/{locale}/store/suspend-contract";
    String SEND_OTP_URL = "api/{locale}/store/send-otp";
    String END_CONTRACT_URL = "api/{locale}/store/resiliation";
    String CHANGE_SIM_URL = "api/{locale}/store/sim-change";
    String RESEND_PUK_URL = "api/{locale}/store/code-puk";

    String GET_HELP_URL = "https://mocki.io/v1/47649be1-946e-488a-84bd-2c52a552ae7c";
}