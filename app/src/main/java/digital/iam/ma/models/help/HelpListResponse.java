package digital.iam.ma.models.help;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpListResponse {

    @SerializedName("categorie")
    @Expose
    private String categorie;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
