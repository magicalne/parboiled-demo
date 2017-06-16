import java.util.ArrayList;
import java.util.List;

/**
 * Author: zehui.lv@dianrong on 6/16/17.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    public TaskList add(final Task task) {
        tasks.add(task);
        return this;
    }

    public List<Task> tasks() {
        return tasks;
    }
}