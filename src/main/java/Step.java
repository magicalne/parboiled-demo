import java.util.Set;

/**
 * Author: zehui.lv@dianrong on 6/15/17.
 */
public class Step {
    private Set<String> ruleSet;
    private Mode mode;
    private String passStep;
    private String rejectStep;
    private String undefineStep;

    public Step() {
    }

    public Set<String> getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(Set<String> ruleSet) {
        this.ruleSet = ruleSet;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getPassStep() {
        return passStep;
    }

    public void setPassStep(String passStep) {
        this.passStep = passStep;
    }

    public String getRejectStep() {
        return rejectStep;
    }

    public void setRejectStep(String rejectStep) {
        this.rejectStep = rejectStep;
    }

    public String getUndefineStep() {
        return undefineStep;
    }

    public void setUndefineStep(String undefineStep) {
        this.undefineStep = undefineStep;
    }

    @Override
    public String toString() {
        return "Step{" +
                "ruleSet=" + ruleSet +
                ", mode=" + mode +
                ", passStep='" + passStep + '\'' +
                ", rejectStep='" + rejectStep + '\'' +
                ", undefineStep='" + undefineStep + '\'' +
                '}';
    }
}
