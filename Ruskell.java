import java.io.*;
import java.util.*;

public class Ruskell {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Ruskell <input.rhs> <output.hs>");
            return;
        }

        String input = args[0];
        String output = args[1];
        StringBuilder codeBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(input)))) {
            String ruskellLine;
            while ((ruskellLine = reader.readLine()) != null) {
                codeBuilder.append(parseLine(ruskellLine)).append("\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(output)))) {
            writer.write(codeBuilder.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        System.out.println("Ruskell: Μετατροπή ολοκληρώθηκε!");
    }

    public static String parseLine(String line) {
        if (!line.contains("циτ")) {
            return parseCode(line);
        }

        int start = line.indexOf("циτ");
        int end = line.indexOf("конциτ");

        if (end == -1) return parseCode(line);

        String before = line.substring(0, start);
        String inside = line.substring(start + 3, end);
        String after = line.substring(end + 6);
        return parseCode(before) + "\"" + inside + "\"" + parseLine(after);
    }

    public static String parseCode(String code) {
        return code
            .replace("импорт", "import")
            .replace("открыτο", "hiding")
            .replace("является", "::")
            .replace("равно", "=")
            .replace("делать", "do")
            .replace("пусть", "let")
            .replace("если", "if")
            .replace("тогда", "then")
            .replace("иначе", "else")
            .replace("вывестиСтроку", "putStrLn")
            .replace("вывести", "putStr")
            .replace("плюс", "++")
            .replace("минус", "-")
            .replace("меньшеРавно", "<=")
            .replace("стрелка", "<-")
            .replace("и", "&&")
            .replace("или", "||")
            .replace("нет", "not")
            .replace("главная", "main")
            .replace("вернуть", "return")
            .replace("пуστοτα", "()");
    }
}