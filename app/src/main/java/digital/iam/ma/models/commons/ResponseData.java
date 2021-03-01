package digital.iam.ma.models.commons;

import com.google.gson.annotations.Expose;

public class ResponseData {

    @Expose
    private Header header;
    @Expose
    private Object response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

}
