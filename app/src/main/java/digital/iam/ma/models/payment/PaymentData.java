package digital.iam.ma.models.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentData {

    @SerializedName("cmi")
    @Expose
    private Boolean cmi;
    @SerializedName("fatourati")
    @Expose
    private Boolean fatourati;
    @SerializedName("mtcash")
    @Expose
    private Boolean mtcash;

    public Boolean getCmi() {
        return cmi;
    }

    public void setCmi(Boolean cmi) {
        this.cmi = cmi;
    }

    public Boolean getFatourati() {
        return fatourati;
    }

    public void setFatourati(Boolean fatourati) {
        this.fatourati = fatourati;
    }

    public Boolean getMtcash() {
        return mtcash;
    }

    public void setMtcash(Boolean mtcash) {
        this.mtcash = mtcash;
    }

}
