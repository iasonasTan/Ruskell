package com.je.ruskelleditor.ui;

import javax.swing.JComponent;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

final class ColorManager {
    private static final Color HIGHLIGHT_COLOR_DARK = new Color(238, 223, 0),
            HIGHLIGHT_COLOR_LIGHT = new Color(118, 113, 30);
    private static final Color BACKGROUND_COLOR_DARK = new Color(26, 26, 26),
            BACKGROUND_COLOR_LIGHT = new Color(206, 206, 206);
    private static final Color FOREGROUND_COLOR_DARK = new Color(255, 255, 255),
            FOREGROUND_COLOR_LIGHT = new Color(0, 0, 0);

    private boolean mDarkTheme, mDarkThemeBefore;
    private List<JComponent> mComponents = new ArrayList<>();

    public ColorManager() {
    }

    public void addComponent(JComponent component) {
        mComponents.add(component);
        if(mDarkThemeBefore != mDarkTheme) {
            mDarkThemeBefore = mDarkTheme;
            restyleAll();
        }
    }

    private void restyleAll() {
        for(JComponent component: mComponents) {
            component.setForeground(mDarkTheme ? FOREGROUND_COLOR_DARK : FOREGROUND_COLOR_LIGHT);
            component.setBackground(mDarkTheme ? BACKGROUND_COLOR_DARK : BACKGROUND_COLOR_LIGHT);
        }
    }

    public Color getHighlightColor() {
        return mDarkTheme ? HIGHLIGHT_COLOR_DARK : HIGHLIGHT_COLOR_LIGHT;
    }

    public void setDarkTheme(boolean dt) {
        mDarkTheme = dt;
        restyleAll();
    }
}
