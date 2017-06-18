import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.Var;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Author: zehui.lv@dianrong on 6/16/17.
 */
@BuildParseTree
public class PolicyParser1 extends BaseParser<Object> {

    Rule Policy() {
        final Policy policy = new Policy();
        policy.setPolicyId("test");
        final Var<Policy> policyVar = new Var<>(policy);
        return Sequence(
                InitBlock(policyVar),
                StepBlockList(policyVar),
                EOI,
                push(policyVar.get())
        );
    }

    Rule InitBlock(Var<Policy> policyVar) {
        final Var<Step> stepVar = new Var<>(new Step());
        return Sequence(
                ZeroOrMore(Spacing()),
                String("init:"),
                NewLine(),
                Step(stepVar),
                policyVar.set(policyVar.get().setInitStep(stepVar.get()))
        );
    }

    Rule StepBlockList(Var<Policy> policyVar) {
        return ZeroOrMore(
                StepBlock(policyVar)
        );
    }

    Rule StepBlock(Var<Policy> policyVar) {
        final Var<Step> stepVar = new Var<>(new Step());
        return Sequence(
                ZeroOrMore(Spacing()),
                String("step"),
                Spacing(),
                StepName(stepVar),
                ColonChar(),
                NewLine(),
                Step(stepVar),
                ZeroOrMore(NewLine()),
                policyVar.set(policyVar.get().addStep(stepVar.get()))
        );
    }

    Rule StepName(Var<Step> stepVar) {
        return Sequence(
                OneOrMore(
                        TestNot(Spacing()),
                        TestNot(ColonChar()),
                        ANY
                ),
                stepVar.set(stepVar.get().setName(match()))

        );
    }

    Rule ColonChar() {
        return Ch(':');
    }

    Rule Step(Var<Step> stepVar) {
        return Sequence(
                RuleSet(stepVar),
                Optional(Mode(stepVar)),
                Optional(PassFlow(stepVar)),
                Optional(RejectFlow(stepVar)),
                Optional(UndefineFlow(stepVar))
        );
    }

    Rule RuleSet(Var<Step> stepVar) {
        return Sequence(
                IgnoreCase("ruleSet"),
                Spacing(),
                EqualChar(),
                Spacing(),
                Array(stepVar),
                Optional(NewLine())
                );
    }

    Rule Array(Var<Step> stepVar) {
        return Sequence(
                Ch('['),
                Element(stepVar),
                ZeroOrMore(Sequence(CommaChar(), Element(stepVar))),
                Ch(']')
        );
    }

    Rule Element(Var<Step> stepVar) {
        return Sequence(
                ZeroOrMore(Spacing()),
                OneOrMore(
                        TestNot(Ch('[')),
                        TestNot(Ch(']')),
                        TestNot(Spacing()),
                        TestNot(CommaChar()),
                        ANY),
                stepVar.set(stepVar.get().addRule(match())),
                ZeroOrMore(Spacing())
        );
    }

    Rule Mode(Var<Step> stepVar) {
        return Sequence(
                ZeroOrMore(Spacing()),
                String("mode"),
                ZeroOrMore(Spacing()),
                EqualChar(),
                ZeroOrMore(Spacing()),
                OneOrMore(
                        TestNot(Spacing()),
                        TestNot(NewLine()),
                        TestNot(EOI),
                        ANY
                ),
                stepVar.set(stepVar.get().setMode(Mode.valueOf(match()))),
                ZeroOrMore(Spacing())
                ).label("Mode");
    }

    Rule PassFlow(Var<Step> stepVar) {
        return Sequence(
                ZeroOrMore(Spacing()),
                IgnoreCase("pass"),
                ZeroOrMore(Spacing()),
                ArrowString(),
                ZeroOrMore(Spacing()),
                OneOrMore(
                        TestNot(Spacing()),
                        TestNot(NewLine()),
                        TestNot(EOI),
                        ANY
                ),
                stepVar.set(stepVar.get().setPassStep(match())),
                ZeroOrMore(Spacing())
                );
    }

    Rule RejectFlow(Var<Step> stepVar) {
        return Sequence(
                ZeroOrMore(Spacing()),
                IgnoreCase("reject"),
                ZeroOrMore(Spacing()),
                ArrowString(),
                ZeroOrMore(Spacing()),
                OneOrMore(
                        TestNot(Spacing()),
                        TestNot(NewLine()),
                        TestNot(EOI),
                        ANY
                ),
                stepVar.set(stepVar.get().setRejectStep(match())),
                ZeroOrMore(Spacing())
        );
    }

    Rule UndefineFlow(Var<Step> stepVar) {
        return Sequence(
                ZeroOrMore(Spacing()),
                IgnoreCase("undefine"),
                ZeroOrMore(Spacing()),
                ArrowString(),
                ZeroOrMore(Spacing()),
                OneOrMore(
                        TestNot(Spacing()),
                        TestNot(NewLine()),
                        TestNot(EOI),
                        ANY
                ),
                stepVar.set(stepVar.get().setUndefineStep(match())),
                ZeroOrMore(Spacing())
        );
    }

    Rule ArrowString() {
        return String("->");
    }

    Rule EqualChar() {
        return Ch('=');
    }

    Rule CommaChar() {
        return Ch(',');
    }

    Rule Spacing() {
        return OneOrMore(AnyOf(" \t\r\n\f").label("BlankSpace"));
    }

    Rule NewLine() {
        return FirstOf('\n', Sequence('\r', Optional('\n')));
    }

    public static void main(String[] args) throws IOException {
        final PolicyParser policyParser = new PolicyParser();
        final ClassLoader classLoader = policyParser.getClass().getClassLoader();
        final URL resource = classLoader.getResource("workflow1.policy");
        if (resource != null) {
            final Path path = Paths.get(resource.getPath());
            final byte[] bytes = Files.readAllBytes(path);
            final String content = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(content);

            final PolicyParser1 parser = Parboiled.createParser(PolicyParser1.class);
            ParsingResult<Policy> result = new TracingParseRunner<Policy>(parser.Policy()).run(content);
            String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
            System.out.println(parseTreePrintOut);
            System.out.println("==============================================");
            System.out.println(result.resultValue);
        }
    }
}
