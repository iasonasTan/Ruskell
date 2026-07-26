mod transpiler {
    use std::io::{Write, BufWriter};
    use std::fs::{File, read_to_string};
    use std::error::Error;
    use std::result::Result;

    pub fn process(input: &String, output: &String) -> Result<(), Box<dyn Error>> {
        let code = read_to_string(input.clone())?;
        let file = File::create(output)?;
        let mut writer: BufWriter<File> = BufWriter::new(file);
        for line in code.split("\n") {
            let parsed_line: String = parse_line(String::from(line));
            writeln!(writer, "{}", parsed_line).expect("Fatal Error: Could not write in output file.");
        }
        Ok(())
    }

    fn parse_line(line: String) -> String {
        let quotes_open: String = String::from("циτ");
        let quotes_close: String= String::from("конциτ");

        println!("(Transpiling: {}...)", line);
        if line.contains(&quotes_open as &str) && line.contains(&quotes_close as &str) {
            // Line contains string
            let start: usize = line.find(&quotes_open).expect("Internal Fatal Error.");
            let end: usize = line.find(&quotes_close).expect("Internal Fatal Error.");

            // Extract non-string parts
            let before: &str = &line[0..start];
            let after: &str = &line[end+quotes_close.len()..line.len()];

            // Extract string
            let string: &str = &line[start+quotes_open.len()+1..end-1]; // +-1 is for the space before and after циτ/конциτ

            let parsed_before: String = parse(&before);
            let parsed_after: String = parse(&after);

            return parsed_before + "\"" + &string + "\"" + &parsed_after;
        }

        parse(&line)
    }

    fn parse(line: &str) -> String {
        let output: &str = &line
            .replace(" импорт ", " import ")
            .replace(" открыτο ", " hiding ")
            .replace(" является ", " :: ")
            .replace(" равно ", " = ")
            .replace(" делать ", " do ") // With space
            .replace(" делать", " do ") // Without space
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
            .replace(" конско ", " ) ") // With space
            .replace(" конско", " ) ") // Without space
            .replace(" равный ", " == ")
            .replace(" ско ", " ( ");
        String::from(output)
    }
}

fn main() {
    let args: Vec<String> = std::env::args().collect();

    let mut input: String = String::new();
    let mut output: String = String::new();

    if args.len() > 1 {
        input = String::from(args[1].clone());
    } else {
        fatal(String::from("input"));
    }

    if args.len() > 2 {
        output = String::from(args[2].clone());
    } else {
        fatal(String::from("output"));
    }

    match transpiler::process(&input, &output) {
        Ok(_) => println!("Code transpiled successfuly!"),
        Err(err) => println!("Error transpiling code: {}", err),
    };
}

fn fatal(missing: String) {
    println!("Fatal Error: You must specify {} file.", missing);
    println!("Usage: ./ruskell <input.rhs> <output.hs>");
    std::process::exit(1);
}
