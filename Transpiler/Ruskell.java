import java.io.*;
import java.util.*;

public class Ruskell {
    private static final String PARAM_RUN = "run";
    private static final String PARAM_SAVE= "save";

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Ruskell <mode> <input.rhs> <output.hs>");
            System.out.println("Modes: '"+PARAM_RUN+"' just transpiles and runs without saving the file," + 
                " '"+PARAM_SAVE+"' saves the file without running it. (Hint: You can use both parameters like this: '"+PARAM_RUN+PARAM_SAVE+"')");
            return;
        }

        final String params = args[0];
        final File input  = new File(args[1]);
        final File output = new File(args[2]);

        try (Transpiler transpiler = new Transpiler(params, output)) {
            transpiler.transpile(input);
        } catch (IOException ioe) {
            System.out.printf("Fatal Error: %s: %s\n", ioe.getClass().getSimpleName(), ioe.getMessage());
        }
    }

    private static final class Transpiler implements Closeable {
        private static final File TEMP_DIR = new File(".temp");

        static {
            if(!TEMP_DIR.exists()) {
                TEMP_DIR.mkdir();
            }
        }

        private final StringBuilder codeBuilder = new StringBuilder();
        private final File output;
        private final String params;

        Transpiler(String params, File output) {
            this.output = output;
            this.params = params;
        }

        public void transpile(File input) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
                String ruskellLine;
                while ((ruskellLine = reader.readLine()) != null) {
                    codeBuilder.append(parseLine(ruskellLine)).append("\n");
                }
            }
        }

        private String parseLine(String line) {
            final String QUOTES_OPEN = "циτ";
            final String QUOTES_CLOSE= "конциτ";

            if (!line.contains(QUOTES_OPEN) || !line.contains(QUOTES_CLOSE)) {
                return parseCode(line);
            }

            int start = line.indexOf(QUOTES_OPEN);
            int end = line.indexOf(QUOTES_CLOSE);

            String before = line.substring(0, start);
            String inside = line.substring(start+QUOTES_OPEN.length()+1, end-1); // +-1 is for spaces before/after циτ/конциτ
            String after = line.substring(end+QUOTES_CLOSE.length());
            return parseCode(before) + "\"" + inside + "\"" + parseLine(after);
        }

        private String parseCode(String line) {
            line += " "; // Append space so `replace` catches even keywords in the end of the line
            return line
                .replace(" импорт ", " import ")
                .replace(" открыτο ", " hiding ")
                .replace(" является ", " :: ")
                .replace(" равно ", " = ")
                .replace(" делать ", " do ")
                .replace(" пусть ", " let ")
                .replace(" если ", " if ")
                .replace(" тогда ", " then ")
                .replace(" иначе ", " else ")
                .replace(" вывестиСтроку ", " putStrLn ")
                .replace(" вывести ", " putStr ")
                .replace(" плюс ", " ++ ")
                .replace(" минус ", " - ")
                .replace(" меньшеРавно ", " <= ")
                .replace(" стрелка ", " <- ")
                .replace(" и ", " && ")
                .replace(" или ", " || ")
                .replace(" нет ", " not ")
                .replace("главная ", "main ") // Main function will always be typed in the start of the line
                .replace(" вернуть ", " return ")
                .replace(" умножать ", " * ")
                .replace(" покажи ", " print ")
                .replace(" конско ", " ) ")
                .replace(" равный ", " == ")
                .replace(" ско ", " ( ");
        }

        @Override
        public void close() throws IOException {
            System.out.println("Ruskell: Transpilation done!");

            final File tempFile = new File(TEMP_DIR, "transpiled.temp.hs");

            // Save temp file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(codeBuilder.toString());
            }

            // Save to specified output if user wants to 
            if (params.contains(PARAM_SAVE)) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                    writer.write(codeBuilder.toString());
                }
            }

            // Run if user wants to
            if (params.contains(PARAM_RUN)) {
                ProcessBuilder pb = new ProcessBuilder("runghc", tempFile.toString());
                Process process = pb.start();
                printProcessStatus(process);
                tempFile.delete();
                TEMP_DIR.delete();
            }
        }

        private void printProcessStatus(Process process) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                System.out.println("runghc output: ");
                System.out.println("```");
                String line = null;
                while((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println("```");
                int exitCode = process.waitFor();
                System.out.println("`runghc exit code: " + exitCode + "`");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}