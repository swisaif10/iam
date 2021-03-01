package digital.iam.ma.models.login;

import com.google.gson.annotations.Expose;

public class LoginResponse {

    @Expose
    private LoginResponseData data;

    public LoginResponseData getData() {
        return data;
    }

    public void setData(LoginResponseData data) {
        this.data = data;
    }
}
