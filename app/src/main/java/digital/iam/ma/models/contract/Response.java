package digital.iam.ma.models.contract;

import com.google.gson.annotations.Expose;

public class Response {
    @Expose
    public String msisdn;
    @Expose
    public String puk;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }
}
