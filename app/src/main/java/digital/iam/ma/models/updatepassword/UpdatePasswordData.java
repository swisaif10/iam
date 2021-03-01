package digital.iam.ma.models.updatepassword;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class UpdatePasswordData {

    @Expose
    private Header header;
    @Expose
    private Boolean response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }
}
