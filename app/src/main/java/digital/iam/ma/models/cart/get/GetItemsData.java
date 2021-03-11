package digital.iam.ma.models.cart.get;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class GetItemsData {
    @Expose
    private Header header;
    @Expose
    private GetItemsResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public GetItemsResponse getResponse() {
        return response;
    }

    public void setResponse(GetItemsResponse response) {
        this.response = response;
    }
}
