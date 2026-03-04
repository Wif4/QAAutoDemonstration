package core.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     ConfigManager.class.getClassLoader()
                             .getResourceAsStream("config.properties")) {

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getBugredUrl() {
        return properties.getProperty("bugred.url");
    }

    public static String getJsonPlaceholderUrl() {
        return properties.getProperty("jsonplaceholder.url");
    }

    public static String getToken(){
        return properties.getProperty("api.token");
    }
}