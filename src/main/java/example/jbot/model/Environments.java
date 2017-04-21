package example.jbot.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Environments {

    private static Map<String, String> environments = new HashMap();

    static {
        environments.put("demo", "http://xyz/");
        environments.put("perf1", "http://xyz/");
        environments.put("perf2", "http://xyz/");
        environments.put("reg1", "http://xyz/");
        environments.put("reg2", "http://xyz/");
        environments.put("qa1", "http://xyz/");
        environments.put("staging", "http://xyz/");
    }

    public static String getUrlForEnvironment(String environment) {
        return environments.get(environment);
    }

    public static Set<String> getEnvironmentNames() {
        return environments.keySet();
    }
}
