package digital.iam.ma.datamanager.retrofit;

public interface ApiEndpoints {

    String CONTROL_VERSION_URL = "{locale}/version-control/check";

    String LOGIN_URL = "{locale}/login";
    String LOGOUT_URL = "{locale}/logout";
    String RESET_PASSWORD_URL = "{locale}/fo/token-reset-password";
    String ADD_NEW_PASSWORD_URL = "{locale}/fo/set-new-password";
    String UPDATE_PASSWORD_URL = "{locale}/bo/reset-password";
    String UPDATE_PROFILE_URL = "/{locale}/user";

    String SIM_ACTIVATION_URL = "/{locale}/store/forfaits/enable";
    String GET_SERVICES_URL = "{locale}/store/services";
    String GET_BUNDLES_URL = "{locale}/store/liste-forfaits";
    String ADD_ITEM_URL = "{locale}/store/cart/add-item";
    String GET_ITEMS_URL = "{locale}/store/cart/get-items";
    String GET_MY_CONSUMPTION_URL = "{locale}/store/consumption";
    String GET_ORDERS_URL = "{locale}/store/order/history";
    String RECHARGE_LIST_URL = "{locale}/store/liste-recharges";
    String RENEW_BUNDLE_URL = "{locale}/store/forfaits/renew";
    String SWITCH_BUNDLE_URL = "{locale}/store/forfaits/switch";
    String CMI_URL = "{locale}/store/order-pay";
    String SUSPEND_CONTRACT_URL = "{locale}/store/suspend-contract";
    String SEND_OTP_URL = "{locale}/store/send-otp";
    String END_CONTRACT_URL = "{locale}/store/resiliation";
    String CHANGE_SIM_URL = "{locale}/store/sim-change";
    String RESEND_PUK_URL = "{locale}/store/code-puk";
    String UPDATE_SERVICES_URL = "{locale}/store/services-update";
    String RECHARGE_PURCHASE = "{locale}/store/recharge/purchase";

    //String GET_HELP_URL = "https://mocki.io/v1/47649be1-946e-488a-84bd-2c52a552ae7c";
    String GET_HELP_URL = "https://mocki.io/v1/c647f0ac-e0ff-4fff-9395-5466466c1fec";
}