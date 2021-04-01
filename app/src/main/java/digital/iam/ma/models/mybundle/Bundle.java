package digital.iam.ma.models.mybundle;

import com.google.gson.annotations.Expose;

public class Bundle {

    @Expose
    private String details;
    @Expose
    private String label;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
