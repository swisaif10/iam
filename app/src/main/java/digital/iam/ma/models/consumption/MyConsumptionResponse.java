package digital.iam.ma.models.consumption;

import com.google.gson.annotations.Expose;

public class MyConsumptionResponse {

    @Expose
    private MyConsumption consumption;

    public MyConsumption getMyConsumption() {
        return consumption;
    }

    public void setMyConsumption(MyConsumption myConsumption) {
        this.consumption = myConsumption;
    }

}
