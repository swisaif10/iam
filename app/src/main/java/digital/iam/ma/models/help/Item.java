package digital.iam.ma.models.help;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Item {
    @Expose
    private String identifier;
    @Expose
    private String title;
    @Expose
    private List<FAQ> faqs;

    public String getIdentifier() {
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public List<FAQ> getFaqs() {
        return faqs;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFaqs(List<FAQ> faqs) {
        this.faqs = faqs;
    }

    @Override
    public String toString() {
        return "Item{" +
                "identifier='" + identifier + '\'' +
                ", title='" + title + '\'' +
                ", faqs=" + faqs +
                '}';
    }
}