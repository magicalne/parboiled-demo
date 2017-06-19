import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Author: zehui.lv@dianrong on 6/19/17.
 */
public class ScriptEngineTest {
    private static final ScriptEngineManager MANAGER = new ScriptEngineManager();
    protected static final ScriptEngine GROOVY_ENGINE = MANAGER.getEngineByName("groovy");

    CompiledScript compile(String script) throws ScriptException {
        final javax.script.ScriptEngine groovyEngine = MANAGER.getEngineByName("groovy");
        return ((Compilable) groovyEngine).compile(script);
    }

    SimpleBindings execute(CompiledScript script, Person person) throws ScriptException {
        final SimpleBindings bindings = new SimpleBindings();
        bindings.put("person", person);
        script.eval(bindings);
        return bindings;
    }

    Object invoke(String script, Person person) throws ScriptException, NoSuchMethodException {
        GROOVY_ENGINE.eval(script);
        final Invocable invocable = (Invocable) GROOVY_ENGINE;
        final Object result = invocable.invokeFunction("rule", person);
        return result;
    }

    private static class Person {
        private int age;
        private String city;

        public Person() {
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException, ScriptException, NoSuchMethodException {
        final ScriptEngineTest test = new ScriptEngineTest();
        final ClassLoader classLoader = test.getClass().getClassLoader();
        final URL resource = classLoader.getResource("InvocableTest.groovy");
        if (resource != null) {
            final Path path = Paths.get(resource.getPath());
            final byte[] bytes = Files.readAllBytes(path);
            final String content = new String(bytes, StandardCharsets.UTF_8);
            final CompiledScript compiledScript = test.compile(content);
            final Person person = new Person();
            person.setAge(22);
            LocalDateTime start = LocalDateTime.now();
            for (int i = 0; i < 100000; i ++) {
                final SimpleBindings bindings = test.execute(compiledScript, person);
            }
            LocalDateTime end = LocalDateTime.now();
            Duration duration = Duration.between(start, end);
            System.out.println("duration:" + duration);

//            System.out.println(bindings);

            System.out.println("==========================================+");

            start = LocalDateTime.now();
            for (int i = 0; i < 100000; i ++) {
                final Object result = test.invoke(content, person);
            }
            end = LocalDateTime.now();
            duration = Duration.between(start, end);
            System.out.println("duration:" + duration);
        }
    }
}
