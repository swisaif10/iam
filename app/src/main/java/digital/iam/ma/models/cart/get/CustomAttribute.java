package digital.iam.ma.models.cart.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomAttribute {

    @SerializedName("attribute_code")
    private String attributeCode;
    @Expose
    private String value;

    public String getAttributeCode() {
        return attributeCode;
    }

    public void setAttributeCode(String attributeCode) {
        this.attributeCode = attributeCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
