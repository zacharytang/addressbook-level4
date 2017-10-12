package seedu.address.logic.parser;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Module;

/**
 * Helper class to parse NUSMods urls.
 */
public class TimetableParser {

    private static final int DAY_MONDAY = 1;
    private static final int DAY_TUESDAY = 2;
    private static final int DAY_WEDNESDAY = 3;
    private static final int DAY_THURSDAY = 4;
    private static final int DAY_FRIDAY = 5;

    private static final int ACAD_YEAR_INDEX = 4;
    private static final int MODULE_INFO_INDEX = 5;

    /**
     * Tester main method
     */
    public static void main(String[] args) {
        String url = "http://modsn.us/WpFGQ";
        try {
            parseUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in a valid timetable URL and attempts to parse it
     */
    public static void parseUrl(String url) throws ParseException {
        try {
            String newUrl = expandUrl(url);
            parseLongUrl(newUrl);
        } catch (IOException e) {
            throw new ParseException("Url cannot be parsed");
        }
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
        String[] modInfo = toParse.split("\\?");
        semester = modInfo[0].substring(3);

        HashMap<String, HashMap<String, String>> mods = parseModuleInfo(modInfo[1]);

        for (String modCode : mods.keySet()) {
            Module mod = getModuleInfo(acadYear, semester, modCode);
            HashMap classInfo = mods.get(modCode);

            // get timings for each class and add to something
        }
    }

    /**
     * Uses NUSMods API to obtain all classes a module has, and returns it in
     * an arraylist of classes. Each class is represented by a hash map, storing the information about the class
     */
    private static Module getModuleInfo(String acadYear, String semester, String modCode) {
        String uri = "http://api.nusmods.com/" + acadYear + "/" + semester + "/modules/" + modCode + ".json";
        ObjectMapper mapper = new ObjectMapper();

        try {
            URL url = new URL(uri);
            Map<String, Object> mappedJson = mapper.readValue(url, HashMap.class);

            Module modToReturn = new Module(modCode);
            ArrayList<HashMap<String, String>> lessonInfo = (ArrayList<HashMap<String, String>>)
                                                                mappedJson.get("Timetable");

            for (HashMap<String, String> lesson : lessonInfo) {
                Lesson lessonToAdd = new Lesson(lesson.get("ClassNo"), lesson.get("LessonType"),
                                                lesson.get("WeekText"), convertDay(lesson.get("DayText")),
                                                lesson.get("StartTime"), lesson.get("EndTime"));
                modToReturn.addLesson(lessonToAdd);
            }

            return modToReturn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses a string of module info from an expanded NUSMods link
     * Returns an arraylist of Module objects
     */
    private static HashMap<String, HashMap<String, String>> parseModuleInfo(String modInfo) {
        HashMap<String, HashMap<String, String>> modules = new HashMap<>();
        String[] classes = modInfo.split("&");

        for (String classInfo : classes) {
            String modCode = classInfo.split("%5B")[0];
            String classType = classInfo.split("%5B")[1].split("%5D")[0];
            String classNo = classInfo.split("=")[1];

            if (!modules.containsKey(modCode)) {
                HashMap<String, String> modClasses = new HashMap<>();
                modClasses.put(classType, classNo);
                modules.put(modCode, modClasses);
            } else {
                HashMap<String, String> modClasses = modules.get(modCode);
                modClasses.put(classType, classNo);
                modules.put(modCode, modClasses);
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

    /**
     * Converts string representing day of class to integer representation
     * Returns -1 if day cannot be found;
     */
    private static int convertDay(String day) {
        int index = -1;

        switch (day) {
        case "Monday":
            index = DAY_MONDAY;
            break;

        case "Tuesday":
            index = DAY_TUESDAY;
            break;

        case "Wednesday":
            index = DAY_WEDNESDAY;
            break;

        case "Thursday":
            index = DAY_THURSDAY;
            break;

        case "Friday":
            index = DAY_FRIDAY;
            break;

        default:
            System.out.println("Unable to find day");
        }

        return index;
    }
}
