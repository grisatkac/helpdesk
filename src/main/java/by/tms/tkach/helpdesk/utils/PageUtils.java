package by.tms.tkach.helpdesk.utils;

import java.util.HashMap;
import java.util.Map;

public final class PageUtils {

    private PageUtils() {}

    public static Map<String, String> init() {
        Map<String, String> links = new HashMap<>();

        links.put("assignable", "/tasks/all/assignable");
        links.put("executable", "/tasks/all/executable");

        return links;
    }

    public static String findLinkByFilter(String filer) {
        Map<String, String> links = init();
        return links.get(filer);
    }
}
