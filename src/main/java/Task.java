import java.util.HashSet;
import java.util.Set;

/**
 * Author: zehui.lv@dianrong on 6/16/17.
 */
public class Task {
    private String summary;
    private String assignee;
    private final Set<String> labels = new HashSet<>();

    public final String summary() {
        return summary;
    }

    public final Task summary(final String summary) {
        this.summary = summary;
        return this;
    }

    public final String assignee() {
        return assignee;
    }

    public final Task assignee(final String assignee) {
        this.assignee = assignee;
        return this;
    }

    public Set<String> labels() {
        return labels;
    }

    public Task label(final String label) {
        labels.add(label);
        return this;
    }

    @Override
    public String toString() {
        return "Task{" +
                "summary='" + summary + '\'' +
                ", assignee='" + assignee + '\'' +
                ", labels=" + labels +
                '}';
    }
}