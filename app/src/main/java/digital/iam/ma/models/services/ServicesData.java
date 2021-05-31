package digital.iam.ma.models.services;

import com.google.gson.annotations.Expose;

import java.util.List;

import digital.iam.ma.models.commons.Header;

public class ServicesData {
    @Expose
    private Header header;
    @Expose
    private List<Service> response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Service> getResponse() {
        return response;
    }

    public void setResponse(List<Service> response) {
        this.response = response;
    }
}
