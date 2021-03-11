package digital.iam.ma.models.consumption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyConsumption {

    @Expose
    private Item internet;
    @SerializedName("voix")
    private Item voice;

    public Item getInternet() {
        return internet;
    }

    public void setInternet(Item internet) {
        this.internet = internet;
    }

    public Item getVoice() {
        return voice;
    }

    public void setVoice(Item voice) {
        this.voice = voice;
    }
}
