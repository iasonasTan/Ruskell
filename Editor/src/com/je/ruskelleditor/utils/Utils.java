package com.je.ruskelleditor.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Utils {
    public static final String KEYWORD_EQUALS = " равно ";

    public static java.util.List<Integer[]> findWord(String str, String query) {
        java.util.List<Integer[]> locations = new ArrayList<>();

        // Check in the start of the string (0, ql)
        if(str.indexOf(query) == 0) {
            locations.add(new Integer[]{0, query.length()});
        }

        int index = 0;
        do {
            index = str.indexOf(query, index + query.length());
            locations.add(new Integer[]{index, index + query.length()});
        } while(index != -1);

        return Collections.unmodifiableList(locations);
    }

    public static java.util.List<Integer[]> findNames(String str) {
        List<String> names = new ArrayList<>();

        for(String line: str.lines().toList()) {
            if(line.contains(KEYWORD_EQUALS)) {
                StringBuilder wordBuilder = new StringBuilder();
                boolean haveFoundLetters = false;
                for(char c: line.toCharArray()) {
                    if(c != ' ') haveFoundLetters = true;
                    if(haveFoundLetters && c == ' ') break;
                    if(haveFoundLetters) wordBuilder.append(c);
                }
                names.add(wordBuilder.toString());
            }
        }

        List<Integer[]> locations = new ArrayList<>();
        for(String name: names) {
            locations.addAll(findWord(str, name));
        }
        return Collections.unmodifiableList(locations);
    }

    private Utils() {}
}
