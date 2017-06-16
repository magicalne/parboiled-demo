import java.util.ArrayList;
import java.util.List;

/**
 * Author: zehui.lv@dianrong on 6/15/17.
 */
public class Policy {
    private String policyId;
    private Step initStep;
    private List<Step> steps;

    public Policy() {
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public Step getInitStep() {
        return initStep;
    }

    public Policy setInitStep(Step initStep) {
        this.initStep = initStep;
        return this;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Policy addStep(Step step) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(step);
        return this;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "policyId='" + policyId + '\'' +
                ", initStep=" + initStep +
                ", steps=" + steps +
                '}';
    }
}
