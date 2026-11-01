package com.je.ruskelleditor.ui;

import com.je.core.JeLib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

class TerminalPanel extends JPanel {
    private static final String OUTPUT_FILE_PATH = ".temp/program.hs";

    public static void show(File openedFile) {
        TerminalPanel panel = new TerminalPanel();

        JFrame frame = new JFrame(openedFile.getName() + " - Ruskell Running");
        panel.attachToWindow(frame);
        frame.setContentPane(panel);
        frame.setPreferredSize(new Dimension(1000, 100));
        frame.setSize(1000, 800);
        frame.setVisible(true);

        try (Process psb = new ProcessBuilder("./ruskell", "save", openedFile.getAbsolutePath(), OUTPUT_FILE_PATH).start()) {
            psb.waitFor();
            int exitCode = psb.exitValue();
            String output = new String(psb.getInputStream().readAllBytes());
            if(exitCode != 0) {
                JOptionPane.showMessageDialog(frame, "Transpiler exit code: " + exitCode);
                JOptionPane.showMessageDialog(frame, output);
            }
            JeLib.console().log("Transpiler output: " + output);
            panel.startProgram(OUTPUT_FILE_PATH);
        } catch (IOException | InterruptedException e) {
            UIUtils.showException(e, frame);
        }
    }

    private final JTextArea mOutput;
    private final JTextField mInput;
    private Process mProgram;
    private BufferedWriter mProgramInput;

    public TerminalPanel() {
        setLayout(new BorderLayout());
        ComponentFactory factory = new ComponentFactory();

        mOutput = factory.createMainTextArea();
        add(new JScrollPane(mOutput), BorderLayout.CENTER);
        mInput = factory.createUserOutput();
        add(mInput, BorderLayout.SOUTH);
    }

    public void attachToWindow(JFrame jframe) {
        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (mProgram != null && mProgram.isAlive()) {
                    mProgram.destroy();
                }
            }
        });
    }

    public void startProgram(String programPath) throws IOException {
        ProcessBuilder psb = new ProcessBuilder("runghc", programPath);
        psb.redirectErrorStream(true);
        mProgram = psb.start();

        mProgramInput = new BufferedWriter(new OutputStreamWriter(mProgram.getOutputStream()));

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(mProgram.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String finalLine = line;
                    SwingUtilities.invokeLater(() -> {
                        mOutput.append(finalLine + "\n");
                        mOutput.setCaretPosition(mOutput.getDocument().getLength()); // auto-scroll
                    });
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> mOutput.append("\n[Error reading process output]\n"));
            }
        }).start();
    }

    final class ComponentFactory {
        JTextArea createMainTextArea() {
            JTextArea output = new JTextArea();
            ColorManager.instance.addComponent(output);
            output.setCaretColor(Color.WHITE);
            output.setFont(new Font("Monospaced", Font.PLAIN, 13));
            output.setEditable(false);
            return output;
        }

        JTextField createUserOutput() {
            JTextField input = new JTextField();
            ColorManager.instance.addComponent(input);
            input.setCaretColor(Color.WHITE);
            input.setFont(new Font("Monospaced", Font.PLAIN, 13));
            input.addActionListener(_ -> {
                String text = mInput.getText() + "\n";
                try {
                    mOutput.append("> " + text);
                    mProgramInput.write(text);
                    mProgramInput.flush();
                } catch (IOException e) {
                    // noinspection all
                    JeLib.console().exception(e);
                    JeLib.console().error("Could not write to stream.");
                }
                mInput.setText("");
            });
            return input;
        }
    }
}
