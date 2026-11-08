package com.je.ruskelleditor.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import com.je.ruskelleditor.utils.*;
import java.io.IOException;

final class CodeEditor extends JTextPane implements FileLoader.Target {
    private final HighlightingManager mHighlightManager = new HighlightingManager();

    public CodeEditor() {
        ColorManager.instance.addComponent(this);
        addKeyListener(new EditorKeyListener());
    }

    @Override
    public void append(String text) {
        setText(getText() + text + '\n');
    }

    public void highlight() {
        mHighlightManager.highlight();
    }

    private final class HighlightingManager {
        private final java.util.List<String> mKeywords = new ArrayList<>();

        public HighlightingManager() {
            loadKeywords();
        }

        private void loadKeywords() {
            FileLoader.Target target = line -> {
                String[] keywords = line.split("///");
                if(keywords.length >= 1) {
                    mKeywords.add(keywords[0]);
                }
            };
            try {
                FileLoader.instance.loadFile(getClass().getResourceAsStream("/keywords"), target);
            } catch (IOException e) {
                UIUtils.showException(e, CodeEditor.this);
            }
        }

        private void highlight() {
            // Strings
            for(Integer[] pair: Utils.findStrings(getText())) {
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setForeground(sas, ColorManager.instance.getStringsHighlightColor());
                getStyledDocument().setCharacterAttributes(pair[0], pair[1] - pair[0], sas, false);
            }
            // Names
            for(Integer[] pair: Utils.findNames(getText())) {
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setForeground(sas, ColorManager.instance.getNameHighlightColor());
                getStyledDocument().setCharacterAttributes(pair[0], pair[1] - pair[0], sas, false);
            }
            // Keywords
            for(String keyword: mKeywords) {
                List<Integer[]> locations = Utils.findWords(getText(), keyword);
                for(Integer[] location: locations) {
                    SimpleAttributeSet sas = new SimpleAttributeSet();
                    StyleConstants.setForeground(sas, ColorManager.instance.getHighlightColor());
                    getStyledDocument().setCharacterAttributes(location[0], location[1] - location[0], sas, false);
                }
            }
        }
    }

    private class EditorKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            CodeEditor.this.highlight();
        }
    }
}