package digital.iam.ma.models.fatourati;

public class FatouratiResponse {

    public Header header;
    public Response response;

    public class Header {
        public int code;
        public String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public class Response {
        public String status;
        public int code;
        public String refFat;

        public String getStatus() {
            return status;
        }

        public int getCode() {
            return code;
        }

        public String getRefFat() {
            return refFat;
        }
    }

    public Header getHeader() {
        return header;
    }

    public Response getResponse() {
        return response;
    }
}
