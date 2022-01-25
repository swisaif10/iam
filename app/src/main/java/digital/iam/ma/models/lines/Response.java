package digital.iam.ma.models.lines;

import com.google.gson.annotations.Expose;

import java.util.List;

import digital.iam.ma.models.login.Line;

public class Response {
    @Expose
    public List<Line> lines;

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
