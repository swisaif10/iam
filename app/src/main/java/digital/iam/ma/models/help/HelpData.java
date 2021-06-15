package digital.iam.ma.models.help;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpData {
    @SerializedName("assistance")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }
}