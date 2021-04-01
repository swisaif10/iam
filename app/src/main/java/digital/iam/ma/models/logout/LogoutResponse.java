package digital.iam.ma.models.logout;

import com.google.gson.annotations.Expose;

public class LogoutResponse {

    @Expose
    private Boolean logout;

    public Boolean getLogout() {
        return logout;
    }

    public void setLogout(Boolean logout) {
        this.logout = logout;
    }

}
