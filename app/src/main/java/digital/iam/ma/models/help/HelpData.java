package digital.iam.ma.models.help;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import digital.iam.ma.models.commons.Header;

public class HelpData {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("response")
    @Expose
    private List<HelpListResponse> response = null;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<HelpListResponse> getResponse() {
        return response;
    }

    public void setResponse(List<HelpListResponse> response) {
        this.response = response;
    }

}