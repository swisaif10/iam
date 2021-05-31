package digital.iam.ma.models.cmi;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class CMIPaymentData {
    @Expose
    private CMIPaymentResponse response;
    @Expose
    private Header header;

    public CMIPaymentResponse getResponse() {
        return response;
    }

    public void setResponse(CMIPaymentResponse response) {
        this.response = response;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
