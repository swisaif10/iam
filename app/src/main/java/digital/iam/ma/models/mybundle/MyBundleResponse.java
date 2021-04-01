package digital.iam.ma.models.mybundle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyBundleResponse {

    @SerializedName("date_renew")
    private Date date;
    @Expose
    private Bundle forfait;
    @Expose
    private String msisdn;
    @Expose
    private Total total;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Bundle getBundle() {
        return forfait;
    }

    public void setBundle(Bundle forfait) {
        this.forfait = forfait;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }
}
