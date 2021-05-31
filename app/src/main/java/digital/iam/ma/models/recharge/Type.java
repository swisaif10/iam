package digital.iam.ma.models.recharge;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Type {
    @Expose
    private List<RechargeSubItem> children;
    @Expose
    private String name;
    @Expose
    private Integer id;
    @Expose
    private String sku;

    public List<RechargeSubItem> getChildren() {
        return children;
    }

    public void setChildren(List<RechargeSubItem> children) {
        this.children = children;
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

    public List<String> getRechargesNames() {
        List<String> names = new ArrayList<>();
        for (RechargeSubItem item : children) {
            names.add(String.valueOf(item.getPrice()));
        }
        return names;
    }
}