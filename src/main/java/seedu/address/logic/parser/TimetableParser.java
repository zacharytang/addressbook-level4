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
import seedu.address.model.person.timetable.Lesson;
import seedu.address.model.person.timetable.ModuleInfoFromUrl;
import seedu.address.model.person.timetable.Timetable.HasLesson;
import seedu.address.model.person.timetable.TimetableInfoFromUrl;


/**
 * Helper class to parse NUSMods urls.
 */
public class TimetableParser {

    private static final int WEEK_ODD = 0;
    private static final int WEEK_EVEN = 1;

    private static final int DAY_MONDAY = 0;
    private static final int DAY_TUESDAY = 1;
    private static final int DAY_WEDNESDAY = 2;
    private static final int DAY_THURSDAY = 3;
    private static final int DAY_FRIDAY = 4;

    private static final int ACAD_YEAR_INDEX = 4;
    private static final int MODULE_INFO_INDEX = 5;

    private static final int ARRAY_NUM_EVEN_ODD = 2;
    private static final int ARRAY_NUM_DAYS = 5;
    private static final int ARRAY_NUM_TIMESLOTS = 32;

    /**
     * Takes in a valid timetable URL and attempts to parse it
     */
    public static HasLesson[][][] parseUrl(String url) throws ParseException {
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
    private static HasLesson[][][] parseLongUrl(String url) throws ParseException {
        String acadYear;
        String semester;

        String[] splitUrl = url.split("/");

        acadYear = splitUrl[ACAD_YEAR_INDEX];
        String toParse = splitUrl[MODULE_INFO_INDEX];
        String[] modInfo = toParse.split("\\?");
        semester = modInfo[0].substring(3);

        TimetableInfoFromUrl timetableInfo = parseModuleInfo(modInfo[1]);

        return constructTimetableArray(acadYear, semester, timetableInfo);
    }

    /**
     * Constructs a timetable array using information parsed from a url
     * @param acadYear academic year
     * @param semester semester
     * @param timetableInfo Class information parsed from url, stored by module code
     */
    private static HasLesson[][][] constructTimetableArray(String acadYear, String semester,
                                                         TimetableInfoFromUrl timetableInfo) throws ParseException {
        HasLesson[][][] timetable = new HasLesson[ARRAY_NUM_EVEN_ODD][ARRAY_NUM_DAYS][ARRAY_NUM_TIMESLOTS];
        initializeTimetableArray(timetable);

        ArrayList<ModuleInfoFromUrl> lessonInfoByModules = timetableInfo.getModuleInfoList();

        for (ModuleInfoFromUrl moduleInfoFromTimetable : lessonInfoByModules) {
            ArrayList<Lesson> lessons = getLessonInfoFromApi(acadYear, semester, moduleInfoFromTimetable.getModCode());
            HashMap<String, String> lessonsForModule = moduleInfoFromTimetable.getLessonInfo();

            for (String classType : lessonsForModule.keySet()) {
                String classNo = lessonsForModule.get(classType);

                for (Lesson lesson : lessons) {
                    if (convertSlotType(classType).equals(lesson.getLessonType()) && classNo.equals(lesson.getClassNo())) {
                        addLessonToTimetableArray(lesson, timetable);
                    }
                }
            }
        }

        return timetable;
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
            Map<String, Object> mappedJson = mapper.readValue(url, HashMap.class);

            ArrayList<Lesson> lessons = new ArrayList<>();
            ArrayList<HashMap<String, String>> lessonInfo = (ArrayList<HashMap<String, String>>)
                                                                mappedJson.get("Timetable");

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
     * Parses a string of module info from an expanded NUSMods link
     * Returns a hashmap where module code is the key, and values are a hashmap of class types and class numbers
     */
    private static TimetableInfoFromUrl parseModuleInfo(String modInfoFromString) {
        TimetableInfoFromUrl modules = new TimetableInfoFromUrl();
        String[] classes = modInfoFromString.split("&");

        for (String classInfo : classes) {
            String moduleCode = classInfo.split("%5B")[0];
            String classType = classInfo.split("%5B")[1].split("%5D")[0];
            String classNo = classInfo.split("=")[1];

            ModuleInfoFromUrl moduleInfo = modules.getModuleInfo(moduleCode);
            moduleInfo.addLesson(classType, classNo);
            modules.addModuleInfo(moduleInfo);
        }

        return modules;
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
            throw new ParseException("Invalid shortened URL provided");
        }

        return expandedUrl;
    }

    /**
     * Adds a lesson to the timetable array provided
     */
    private static void addLessonToTimetableArray(Lesson lesson, HasLesson[][][] timetableArray) {
        int startTimeIndex = convertStartEndTime(lesson.getStartTime());
        int endTimeIndex = convertStartEndTime(lesson.getEndTime());
        int dayIndex = convertDay(lesson.getDay());

        if (lesson.getWeekType().equals("Every Week")) {
            fillTimetableArray(WEEK_ODD, dayIndex, startTimeIndex, endTimeIndex, timetableArray);
            fillTimetableArray(WEEK_EVEN, dayIndex, startTimeIndex, endTimeIndex, timetableArray);
        } else {
            fillTimetableArray((lesson.getWeekType().equals("Odd Week") ? WEEK_ODD : WEEK_EVEN),
                    dayIndex, startTimeIndex, endTimeIndex, timetableArray );
        }
    }

    /**
     * Fills the timetable array using the indexes specified
     */
    private static void fillTimetableArray(int weekIndex, int dayIndex, int startTimeIndex,
                                           int endTimeIndex, HasLesson[][][] timetableArray) {
        for (int i = startTimeIndex; i < endTimeIndex; i++) {
            timetableArray[weekIndex][dayIndex][i] = HasLesson.hasLesson;
        }
    }

    /**
     * Initialises the timetable array to be an empty timetable without lessons
     */
    private static void initializeTimetableArray(HasLesson[][][] timetableArray) {
        for (int i = 0; i < ARRAY_NUM_EVEN_ODD; i++) {
            for (int j = 0; j < ARRAY_NUM_DAYS; j++) {
                for (int k = 0; k < ARRAY_NUM_TIMESLOTS; k++) {
                    timetableArray[i][j][k] = HasLesson.noLesson;
                }
            }
        }
    }

    /* ------------------------------------------- Helper methods ----------------------------------------------------*/

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

    /**
     * Converts shortened slot type in URL to full slot-type string used in API
     */
    private static String convertSlotType(String slotType) {
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
     * Takes in String representing start/end timing of lessons, and returns respective index to be used for array
     */
    private static int convertStartEndTime(String timing) {
        return (int) Math.ceil(((Integer.parseInt(timing) - 800) * 2) / 100.0);
    }
}
