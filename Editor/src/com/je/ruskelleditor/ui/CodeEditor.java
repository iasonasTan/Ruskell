package com.je.ruskelleditor.ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import com.je.ruskelleditor.utils.*;
import java.io.IOException;

final class CodeEditor extends JTextArea implements FileLoader.Target {
    private final HighlightingManager mHighlightManager = new HighlightingManager();

    public CodeEditor() {
        ColorManager.instance.addComponent(this);
        addKeyListener(new EditorKeyListener());
    }

    @Override
    public void append(String text) {
        super.append(text+'\n');
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

        public void highlight() {
            getHighlighter().removeAllHighlights();
            highlightKeywords();
            highlightNames();
            highlightStrings();
        }

        private void highlightStrings() {
            var stringsHighlighter = new DefaultHighlighter.DefaultHighlightPainter(ColorManager.instance.getStringsHighlightColor());
            for(Integer[] pair: Utils.findStrings(getText())) {
                try {
                    getHighlighter().addHighlight(pair[0], pair[1], stringsHighlighter);
                } catch (BadLocationException e) {
                    // ignore
                }
            }
        }

        private void highlightNames() {
            var namesHighlighter = new DefaultHighlighter.DefaultHighlightPainter(ColorManager.instance.getNameHighlightColor());
            for(Integer[] pair: Utils.findNames(getText())) {
                try {
                    getHighlighter().addHighlight(pair[0], pair[1], namesHighlighter);
                } catch (BadLocationException e) {
                    // ignore
                }
            }
        }

        private void highlightKeywords() {
            var keywordsHighlighter = new DefaultHighlighter.DefaultHighlightPainter(ColorManager.instance.getHighlightColor());
            for(String keyword: mKeywords) {
                List<Integer[]> locations = Utils.findWords(getText(), keyword);
                try {
                    for(Integer[] location: locations) {
                        getHighlighter().addHighlight(location[0], location[1], keywordsHighlighter);
                    }
                } catch (BadLocationException e) {
                    // ignore
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