mod transpiler {
    pub fn transpile(input: &String) -> String {
        let mut out: String = String::new();
        match std::fs::read_to_string(input.clone()) {
            Ok(code) => {
                let mut transpiled: String = String::new();
                for line in code.lines() {
                    let parsed_line: String = parse_line(String::from(line));
                    transpiled.push_str(&parsed_line);
                    transpiled.push_str("\n");
                }
                out = transpiled.clone();
            },
            Err(err) => {
                println!("Error while reading file: {}.", err);
            },
        };
        out
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

mod runner {
    use std::process::Command;

    pub fn run(file: &String) {
        let status = Command::new("runghc")
            .arg(file)
            .status()
            .expect("Failed to run  program.");
        println!("\nExit code: {}", status);
    }
}

mod saver {
    use std::io::{Write, BufWriter};
    use std::fs::File;
    use std::path::MAIN_SEPARATOR_STR;
    use std::path::Path;

    pub fn save(code: &String, output: &String) {
        match File::create(output) {
            Ok(file) => {
                let mut writer: BufWriter<File> = BufWriter::new(file);
                writeln!(writer, "{}", code).expect("Error while trying to write code.");
            },
            Err(err) => {
                println!("Error openning file: {}", err);
            },
        };
    }

    pub fn prepare(dir: &String) {
        if !Path::new(dir).exists() {
            std::fs::create_dir(&dir).expect("Could not create temp folder!");
        }
    }

    pub fn path(items: &[String]) -> String {
        let mut out: String = String::new();
        for item in items {
            out.push_str(&item);
            out.push_str(MAIN_SEPARATOR_STR);
        }
        out.pop();
        out
    }
}

fn main() {
    let args: Vec<String> = std::env::args().collect();
    
    // Parameters constants
    let param_run: String = String::from("run");
    let param_save: String= String::from("save");

    // File IO constants
    let temp_dir: String = String::from(".temp");
    let temp_file: String = String::from("transpiled.temp.hs");

    if !(args.len() > 3) {
        println!("Usage: ./ruskell <mode> <input.rhs> <output.hs>");
        println!(
            concat!("Modes: '{}' just transpiles and runs without saving the file, '{}' saves the file ",
                "without running it. (Hint: You can use both parameters like this: '{}')"),
            &param_run, &param_save, &format!("{}{}", param_run, param_save)
        );
        std::process::exit(1);
    }

    let params: String = String::from(args[1].clone());
    let input: String = String::from(args[2].clone());
    let output: String = String::from(args[3].clone());

    // Transpile code
    let code: String = transpiler::transpile(&input);

    saver::prepare(&temp_dir);

    // Save temp file
    let path: String = saver::path(&[temp_dir, temp_file]);
    saver::save(&code, &path);

    // Save in output if user wants to
    if params.contains(&param_save) {
        saver::save(&code, &output);
    }

    // Run generated haskell code if user wants to
    if params.contains(&param_run) {
        runner::run(&path);
    }
}