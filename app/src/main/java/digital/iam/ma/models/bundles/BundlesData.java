package digital.iam.ma.models.bundles;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class BundlesData {
    @Expose
    private Header header;
    @Expose
    private BundlesResponseData response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public BundlesResponseData getResponse() {
        return response;
    }

    public void setResponse(BundlesResponseData response) {
        this.response = response;
    }
}
