/**
 * Author: zehui.lv@dianrong on 6/16/17.
 */
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.Var;

import java.util.List;

@BuildParseTree
public class TaskListParser extends BaseParser<Object> {

    public Rule Tasks() {
        Var<TaskList> tasks = new Var<>(new TaskList());
        return Sequence(OneOrMore(Task(tasks)), EOI, push(tasks.getAndClear()));
    }

    public Rule Task(final Var<TaskList> tasks) {
        Var<Task> dto = new Var<>(new Task());
        return Sequence(Summary(dto), Optional(Options(dto)),
                        Optional(Newline()), push(tasks.get().add(dto.get()))).label(
                "task");
    }

    public Rule Summary(final Var<Task> dto) {
        return Sequence('-', ZeroOrMore(' '), Chars(),
                        push(dto.get().summary(match()))).label("summary");
    }

    public Rule Chars() {
        return OneOrMore(TestNot(OptLim()), TestNot(FieldLim()), ANY);
    }

    public Rule NoCommaChars() {
        return OneOrMore(TestNot(ValSep()), TestNot(OptLim()),
                         TestNot(FieldLim()), ANY);
    }

    public Rule Options(final Var<Task> dto) {
        return Sequence(OptSep(), Optional(Assignee(dto)),
                        Optional(Labels(dto)));
    }

    public Rule OptSep() {
        return Sequence(OptSp(), OptLim(), OptSp());
    }

    public Rule Assignee(final Var<Task> dto) {
        return Sequence(OptSp(), String("assignee"), FieldSep(), FieldLim(),
                        Chars(), push(dto.get().assignee(match())), FieldLim()).label(
                "assignee");
    }

    public Rule FieldLim() {
        return Ch('"');
    }

    public Rule OptLim() {
        return Ch('|');
    }

    public Rule FieldSep() {
        return Ch(':');
    }

    public Rule OptSp() {
        return ZeroOrMore(' ');
    }

    public Rule ValSep() {
        return Ch(',');
    }

    public Rule Labels(final Var<Task> dto) {
        return Sequence(OptSp(), String("labels"), FieldSep(), FieldLim(),
                        OneOrMore(Label(dto)), FieldLim());
    }

    public Rule Label(final Var<Task> dto) {
        return Sequence(OptSp(), NoCommaChars(),
                        push(dto.get().label(match())), Optional(ValSep()), OptSp());
    }

    public Rule Newline() {
        return FirstOf('\n', Sequence('\r', Optional('\n')));
    }

    public static void main(String[] args) {
        String line1 = String
                .format("- A first task | assignee:\"joe\" labels:\"foo,bar,baz\"");
        String line2 = "- This is the second entry | assignee:\"fred\" labels:\"beer,wine\"";
        String line3 = "- And a third entry";
        String dslString = line1 + "\n" + line2 + "\n" + line3;
        final TaskListParser parser = Parboiled.createParser(TaskListParser.class);
        ParsingResult<TaskList> result = new RecoveringParseRunner<TaskList>(parser.Tasks()).run(dslString);
        TaskList taskList = result.resultValue;
        List<Task> tasks = taskList.tasks();
        System.out.println(tasks);
    }
}