package com.je.ruskelleditor.ui;

import com.je.core.JeApplication;
import com.je.core.NotInitializedException;
import com.je.core.broadcast.Message;
import com.je.io.configuration.Configuration;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

final class ColorManager {
    private static ColorManager instance;

    static void initialize(JeApplication jeApplication) {
        instance = new ColorManager(jeApplication);
    }

    static ColorManager instance() {
        if(instance == null)
            throw new NotInitializedException("ColorManager is not initialized yet.");
        return instance;
    }

    private static final Color HIGHLIGHT_COLOR_DARK = new Color(138, 123, 0), HIGHLIGHT_COLOR_LIGHT = new Color(118, 113, 30);
    private static final Color BACKGROUND_COLOR_DARK = new Color(26, 26, 26), BACKGROUND_COLOR_LIGHT = new Color(206, 206, 206);
    private static final Color FOREGROUND_COLOR_DARK = new Color(255, 255, 255), FOREGROUND_COLOR_LIGHT = new Color(0, 0, 0);
    private static final Color HIGHLIGHT_NAMES_COLOR_DARK = new Color(84, 162, 192), HIGHLIGHT_NAMES_COLOR_LIGHT = new Color(17, 46, 80);
    private static final Color HIGHLIGHT_STRINGS_COLOR_DARK = new Color(120, 215, 164), HIGHLIGHT_STRINGS_COLOR_LIGHT = new Color(58, 96, 76);

    private boolean mDarkTheme = Configuration.loadBundle(GuiInitializer.THEME_SETTINGS).getBoolean(GuiInitializer.DARK_THEME, false);
    private final JeApplication mApplication;
    private final List<Component> mComponents = new ArrayList<>();

    private ColorManager(JeApplication jeApplication) {
        mApplication = jeApplication;
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
        Message message = Message.newBuilder()
                .setAction(GuiInitializer.THEME_SETTINGS)
                .putExtra(GuiInitializer.DARK_THEME, dt)
                .build();
        mApplication.broadcastMessage(message);
    }

    public boolean isDarkTheme() {
        return mDarkTheme;
    }

    public Border getBorder() {
        return BorderFactory.createLineBorder(mDarkTheme ? BACKGROUND_COLOR_DARK : BACKGROUND_COLOR_LIGHT, 2, false);
    }

    public Color getNameHighlightColor() {
        return mDarkTheme ? HIGHLIGHT_NAMES_COLOR_DARK : HIGHLIGHT_NAMES_COLOR_LIGHT;
    }

    public Color getStringsHighlightColor() {
        return mDarkTheme ? HIGHLIGHT_STRINGS_COLOR_DARK : HIGHLIGHT_STRINGS_COLOR_LIGHT;
    }
}
