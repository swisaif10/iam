package digital.iam.ma.models.recharge;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class RechargeListData {

    @Expose
    private Header header;
    @Expose
    private RechargeListResponse response;


    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public RechargeListResponse getResponse() {
        return response;
    }

    public void setResponse(RechargeListResponse response) {
        this.response = response;
    }
}
