package digital.iam.ma.models.linestatus;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class LineStatusData {

    @Expose
    private Header header;
    @Expose
    private LineStatusResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public LineStatusResponse getResponse() {
        return response;
    }

    public void setResponse(LineStatusResponse response) {
        this.response = response;
    }
}
