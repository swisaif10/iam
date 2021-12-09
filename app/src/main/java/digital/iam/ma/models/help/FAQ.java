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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExpandable(String expandable) {
        this.expandable = expandable;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
