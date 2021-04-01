package digital.iam.ma.models.mybundle;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class MyBundleData {

    @Expose
    private Header header;
    @Expose
    private MyBundleResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public MyBundleResponse getMyBundleResponse() {
        return response;
    }

    public void setMyBundleResponse(MyBundleResponse myBundleResponse) {
        this.response = myBundleResponse;
    }

}
