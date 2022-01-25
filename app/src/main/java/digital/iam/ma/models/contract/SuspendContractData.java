package digital.iam.ma.models.contract;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class SuspendContractData {
    @Expose
    private Header header;
    @Expose
    private Response response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}

