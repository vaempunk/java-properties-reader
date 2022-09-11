package com.vaempunk.properties;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Simple properties file format scanner
 * 
 * @author vaem
 * @version 0.9
 */
public class PropertiesReader {

    /**
     * Get map of key-value pairs in a file
     * 
     * @param filePath path to the input file
     * @param charset  charset of the file
     * @return {@code LinkedHashMap} containing pairs. Null if there is an error
     *         occured while reading file
     * @throws IOException
     */
    public static Map<String, String> getAll(Path filePath, Charset charset)
            throws IOException {

        try (Scanner scanner = new Scanner(filePath, charset);) {

            Map<String, String> result = new LinkedHashMap<>();

            while (scanner.hasNextLine()) {
                String entryString = scanner.nextLine().trim();
                while (entryString.charAt(entryString.length() - 1) == '\\'
                        && entryString.charAt(entryString.length() - 2) != '\\'
                        && scanner.hasNextLine())
                    entryString = entryString.substring(0, entryString.length() - 1) + scanner.nextLine().trim();

                Pair<String, String> entry = extractPair(entryString);

                if (entry != null)
                    result.put(entry.getKey(), entry.getValue());
            }

            return result;
        }

    }

    /**
     * Extract pair from a string containing key-value pair
     * 
     * @param entryString
     * @return {@code Pair<String, String>} containing pair. Null if the pair is not
     *         found.
     */
    public static Pair<String, String> extractPair(String entryString) {
        if (entryString == null || entryString.isEmpty()
                || entryString.charAt(0) == '!' || entryString.charAt(0) == '#')
            return null;

        int separator = entryString.indexOf('=');
        if (separator == -1)
            separator = entryString.indexOf(':');
        if (separator == -1)
            return null;

        return new Pair<String, String>(
                entryString.substring(0, separator).trim(),
                entryString.substring(separator + 1).trim());

    }

    public static class Pair<T, V> {

        private T key;
        private V value;

        public Pair(T key, V value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

}
