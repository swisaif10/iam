package digital.iam.ma.models.recharge;

import com.google.gson.annotations.Expose;

public class RechargeSubItem {
    @Expose
    private Integer price;
    @Expose
    private String name;
    @Expose
    private Integer id;
    @Expose
    private String sku;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
