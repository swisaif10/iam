package digital.iam.ma.models.mybundle;

import com.google.gson.annotations.Expose;

public class Date {

    @Expose
    private String expire;
    @Expose
    private String start;

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

}
