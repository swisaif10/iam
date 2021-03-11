
package digital.iam.ma.models.logout;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class LogoutData {

    @Expose
    private Header header;
    @Expose
    private LogoutResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public LogoutResponse getLogoutResponse() {
        return response;
    }

    public void setLogoutResponse(LogoutResponse logoutResponse) {
        this.response = logoutResponse;
    }

}
