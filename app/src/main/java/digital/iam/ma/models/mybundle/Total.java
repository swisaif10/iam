package digital.iam.ma.models.mybundle;

import com.google.gson.annotations.Expose;

public class Total {
    @Expose
    private String amount;
    @Expose
    private String currency;
    @Expose
    private String period;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
