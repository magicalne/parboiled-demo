import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.RecoveringParseRunner;
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
 * Author: zehui.lv@dianrong on 6/15/17.
 */
@BuildParseTree
public class PolicyParser extends BaseParser<Object> {


    Rule PolicyRoot() {
        Policy policy = new Policy();
        final Var<Policy> policyVar = new Var<>(policy);
        return Sequence(InitBlock(policyVar), ZeroOrMore(StepBlock(policyVar)), EOI, push(policyVar.getAndClear()));
    }

    Rule InitBlock(Var<Policy> policyVar) {
        final Var<Step> initVar = new Var<>(new Step());
        return Sequence(
                String("init:"), Spacing(),// push(initVar.get().setName("init")),
                Block(initVar),// push(policyVar.get().setInitStep(initVar.get())),
//                FirstOf(OneOrMore(NewLine()), EOI));
                NewLine());
    }

    Rule StepBlock(Var<Policy> policyVar) {
        final Var<Step> stepVar = new Var<>(new Step());
        return Sequence(
                IgnoreCase("step-"),
                ANY, push(stepVar.get().setName(match())),
                ZeroOrMore(Ch(' '), Block(stepVar), FirstOf(OneOrMore(NewLine()), EOI)),
                push(policyVar.get().getSteps().add(stepVar.get()))
        );
    }

    Rule Block(Var<Step> stepVar) {
        return Sequence(
                RuleSet(stepVar),
                Optional(Mode(stepVar)),
                Optional(PassFlow(stepVar)),
                Optional(RejectFlow(stepVar)),
                Optional(UndefineFlow(stepVar))
        );
    }

    Rule Mode(Var<Step> stepVar) {
        return Sequence(
                String("mode"),
                Ch('='),
                ANY, push(stepVar.get().setMode(Mode.valueOf(match())))
        );
    }

    Rule PassFlow(Var<Step> stepVar) {
        return Sequence(
                IgnoreCase("PASS"),
                String("->"),
                ANY, push(stepVar.get().setPassStep(match()))
        );
    }

    Rule RejectFlow(Var<Step> stepVar) {
        return Sequence(
                IgnoreCase("REJECT"),
                String("->"),
                ANY, push(stepVar.get().setRejectStep(match()))
        );
    }

    Rule UndefineFlow(Var<Step> stepVar) {
        return Sequence(
                IgnoreCase("UNDEFINE"),
                String("->"),
                ANY, push(stepVar.get().setUndefineStep(match()))
        );
    }

    Rule RuleSet(Var<Step> initVar) {
        return Sequence(
                String("ruleSet"),
                Ch('='),
                Rules(initVar));
    }

    Rule Rules(Var<Step> initVar) {
        return Sequence(
                Ch('['),
                SingleRule(initVar),
                Ch(']')
        );
    }

    Rule SingleRule(Var<Step> initVar) {
        return Sequence(
                ZeroOrMore(Ch(' ')),
                OneOrMore(TestNot(Ch(','), Ch('"')), ANY), //push(initVar.get().getRuleSet().add(match())),
                Optional(Ch(',')),
                Optional(ZeroOrMore(Ch(' ')))
        );
    }

    Rule NewLine() {
        return FirstOf('\n', Sequence('\r', Optional('\n')));
    }

    Rule Spacing() {
        return ZeroOrMore(OneOrMore(AnyOf(" \t\r\n\f").label("Whitespace")));
    }

    public static void main(String[] args) throws IOException {
        final PolicyParser policyParser = new PolicyParser();
        final ClassLoader classLoader = policyParser.getClass().getClassLoader();
        final URL resource = classLoader.getResource("workflow.policy");
        if (resource != null) {
            final Path path = Paths.get(resource.getPath());
            final byte[] bytes = Files.readAllBytes(path);
            final String content = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(content);

            final PolicyParser parser = Parboiled.createParser(PolicyParser.class);
            ParsingResult<Policy> result = new RecoveringParseRunner<Policy>(parser.PolicyRoot()).run(content);
            String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
            System.out.println(parseTreePrintOut);
        }
    }
}
