package digital.iam.ma.models.contract;

import digital.iam.ma.models.commons.Header;

public class Contract {
    public Header header;
    public String response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

