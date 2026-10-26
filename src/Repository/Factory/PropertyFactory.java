package Repository.Factory;

import Repository.Exceptions.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

public class PropertyFactory {
    public Properties propertiesFromConfigFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new ConfigException("The config file path cannot be blank!");
        }
        if (!filePath.endsWith(".properties")) {
            throw new ConfigException("The config file must have a .properties extension!");
        }

        Properties properties = new Properties();

        try (InputStream configStream = Files.newInputStream(Path.of(filePath))) {
            properties.load(configStream);
            return properties;
        }
        catch (IOException e) {
            throw new ConfigException("The config file could not be read: " + filePath);
        }
    }
}
