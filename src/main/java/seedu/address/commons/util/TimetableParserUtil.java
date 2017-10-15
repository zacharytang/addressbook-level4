package seedu.address.commons.util;

import static seedu.address.model.person.timetable.Timetable.MESSAGE_TIMETABLE_URL_CONSTRAINTS;
import static seedu.address.model.person.timetable.Timetable.WEEK_EVEN;
import static seedu.address.model.person.timetable.Timetable.WEEK_ODD;
import static seedu.address.model.person.timetable.Timetable.isValidUrl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.timetable.Lesson;
import seedu.address.model.person.timetable.ModuleInfoFromUrl;
import seedu.address.model.person.timetable.TimetableInfoFromUrl;
import seedu.address.model.person.timetable.TimetableWeek;

/**
 * Helper class that contains utilities to parse NUSMods urls.
 */
public class TimetableParserUtil {

    public static final String MESSAGE_INVALID_SHORT_URL = "Invalid shortened URL provided";

    private static final int DAY_MONDAY = 0;
    private static final int DAY_TUESDAY = 1;
    private static final int DAY_WEDNESDAY = 2;
    private static final int DAY_THURSDAY = 3;
    private static final int DAY_FRIDAY = 4;

    private static final int INDEX_ACAD_YEAR = 4;
    private static final int INDEX_MODULE_INFO = 5;
    private static final int INDEX_SEMESTER_INFO = 0;
    private static final int INDEX_CLASS_INFO = 1;

    private static final int ARRAY_NUM_EVEN_ODD = 2;

    private static final String SPLIT_BACKWARDS_SLASH = "/";
    private static final String SPLIT_QUESTION_MARK = "\\?";
    private static final String SPLIT_AMPERSAND = "&";
    private static final String SPLIT_EQUALS_SIGN = "=";
    private static final String SPLIT_LEFT_SQAURE_BRACKET = "%5B";
    private static final String SPLIT_RIGHT_SQUARE_BRACKET = "%5D";

    /**
     * Takes in a valid timetable URL and attempts to parse it
     */
    public static TimetableWeek[] parseUrl(String url) throws ParseException {
        if (!isValidUrl(url)) {
            throw new ParseException(MESSAGE_TIMETABLE_URL_CONSTRAINTS);
        }

        try {
            String newUrl = expandUrl(url);
            return parseLongUrl(newUrl);
        } catch (IOException e) {
            throw new ParseException("Url cannot be accessed");
        }
    }

    /**
     * Parses a full expanded NUSMods url
     */
    private static TimetableWeek[] parseLongUrl(String url) throws ParseException {
        String acadYear;
        String semester;

        String[] splitUrl = url.split(SPLIT_BACKWARDS_SLASH);

        acadYear = splitUrl[INDEX_ACAD_YEAR];
        String toParse = splitUrl[INDEX_MODULE_INFO];
        String[] modInfo = toParse.split(SPLIT_QUESTION_MARK);

        if (modInfo.length != 2) {
            TimetableWeek[] emptyTimetable = new TimetableWeek[ARRAY_NUM_EVEN_ODD];
            initializeEmptyTimetable(emptyTimetable);
            return emptyTimetable;
        }

        semester = modInfo[INDEX_SEMESTER_INFO].substring(3);

        TimetableInfoFromUrl timetableInfo = parseModuleInfo(modInfo[INDEX_CLASS_INFO]);

        return constructTimetable(acadYear, semester, timetableInfo);
    }

    /**
     * Parses a string of module info from an expanded NUSMods link
     * Returns a hashmap where module code is the key, and values are a hashmap of class types and class numbers
     */
    private static TimetableInfoFromUrl parseModuleInfo(String modInfoFromString) {
        TimetableInfoFromUrl modules = new TimetableInfoFromUrl();
        String[] classes = modInfoFromString.split(SPLIT_AMPERSAND);

        // Split a string of format "MODULE_CODE[CLASS_TYPE]=CLASS_NO"
        for (String classInfo : classes) {
            String moduleCode = classInfo.split(SPLIT_LEFT_SQAURE_BRACKET)[0];
            String classType = classInfo.split(SPLIT_LEFT_SQAURE_BRACKET)[1].split(SPLIT_RIGHT_SQUARE_BRACKET)[0];
            String classNo = classInfo.split(SPLIT_EQUALS_SIGN)[1];

            ModuleInfoFromUrl moduleInfo = modules.getModuleInfo(moduleCode);
            moduleInfo.addLesson(classType, classNo);
            modules.addModuleInfo(moduleInfo);
        }

        return modules;
    }

