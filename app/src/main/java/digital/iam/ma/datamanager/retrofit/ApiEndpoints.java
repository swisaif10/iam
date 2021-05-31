package digital.iam.ma.datamanager.retrofit;

public interface ApiEndpoints {

    String CONTROL_VERSION_URL = "api/{locale}/version-control/check";
    String RESET_PASSWORD_URL = "api/{locale}/fo/token-reset-password";
    String ADD_NEW_PASSWORD_URL = "api/{locale}/fo/set-new-password";
    String LOGIN_URL = "api/{locale}/login";
    String SIM_ACTIVATION_URL = "api/{locale}/sim-activation";
    String UPDATE_PASSWORD_URL = "api/{locale}/bo/reset-password";
    String GET_SERVICES_URL = "api/{locale}/services";
    String GET_BUNDLES_URL = "api/{locale}/store/liste-forfaits";
    String ADD_ITEM_URL = "api/{locale}/store/cart/add-item";
    String GET_ITEMS_URL = "api/{locale}/store/cart/get-items";
    String LOGOUT_URL = "api/{locale}/logout";
    String GET_MY_CONSUMPTION_URL = "api/{locale}/consumption";
    String GET_ORDERS_URL = "api/{locale}/store/order/history";
    String RECHARGE_LIST_URL = "api/{locale}/store/liste-recharges";
    String PURCHASE_RECHARGE_URL = "api/{locale}/store/recharge/purchase";
    String RENEW_BUNDLE_URL = "api/{locale}/store/forfaits/renew";
    String SWITCH_BUNDLE_URL = "api/{locale}/store/forfaits/switch";
}