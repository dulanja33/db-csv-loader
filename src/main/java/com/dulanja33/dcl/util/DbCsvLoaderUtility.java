package com.dulanja33.dcl.util;

import com.dulanja33.dcl.exception.DclException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DbCsvLoaderUtility {
    final static private ObjectMapper mapper = new ObjectMapper();
    final static private Yaml yaml = new Yaml();

    private DbCsvLoaderUtility() {
    }

    public static <T> T readYmlFileToObject(String filePath, Class<T> clazz) throws DclException {
        File file = new File(filePath);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        try (InputStream fileInputStream = new FileInputStream(file)) {
            return mapper.convertValue(yaml.load(fileInputStream), clazz);
        } catch (Exception e) {
            log.error("Unable to read mapping yml file: {}", e.getMessage());
            throw new DclException("Unable to read mapping yml file.");
        }
    }

    public static String substituteValues(String base, Map<String, String> subValues) {
        StringBuilder resultString = new StringBuilder();
        char[] chars = base.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '$') {
                int k = i + 2; // omit $ and {
                StringBuilder placeHolder = new StringBuilder();
                c = chars[k];
                while (c != '}') {
                    placeHolder.append(c);
                    c = chars[++k];
                }

                resultString.append(subValues.get(placeHolder.toString()));
                i = k;
            } else {
                resultString.append(c);
            }
        }

        return resultString.toString();
    }

    public static String substituteArray(String base, List<Map<String, String>> data) {
        final String regex = "\\$\\{\\([\\S\\s]*\\)\\}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(base);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String placeHolder = matcher.group().substring(2, matcher.group().length() - 1);
            String valueUpdateString = substituteValues(placeHolder, data.get(0));
            matcher.appendReplacement(sb, valueUpdateString);

            for (int i = 1; i < data.size(); i++) {
                sb.append(",").append(substituteValues(placeHolder, data.get(i)));
            }
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String escapeString(String str, char escapeChar, char charToEscape) {
        if (str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i=0; i<str.length(); i++) {
            char curChar = str.charAt(i);
            if (curChar == escapeChar || curChar == charToEscape) {
                result.append(escapeChar).append(escapeChar);
            }
            result.append(curChar);
        }
        return result.toString();
    }
}
