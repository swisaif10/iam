package digital.iam.ma.models.recharge;

import com.google.gson.annotations.Expose;

public class RechargeItem {

    @Expose
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
