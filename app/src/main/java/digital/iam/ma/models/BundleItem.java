package digital.iam.ma.models;

public class BundleItem {
    private String title;
    private String subtitle;
    private Boolean selected = false;

    public BundleItem(String title, String subtitle, Boolean selected) {
        this.title = title;
        this.subtitle = subtitle;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
