import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

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
public class PolicyParser extends BaseParser<Policy> {

    private Policy policy;
    public PolicyParser() {
        policy = new Policy();
    }

    Rule PolicyRoot() {
        return Sequence(InitBlock(), ZeroOrMore(StepBlock()));
    }

    Rule InitBlock() {
        return Sequence(
                String("init:"),
                FlowBlock(),
                FirstOf(OneOrMore(NewLine()), EOI));
    }

    Rule StepBlock() {
        return Sequence(
                IgnoreCase("step-"),
                ANY,
                ZeroOrMore(Ch(' '))

        );
    }

    Rule FlowBlock() {
        return FirstOf(
                RuleSet(),
                Optional(Mode()),
                Optional(Flow()),
                Optional(Flow()),
                Optional(Flow())
        );
    }

    Rule Mode() {
        return Sequence(String("mode"), Ch('='), ANY);
    }

    Rule Flow() {
        return Sequence(
                FirstOf(String("PASS"), String("REJECT"), String("UNDEFINE")),
                String("->"),
                ANY
        );
    }

    Rule RuleSet() {
        return Sequence(String("ruleSet"), Ch('='), Rules());
    }

    Rule Rules() {
        return Sequence(Ch('['), SingleRule(), Ch(']'));
    }

    Rule SingleRule() {
        return Sequence(
                ZeroOrMore(Ch(' ')),
                OneOrMore(TestNot(Ch(',')), ANY),
                Optional(Ch(',')),
                Optional(ZeroOrMore(Ch(' ')))
        );
    }

    public Rule NewLine() {
        return FirstOf('\n', Sequence('\r', Optional('\n')));
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
            ParsingResult<?> result = new ReportingParseRunner(parser.PolicyRoot()).run(content);
            String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
            System.out.println(parseTreePrintOut);
        }
    }
}
