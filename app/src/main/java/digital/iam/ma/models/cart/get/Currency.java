package digital.iam.ma.models.cart.get;

import com.google.gson.annotations.SerializedName;

public class Currency {

    @SerializedName("base_currency_code")
    private String baseCurrencyCode;
    @SerializedName("base_to_global_rate")
    private int baseToGlobalRate;
    @SerializedName("base_to_quote_rate")
    private int baseToQuoteRate;
    @SerializedName("global_currency_code")
    private String globalCurrencyCode;
    @SerializedName("quote_currency_code")
    private String quoteCurrencyCode;
    @SerializedName("store_currency_code")
    private String storeCurrencyCode;
    @SerializedName("store_to_base_rate")
    private int storeToBaseRate;
    @SerializedName("store_to_quote_rate")
    private int storeToQuoteRate;

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public int getBaseToGlobalRate() {
        return baseToGlobalRate;
    }

    public void setBaseToGlobalRate(int baseToGlobalRate) {
        this.baseToGlobalRate = baseToGlobalRate;
    }

    public int getBaseToQuoteRate() {
        return baseToQuoteRate;
    }

    public void setBaseToQuoteRate(int baseToQuoteRate) {
        this.baseToQuoteRate = baseToQuoteRate;
    }

    public String getGlobalCurrencyCode() {
        return globalCurrencyCode;
    }

    public void setGlobalCurrencyCode(String globalCurrencyCode) {
        this.globalCurrencyCode = globalCurrencyCode;
    }

    public String getQuoteCurrencyCode() {
        return quoteCurrencyCode;
    }

    public void setQuoteCurrencyCode(String quoteCurrencyCode) {
        this.quoteCurrencyCode = quoteCurrencyCode;
    }

    public String getStoreCurrencyCode() {
        return storeCurrencyCode;
    }

    public void setStoreCurrencyCode(String storeCurrencyCode) {
        this.storeCurrencyCode = storeCurrencyCode;
    }

    public int getStoreToBaseRate() {
        return storeToBaseRate;
    }

    public void setStoreToBaseRate(int storeToBaseRate) {
        this.storeToBaseRate = storeToBaseRate;
    }

    public int getStoreToQuoteRate() {
        return storeToQuoteRate;
    }

    public void setStoreToQuoteRate(int storeToQuoteRate) {
        this.storeToQuoteRate = storeToQuoteRate;
    }

}
