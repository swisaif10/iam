package digital.iam.ma.models.cmi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CMIPaymentResponse {
    @SerializedName("url_cmi")
    private String urlCmi;
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlCmi() {
        return urlCmi;
    }

    public void setUrlCmi(String urlCmi) {
        this.urlCmi = urlCmi;
    }
}
