package digital.iam.ma.models.login;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class LoginData {

    @Expose
    private Header header;
    @Expose
    private LoginResponseData response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public LoginResponseData getResponse() {
        return response;
    }

    public void setResponse(LoginResponseData response) {
        this.response = response;
    }

}
