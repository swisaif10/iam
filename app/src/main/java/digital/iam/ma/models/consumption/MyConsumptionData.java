package digital.iam.ma.models.consumption;

import com.google.gson.annotations.Expose;

import digital.iam.ma.models.commons.Header;

public class MyConsumptionData {

    @Expose
    private Header header;
    @Expose
    private MyConsumptionResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public MyConsumptionResponse getMyConsumptionResponse() {
        return response;
    }

    public void setMyConsumptionResponse(MyConsumptionResponse myConsumptionResponse) {
        this.response = myConsumptionResponse;
    }

}
