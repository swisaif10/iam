package digital.iam.ma.models.consumption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @Expose
    private Object consumed;
    @SerializedName("forfait")
    private String bundle;
    @Expose
    private String percent;

    public Object getConsumed() {
        return consumed;
    }

    public void setConsumed(Object consumed) {
        this.consumed = consumed;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

}
