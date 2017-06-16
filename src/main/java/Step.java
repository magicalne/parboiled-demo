import java.util.HashSet;
import java.util.Set;

/**
 * Author: zehui.lv@dianrong on 6/15/17.
 */
public class Step {
    private String name;
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

    public Step setRuleSet(Set<String> ruleSet) {
        this.ruleSet = ruleSet;
        return this;
    }

    public Step addRule(String rule) {
        if (this.ruleSet == null) {
            ruleSet = new HashSet<>();
        }
        ruleSet.add(rule);
        return this;
    }

    public Mode getMode() {
        return mode;
    }

    public Step setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public String getPassStep() {
        return passStep;
    }

    public Step setPassStep(String passStep) {
        this.passStep = passStep;
        return this;
    }

    public String getRejectStep() {
        return rejectStep;
    }

    public Step setRejectStep(String rejectStep) {
        this.rejectStep = rejectStep;
        return this;
    }

    public String getUndefineStep() {
        return undefineStep;
    }

    public Step setUndefineStep(String undefineStep) {
        this.undefineStep = undefineStep;
        return this;
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

    public String getName() {
        return name;
    }

    public Step setName(String name) {
        this.name = name;
        return this;
    }
}
