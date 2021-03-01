package digital.iam.ma.models.services;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class ServicesData {
    @Expose
    private Header header;
    @Expose
    private ServicesResponseData response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ServicesResponseData getResponse() {
        return response;
    }

    public void setResponse(ServicesResponseData response) {
        this.response = response;
    }
}
