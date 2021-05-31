package digital.iam.ma.models.recharge;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class RechargeListResponse {
    @Expose
    private List<RechargeItem> recharges;

    public List<RechargeItem> getRecharges() {
        return recharges;
    }

    public void setRecharges(List<RechargeItem> recharges) {
        this.recharges = recharges;
    }

    public List<String> getRechargesNames() {
        List<String> names = new ArrayList<>();
        for (RechargeItem recharge : recharges) {
            names.add(recharge.getType().getName());
        }
        return names;
    }
}
