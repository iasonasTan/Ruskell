mod transpiler {
    use std::io::Write;

    pub fn process(input: &String, output: &String) {
        match std::fs::read_to_string(input.clone()) {
            Ok(code) => {
                match std::fs::File::create(output) {
                    Ok(file) => {
                        let mut writer: std::io::BufWriter<std::fs::File> = std::io::BufWriter::new(file);
                        for line in code.split("\n") {
                            let parsed_line: String = parse_line(String::from(line));
                            match writeln!(writer, "{}", parsed_line) {
                                Ok(_) => {

                                },
                                Err(_) => {
                                    println!("Fatal Error: Could not write in output file.");
                                },
                            };
                        }
                    },
                    Err(error) => {
                        println!("Fatal Error: Could not create output file: {}", error);
                    },
                };
            },
            Err(error) => {
                println!("Fatal Error: Could not read code: {}", error);
                std::process::exit(1);
            },
        };
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

    transpiler::process(&input, &output);
}

fn fatal(missing: String) {
    println!("Fatal Error: You must specify {} file.", missing);
    std::process::exit(1);
}