package digital.iam.ma.models.cart.add;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class AddItemData {
    @Expose
    private Header header;
    @Expose
    private AddItemResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public AddItemResponse getResponse() {
        return response;
    }

    public void setResponse(AddItemResponse response) {
        this.response = response;
    }
}
