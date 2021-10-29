package digital.iam.ma.models.recharge;

import com.google.gson.annotations.SerializedName;

public class RechargePurchaseResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("code")
    public int code;
    @SerializedName("url")
    public String url;
    @SerializedName("message")
    public String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
