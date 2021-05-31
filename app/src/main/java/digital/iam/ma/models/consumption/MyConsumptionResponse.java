package digital.iam.ma.models.consumption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyConsumptionResponse {

    @Expose
    private Item internet;
    @SerializedName("voix")
    private Item calls;

    public Item getInternet() {
        return internet;
    }

    public void setInternet(Item internet) {
        this.internet = internet;
    }

    public Item getCalls() {
        return calls;
    }

    public void setCalls(Item calls) {
        this.calls = calls;
    }
}
