package com.je.ruskelleditor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Utils {
    public static void showException(Exception exception, Component parent) {
        JOptionPane.showMessageDialog(
                parent,
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                JOptionPane.ERROR_MESSAGE
        );
    }

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
}
