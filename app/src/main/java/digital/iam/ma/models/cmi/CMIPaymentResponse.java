package digital.iam.ma.models.cmi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CMIPaymentResponse {
    @SerializedName(value = "url", alternate = {"url_cmi"})
    private String url;
    @Expose
    private String status;
    @Expose
    private String message;
    @Expose
    private String code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
