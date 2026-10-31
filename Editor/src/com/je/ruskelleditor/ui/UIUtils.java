package com.je.ruskelleditor.ui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

public class UIUtils {
    public static final UIUtils instance = new UIUtils();

    private UIUtils(){}

    private void applyDarkMode(Component component) {
        ColorManager.instance.restyle(component);
        if (component instanceof JComponent jComponent) {
            try {
                jComponent.setBorder(ColorManager.instance.getBorder());
            } catch (IllegalArgumentException iae) {
                // ignore
            }
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyDarkMode(child);
            }
        }
    }

    private JFileChooser createFileChooser(final Consumer<File> consumer) {
        JFileChooser jfc = new JFileChooser(){
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                consumer.accept(f);
                super.approveSelection();
            }
        };
        applyDarkMode(jfc);
        return jfc;
    }

    public void getDirectory(Consumer<File> consumer, Component parent) {
        JFileChooser chooser = createFileChooser(consumer);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Ruskell Editor - Choose Directory.");
        ColorManager.instance.restyle(chooser);
        chooser.showDialog(parent, "Select");
    }

    public static void showException(Exception exception, Component parent) {
        JOptionPane.showMessageDialog(
                parent,
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                JOptionPane.ERROR_MESSAGE
        );
    }
}
