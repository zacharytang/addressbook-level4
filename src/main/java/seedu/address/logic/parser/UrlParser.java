package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_TIMETABLE_URL;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Module;
import seedu.address.model.person.Timetable;

/**
 * Helper class to parse NUSMods urls.
 */
public class UrlParser {

    private static final String NUSMODS_SHORT = "modsn.us";
    private static final String NUSMODS_HOST = "nusmods.com";
    private static final String URL_HOST_REGEX = "\\/\\/.*?\\/";

    private static final int ACAD_YEAR_INDEX = 4;
    private static final int MODULE_INFO_INDEX = 5;

    /**
     * Main function for testing
     */
    public static void main(String[] args) {
        String url = "http://modsn.us/WpFGQ";

        try {
            parse(url);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Checks if a url is a valid url from NUSMods, and expands it if a shortened url is given
     * Passes expanded url to parseLongUrl to be parsed for module information
     */
    public static Timetable parse(String url) throws ParseException {

        HashMap<String, HashMap<String, String>> modules = new HashMap<>();
        Matcher matcher = Pattern.compile(URL_HOST_REGEX).matcher(url);
        if (!matcher.find()) {
            throw new ParseException(MESSAGE_INVALID_TIMETABLE_URL);
        }

        String hostName = matcher.group()
                                 .substring(2, matcher.group().length() - 1);

        if (hostName.equals(NUSMODS_SHORT)) {
            try {
                url = expandUrl(url);
            } catch (IOException e) {
                System.out.println(e);
            }
        } else if (!hostName.equals(NUSMODS_HOST)) {
            throw new ParseException(MESSAGE_INVALID_TIMETABLE_URL);
        }

        parseLongUrl(url);
        return null;
    }

    /**
     *
     */
    private static void parseLongUrl(String url) {
        String acadYear;
        String semester;

        String[] splitUrl = url.split("/");

        acadYear = splitUrl[ACAD_YEAR_INDEX];
        String toParse = splitUrl[MODULE_INFO_INDEX];
        String[] moduleInfo = toParse.split("\\?");
        semester = moduleInfo[0].substring(3);

        ArrayList<Module> modules = parseModuleInfo(moduleInfo[1]);

        for (Module moduleToGet : modules) {
            ArrayList<HashMap<String, String>> classInfo = getModuleInfo(acadYear, semester,
                                                                            moduleToGet.getModuleCode());

            // get timings for each class and add to something
        }
    }

    /**
     * Uses NUSMods API to obtain all classes a module has, and returns it in
     * an arraylist of classes. Each class is represented by a hash map, storing the information about the class
     */
    private static ArrayList<HashMap<String, String>> getModuleInfo(String acadYear,
                                                                    String semester, String moduleCode) {
        String uri = "http://api.nusmods.com/" + acadYear + "/" + semester + "/modules/" + moduleCode + ".json";
        ObjectMapper mapper = new ObjectMapper();

        try {
            URL url = new URL(uri);
            Map<String, Object> mappedJson = mapper.readValue(url, HashMap.class);

            return (ArrayList<HashMap<String, String>>) mappedJson.get("Timetable");
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Parses a string of module info from an expanded NUSMods link
     * Returns an arraylist of Module objects
     */
    private static ArrayList<Module> parseModuleInfo(String modInfo) {
        ArrayList<Module> modules = new ArrayList<>();
        String[] classes = modInfo.split("&");

        for (String classInfo : classes) {
            String moduleCode = classInfo.split("%5B")[0];
            String classType = classInfo.split("%5B")[1].split("%5D")[0];
            String classValues = classInfo.split("=")[1];

            Module module = new Module(moduleCode);

            if (!modules.contains(module)) {
                module.addClassSlot(classType, classValues);
                modules.add(module);
            } else {
                int index = modules.indexOf(module);
                Module oldModuleInfo = modules.get(index);
                oldModuleInfo.addClassSlot(classType, classValues);
                modules.set(index, oldModuleInfo);
            }
        }

        return modules;
    }

    /**
     * Takes a shortened URL and returns the full length URL as a string
     */
    private static String expandUrl(String shortenedUrl) throws IOException {
        URL url = new URL(shortenedUrl);
        // open connection
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);

        // stop following browser redirect
        httpUrlConnection.setInstanceFollowRedirects(false);

        // extract location header containing the actual destination URL
        String expandedUrl = httpUrlConnection.getHeaderField("Location");
        httpUrlConnection.disconnect();

        return expandedUrl;
    }
}
