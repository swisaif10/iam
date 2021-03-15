package digital.iam.ma.models.linestatus;

import com.google.gson.annotations.Expose;

public class LineStatusResponse {

    @Expose
    private Data data;
    @Expose
    private String message;
    @Expose
    private String title;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
