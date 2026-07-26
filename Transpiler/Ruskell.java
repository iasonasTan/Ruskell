import java.io.*;
import java.util.*;

public class Ruskell {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Ruskell <input.rhs> <output.hs>");
            return;
        }

        File input  = new File(args[0]);
        File output = new File(args[1]);

        try (Transpiler transpiler = new Transpiler(output)) {
            transpiler.transpile(input);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

        System.out.println("Ruskell: Transpilation done!");
    }

    private static final class Transpiler implements Closeable {
        private final StringBuilder codeBuilder = new StringBuilder();
        private final File output;

        Transpiler(File output) {
            this.output = output;
            if(!output.exists() || !output.isFile()) {
                var exception = new IOException("Fatal Error: Given output file does not exist or it's a directory.");
                throw new UncheckedIOException(exception);
            }
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
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                writer.write(codeBuilder.toString());
            }
        }
    }
}