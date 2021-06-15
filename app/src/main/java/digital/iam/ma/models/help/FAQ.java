package digital.iam.ma.models.help;

import com.google.gson.annotations.Expose;

public class FAQ {
    @Expose
    private String title;
    @Expose
    private String expandable;
    @Expose
    private String content;

    public String getTitle() {
        return title;
    }

    public String getExpandable() {
        return expandable;
    }

    public String getContent() {
        return content;
    }
}
