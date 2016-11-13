package example.jbot.model;

import java.util.HashMap;
import java.util.Map;

public class Environments {

    private static Map<String, String> environments = new HashMap();

    static {
        environments.put("demo", "http://52.36.28.37/");
        environments.put("perf1", "http://52.42.141.41/");
        environments.put("perf2", "http://35.161.210.211/");
        environments.put("reg1", "http://52.89.111.6/");
        environments.put("reg2", "http://52.89.211.248/");
        environments.put("qa1", "http://52.34.132.141/");
    }

    public static String getUrlForEnvironment(String environment) {
        return environments.get(environment);
    }
}
