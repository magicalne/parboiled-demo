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

    public void setInitStep(Step initStep) {
        this.initStep = initStep;
    }

    public List<Step> getSteps() {
        return steps;
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
