package digital.iam.ma.models.linestatus;

import com.google.gson.annotations.Expose;

public class Data {

    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
