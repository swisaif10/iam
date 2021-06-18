package digital.iam.ma.models.profile;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class UpdateProfileData {
    @Expose
    private Response response;
    @Expose
    private Header header;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
