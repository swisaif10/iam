package digital.iam.ma.models.controlversion;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class ControlVersionData {
    @Expose
    private Header header;
    @Expose
    private ControlVersionResponse response;

    public ControlVersionResponse getResponse() {
        return response;
    }

    public void setResponse(ControlVersionResponse controlVersionResponse) {
        this.response = controlVersionResponse;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
