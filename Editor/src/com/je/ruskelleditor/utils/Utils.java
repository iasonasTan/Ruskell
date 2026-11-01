package com.je.ruskelleditor.utils;

import com.je.core.JeLib;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Utils {
    public static final String KEYWORD_EQUALS = " равно ";
    public static final String KEYWORD_STR_OPEN = " цит ";
    public static final String KEYWORD_STR_CLOSE = " концит";

    public static java.util.List<Integer[]> findWords(String str, String query) {
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

        for (String line : str.lines().toList()) {
            if (line.contains(KEYWORD_EQUALS)) {
                StringBuilder wordBuilder = new StringBuilder();
                boolean haveFoundLetters = false;
                for (char c : line.toCharArray()) {
                    if (c != ' ') haveFoundLetters = true;
                    if (haveFoundLetters && c == ' ') break;
                    if (haveFoundLetters) wordBuilder.append(c);
                }
                names.add(wordBuilder.toString());
            }
        }

        List<Integer[]> locations = new ArrayList<>();
        for (String name : names) {
            locations.addAll(findWords(str, name));
        }
        return Collections.unmodifiableList(locations);
    }

    public static List<Integer[]> findStrings(String str) {
        List<Integer[]> locations = new ArrayList<>();

        int index = 0;
        while ((index = str.indexOf(KEYWORD_STR_OPEN, index)) != -1) {
            int open = index + KEYWORD_STR_OPEN.length();
            int close = str.indexOf(KEYWORD_STR_CLOSE, open);
            if (close == -1) continue;
            locations.add(new Integer[]{open, close});
            index = close + KEYWORD_STR_CLOSE.length();
        }

        return Collections.unmodifiableList(locations);
    }

    private Utils() {}
}
