package digital.iam.ma.models.orders;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class GetOrdersData {

    @Expose
    private Header header;
    @Expose
    private GetOrdersResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public GetOrdersResponse getResponse() {
        return response;
    }

    public void setResponse(GetOrdersResponse response) {
        this.response = response;
    }
}
