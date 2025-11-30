package it.unibo.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Loads a Configuration from a configuration file.
 */
public final class ConfigurationLoader {

    private ConfigurationLoader() {
        // Prevent instantiation
    }

    /**
     * Loads a configuration from a file.
     * 
     * @param configFile the configuration file path
     * @return the loaded configuration
     */
    public static Configuration load(final String configFile) {
        final Configuration.Builder builder = new Configuration.Builder();
        final InputStream resourceStream = ClassLoader.getSystemResourceAsStream(configFile);

        if (resourceStream == null) {
            throw new IllegalStateException("Configuration file not found: " + configFile);
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(resourceStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) { // NOPMD Normal usage of readLine
                final StringTokenizer tokenizer = new StringTokenizer(line, ":");

                if (tokenizer.countTokens() == 2) {
                    final String parameter = tokenizer.nextToken().trim().toLowerCase(Locale.ROOT);
                    final int value = Integer.parseInt(tokenizer.nextToken().trim());
                    switch (parameter) {
                        case "minimum":
                            builder.withMin(value);
                            break;
                        case "maximum":
                            builder.withMax(value);
                            break;
                        case "attempts":
                        builder.withAttempts(value);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException("Error loading configuration", e);
        }
        return builder.build();
    }
}
