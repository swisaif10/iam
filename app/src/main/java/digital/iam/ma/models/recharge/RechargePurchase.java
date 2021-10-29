package digital.iam.ma.models.recharge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import digital.iam.ma.models.commons.Header;

public class RechargePurchase {
    @Expose
    private Header header;
    @Expose
    private RechargePurchaseResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public RechargePurchaseResponse getResponse() {
        return response;
    }

    public void setResponse(RechargePurchaseResponse response) {
        this.response = response;
    }
}
