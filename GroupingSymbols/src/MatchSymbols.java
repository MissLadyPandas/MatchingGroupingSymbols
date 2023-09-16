import java.io.*;

// this class checks for matching grouping symbols in a Java source-code.
public class MatchSymbols {

    public static void main(String[] args) {
        
        // this is a simple test to show the code works.
        runTest();
        
        // if you provide a filename when running the program, it will check that file for matching symbols.
        if (args.length == 1) {
            String filename = args[0];
            try {
                // Check the file for matching symbols and show a message about it.
                boolean symbolsMatch = checkSymbols(filename);
                if (symbolsMatch) {
                    System.out.println("All symbols in " + filename + " match!");
                } else {
                    System.out.println("Symbols in " + filename + " do not match!");
                }
            } catch (IOException e) {
                System.out.println("Had trouble reading the file: " + e.getMessage());
            }
        }
    }
    
    // this method checks if the symbols in a file match up correctly.
    public static boolean checkSymbols(String filename) throws IOException {
        
        // this helps read the file.
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        // piler. add symbols to the pile and remove when find a match.
        StringBuilder stack = new StringBuilder();

        int ch;
        // read the file one character at a time.
        while ((ch = reader.read()) != -1) {
            char symbol = (char) ch;
            
            // when see opening symbol, add it to pile.
            if (symbol == '(' || symbol == '{' || symbol == '[') {
                stack.append(symbol);
            } 
            // when see a closing symbol, checks if the last symbol in pile matches it. if it does,  remove it from the pile.
            else if (symbol == ')' && (stack.length() == 0 || stack.charAt(stack.length() - 1) != '(') ||
                     symbol == '}' && (stack.length() == 0 || stack.charAt(stack.length() - 1) != '{') ||
                     symbol == ']' && (stack.length() == 0 || stack.charAt(stack.length() - 1) != '[')) {
                reader.close();
                return false;
            }
        }

        reader.close();
        // if  pile is empty at the end, all symbols matched. Otherwise, they didn't.
        return stack.length() == 0;
    }

    // a simple test to see if  symbol checker works.
    public static void runTest() {
        String testCode = "public class Test {\n" +
                          "    public static void main(String[] args) {\n" +
                          "        System.out.println(\"Hello World!\");\n" +
                          "        if (args.length > 0) {\n" +
                          "            System.out.println(\"{Hello}\");\n" +
                          "        }\n" +
                          "    }\n" +
                          "}\n";
        try {
            // save the test code to a temporary file.
            File tempFile = File.createTempFile("test", ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write(testCode);
            writer.close();
            
            // check the temporary file with method.
            if (checkSymbols(tempFile.getAbsolutePath())) {
                System.out.println("Symbols in test code match!");
            } else {
                System.out.println("Symbols in test code do not match!");
            }

            // clean up by deleting the temporary file.
            tempFile.deleteOnExit();
        } catch(IOException e) {
            System.out.println("Error during the test: " + e.getMessage());
        }
    }
}
