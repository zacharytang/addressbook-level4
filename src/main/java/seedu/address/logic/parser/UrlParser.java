package seedu.address.logic.parser;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.model.person.Module;

/**
 * Helper class to parse NUSMods urls.
 */
public class UrlParser {

    private static final int ACAD_YEAR_INDEX = 4;
    private static final int MODULE_INFO_INDEX = 5;

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
    public static String expandUrl(String shortenedUrl) throws IOException {
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
