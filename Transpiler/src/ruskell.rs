mod transpiler {
    const KEYWORDS_RAW: &str = include_str!("../../Keywords/keywords");

    pub fn transpile(input: &String) -> String {
        let mut keywords: Vec<(String, String)> = Vec::new();
        load_keywords(&mut keywords);

        let mut out: String = String::new();
        match std::fs::read_to_string(input.clone()) {
            Ok(code) => {
                let mut transpiled: String = String::new();
                for line in code.lines() {
                    let parsed_line: String = parse_line(String::from(line), &keywords);
                    transpiled.push_str(&parsed_line);
                    transpiled.push_str("\n");
                }
                out = transpiled.clone();
            }
            Err(err) => {
                println!("Error while reading code file: {}.", err);
            }
        };
        out
    }

    fn load_keywords(keywords: &mut Vec<(String, String)>) {
        for line in KEYWORDS_RAW.lines() {
            if let Some((key, value)) = line.split_once("///") {
                keywords.push((key.to_string(), value.to_string()));
            }
        }
        keywords.sort_by_key(|(key, _)| std::cmp::Reverse(key.len()));
    }

    fn parse_line(line: String, keywords: &Vec<(String, String)>) -> String {
        let quotes_open: String = String::from("цит");
        let quotes_close: String = String::from("концит");

        print!("Transpiling: (`{}` => ", line);
        if line.contains(&quotes_open as &str) && line.contains(&quotes_close as &str) {
            // Line contains string
            let start: usize = line.find(&quotes_open).expect("Internal Fatal Error.");
            let end: usize = line.find(&quotes_close).expect("Internal Fatal Error.");

            // Extract non-string parts
            let left_code: &str = &line[0..start];
            let right_code: &str = &line[end + quotes_close.len()..line.len()];

            // Extract string
            // +-1 is for the space before and after циτ/конциτ
            let string: &str = &line[start + quotes_open.len() + 1..end - 1];

            let parsed_left_code: String = parse(&left_code, keywords);
            let parsed_right_code: String = parse(&right_code, keywords);

            return parsed_left_code + "\"" + &string + "\"" + &parsed_right_code;
        }
        let line = parse(&line, keywords);
        println!("`{}`)", line);
        return line;
    }

    fn parse(line: &str, keywords: &Vec<(String, String)>) -> String {
        let mut output: String = line.to_string();
        for (key, value) in keywords {
            output = output.replace(key, value);
        }
        output
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
    use std::fs::File;
    use std::io::{BufWriter, Write};
    use std::path::Path;
    use std::path::MAIN_SEPARATOR_STR;

    pub fn save(code: &String, output: &String) {
        match File::create(output) {
            Ok(file) => {
                let mut writer: BufWriter<File> = BufWriter::new(file);
                writeln!(writer, "{}", code).expect("Error while trying to write code.");
            }
            Err(err) => {
                println!("Error openning file: {}", err);
            }
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
    let param_save: String = String::from("save");

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
