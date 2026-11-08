package com.je.ruskelleditor.ui;

import com.je.core.NotInitializedException;
import java.util.ArrayList;
import com.je.core.util.Bundle;
import java.awt.Component;
import java.util.List;
import com.je.ruskelleditor.ui.GuiInitializer;
import com.je.io.configuration.Configuration;

final class FontManager {
    private static FontManager sInstance;

    static void initialize() {
        sInstance = new FontManager();
    }

    static FontManager instance() {
        if(sInstance == null) {
            throw new NotInitializedException("Util FontManager is not initialized yet.");
        }
        return sInstance;
    }

    private List<Component> mComponents = new ArrayList<>();
    private float mFontSize = (float)Configuration.loadBundle(GuiInitializer.FONT_SETTINGS).getDouble(GuiInitializer.BUNDLEE_FONT_SIZE, 14.0f);

    private FontManager() {
    }

    public void increaseFontSize() {
        mFontSize+=1.0f;
        Bundle bundle = Bundle.builder()
                .put(GuiInitializer.BUNDLEE_FONT_SIZE, (double)mFontSize)
                .build();
        Configuration.storeBundle(GuiInitializer.FONT_SETTINGS, bundle);
        restyleAll();
    }

    public void decreaseFontSize() {
        mFontSize-=1.0f;
        Bundle bundle = Bundle.builder()
                .put(GuiInitializer.BUNDLEE_FONT_SIZE, (double)mFontSize)
                .build();
        Configuration.storeBundle(GuiInitializer.FONT_SETTINGS, bundle);
        restyleAll();
    }

    public void addComponent(Component component) {
        mComponents.add(component);
        restyle(component);
    }

    private void restyle(Component component) {
        component.setFont(component.getFont().deriveFont(mFontSize));
    }

    public void restyleAll() {
        for(Component component: mComponents) {
            restyle(component);
        }
    }
}