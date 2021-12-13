package digital.iam.ma.listener;

import digital.iam.ma.models.recharge.RechargeItem;
import digital.iam.ma.models.recharge.RechargeSubItem;

public interface OnRechargeSelectedListener {
    //void onRechargeSelected(RechargeItem rechargeItem, RechargeSubItem rechargeSubItem);
    void onPurchaseRecharge(String sku, int mode);
}