    /**
     * Uses NUSMods API to obtain all classes a module has, and returns it in
     * an arraylist of classes. Each class is represented by a hash map, storing the information about the class
     */
    private static ArrayList<Lesson> getLessonInfoFromApi(String acadYear, String semester, String modCode)
            throws ParseException {
        String uri = "http://api.nusmods.com/" + acadYear + "/" + semester + "/modules/" + modCode + ".json";
        ObjectMapper mapper = new ObjectMapper();

        try {
            URL url = new URL(uri);
            @SuppressWarnings("unchecked")
            Map<String, Object> mappedJson = mapper.readValue(url, HashMap.class);
            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, String>> lessonInfo = (ArrayList<HashMap<String, String>>)
                    mappedJson.get("Timetable");

            ArrayList<Lesson> lessons = new ArrayList<>();
            for (HashMap<String, String> lesson : lessonInfo) {
                Lesson lessonToAdd = new Lesson(lesson.get("ClassNo"), lesson.get("LessonType"),
                        lesson.get("WeekText"), lesson.get("DayText"),
                        lesson.get("StartTime"), lesson.get("EndTime"));
                lessons.add(lessonToAdd);
            }

            return lessons;
        } catch (Exception e) {
            throw new ParseException("Cannot retrieve module information");
        }
    }

    /**
     * Takes a shortened URL and returns the full length URL as a string
     */
    private static String expandUrl(String shortenedUrl) throws IOException, ParseException {
        URL url = new URL(shortenedUrl);
        // open connection
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);

        // stop following browser redirect
        httpUrlConnection.setInstanceFollowRedirects(false);

        // extract location header containing the actual destination URL
        String expandedUrl = httpUrlConnection.getHeaderField("Location");
        httpUrlConnection.disconnect();

        if (expandedUrl.equals("http://modsn.us")) {
            throw new ParseException(MESSAGE_INVALID_SHORT_URL);
        }

        return expandedUrl;
    }

    /**
     * Constructs a timetable array using information parsed from a url
     * @param acadYear academic year
     * @param semester semester
     * @param timetableInfo Class information parsed from url, stored by module code
     */
    private static TimetableWeek[] constructTimetable(String acadYear, String semester,
                                                      TimetableInfoFromUrl timetableInfo) throws ParseException {
        TimetableWeek[] timetable = new TimetableWeek[ARRAY_NUM_EVEN_ODD];
        initializeEmptyTimetable(timetable);

        ArrayList<ModuleInfoFromUrl> lessonInfoByModules = timetableInfo.getModuleInfoList();

        for (ModuleInfoFromUrl moduleInfoFromTimetable : lessonInfoByModules) {
            ArrayList<Lesson> lessons = getLessonInfoFromApi(acadYear, semester, moduleInfoFromTimetable.getModCode());
            HashMap<String, String> lessonsForModule = moduleInfoFromTimetable.getLessonInfo();

            for (String classType : lessonsForModule.keySet()) {
                String classNo = lessonsForModule.get(classType);

                for (Lesson lesson : lessons) {
                    if (parseSlotType(classType).equals(lesson.getLessonType())
                            && classNo.equals(lesson.getClassNo())) {
                        addLessonToTimetableArray(lesson, timetable);
                    }
                }
            }
        }

        return timetable;
    }

    /**
     * Adds a lesson to the timetable array provided
     */
    private static void addLessonToTimetableArray(Lesson lesson, TimetableWeek[] timetable) {

        if (lesson.getWeekType().equals("Every Week")) {
            timetable[WEEK_ODD].updateSlotsWithLesson(lesson.getDay(), lesson.getStartTime(), lesson.getEndTime());
            timetable[WEEK_EVEN].updateSlotsWithLesson(lesson.getDay(), lesson.getStartTime(), lesson.getEndTime());
        } else {
            timetable[parseWeekType(lesson.getWeekType())]
                    .updateSlotsWithLesson(lesson.getDay(), lesson.getStartTime(), lesson.getEndTime());
        }
    }

    /**
     * Initialises the timetable array to be an empty timetable without lessons
     */
    private static void initializeEmptyTimetable(TimetableWeek[] timetableArray) {
        for (int i = 0; i < ARRAY_NUM_EVEN_ODD; i++) {
            timetableArray[i] = new TimetableWeek();
        }
    }

    /* ------------------------------------------- Helper methods ----------------------------------------------------*/

    /**
     * Converts string representing day of class to integer representation
     * Returns -1 if day cannot be found;
     */
    public static int parseDay(String day) {
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

    /**
     * Takes in String representing start/end timing of lessons, and returns respective index to be used for array
     */
    public static int parseStartEndTime(String timing) {
        return (int) Math.ceil(((Integer.parseInt(timing) - 800) * 2) / 100.0);
    }

    /**
     * Converts shortened slot type in URL to full slot-type string used in API
     */
    public static String parseSlotType(String slotType) {
        switch (slotType) {
        case "LEC":
            slotType = "Lecture";
            break;

        case "TUT":
            slotType = "Tutorial";
            break;

        case "LAB":
            slotType = "Laboratory";
            break;

        case "SEM":
            slotType = "Seminar-Style Module Class";
            break;

        case "SEC":
            slotType = "Sectional Teaching";
            break;

        case "REC":
            slotType = "Recitation";
            break;

        default:
            System.out.println("Cannot find slot type");
        }

        return slotType;
    }

    /**
     * Converts week type from string used in api to integer index for use in URL
     */
    public static int parseWeekType(String weekType) {
        return weekType.equals("Odd Week") ? WEEK_ODD : WEEK_EVEN;
    }
}
