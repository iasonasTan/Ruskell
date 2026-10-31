package com.je.ruskelleditor.ui;

import com.je.io.configuration.Configuration;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

final class ColorManager {
    public static final ColorManager instance = new ColorManager();

    static final String THEME_SETTINGS = "theme.settings";
    static final String DARK_THEME     = "dark_theme";

    private static final Color HIGHLIGHT_COLOR_DARK = new Color(138, 123, 0),
            HIGHLIGHT_COLOR_LIGHT = new Color(118, 113, 30);
    private static final Color BACKGROUND_COLOR_DARK = new Color(26, 26, 26),
            BACKGROUND_COLOR_LIGHT = new Color(206, 206, 206);
    private static final Color FOREGROUND_COLOR_DARK = new Color(255, 255, 255),
            FOREGROUND_COLOR_LIGHT = new Color(0, 0, 0);

    private boolean mDarkTheme = Configuration.loadBundle(THEME_SETTINGS).getBoolean(DARK_THEME, false);;
    private final List<Component> mComponents = new ArrayList<>();

    private ColorManager() {
    }

    public void addComponent(Component component) {
        mComponents.add(component);
        restyle(component);
    }

    private void restyleAll() {
        for(Component component: mComponents) {
            restyle(component);
        }
    }

    public void restyle(Component component) {
        component.setForeground(mDarkTheme ? FOREGROUND_COLOR_DARK : FOREGROUND_COLOR_LIGHT);
        component.setBackground(mDarkTheme ? BACKGROUND_COLOR_DARK : BACKGROUND_COLOR_LIGHT);
    }

    public Color getHighlightColor() {
        return mDarkTheme ? HIGHLIGHT_COLOR_DARK : HIGHLIGHT_COLOR_LIGHT;
    }

    public void setDarkTheme(boolean dt) {
        mDarkTheme = dt;
        restyleAll();
    }

    public boolean isDarkTheme() {
        return mDarkTheme;
    }

    public Border getBorder() {
        return BorderFactory.createLineBorder(mDarkTheme ? BACKGROUND_COLOR_DARK : BACKGROUND_COLOR_LIGHT, 2, false);
    }
}
