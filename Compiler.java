import java.io.*;
import java.util.*;
import java.util.regex.*;
public class Compiler {
    static HashMap<String, String> variableTypes = new HashMap<String, String>();
    static HashSet<String> objectNames = new HashSet<>();
    static boolean standard = true;

    public static void main(String[] args) throws IOException {
        String line = args[0];
        //change this
        objectNames.add("Object");
        //getting macros
        HashMap<String, String> macros = new HashMap<>();
        HashSet<String> libraries = new HashSet<>();
        ArrayList<String> dependencies = new ArrayList<>();
        // Reading the file
        String page = readLines(line, macros, dependencies, libraries);
        //removing comments in the form /* */ regular expression. Change it later.
         String regex = "/\\*[^~]*\\*/";
         Pattern pattern = Pattern.compile(regex);
         Matcher matcher = pattern.matcher(page);
        page = matcher.replaceAll("");
        String className = line.split("\\.")[0];
        //replacing macros, switches out the first stuff with the target.
        for(String macro: macros.keySet()) {
            page = page.replace(macro, macros.get(macro));
        }
        // Creating compiler target file
        File compiled = new File(className+".java");
        compiled.createNewFile();

        //splitting the file on semicolons: to change later
        String[] lines = page.split(";");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(compiled)));
        if(standard) {
            out.write("import java.util.*;\nimport java.io.*;");
            variableTypes.put("readLine()", "String");
            variableTypes.put("readInt()", "int");
            variableTypes.put("readDouble()", "int");
        }
        //write libraries, also you gotta figure out a language name
        for(String library: libraries)
            out.write(library.replace("language", "java")+"\n");
        //boiler plate
        out.write("public class "+className+" {\n");
        out.write("\t public static void main(String[] args) {\n");
        for (String s : lines)
            out.write("\t\t"+evaluateStatement(s.trim()).trim() + ";\n");
        // Writing to the file
        //creating wrapper text for class and main

        // Creating actual java program

        //Splitting the input into the tokens, and statements to be evaluated, look for keywords: loop, for, while until '}'
       // String[] code = page.split("\\.");
        //for(String i: code)
          //  out.print("\t\t"+evaluate(i)+"\n");
        //first split on {}, {} blocks are the most important in terms of precedence
        //String[] code = bracketSplit(page);

        // Ending wrapper text
        out.write("\t}\n");
        if(standard) {
            out.write("\tpublic static String readLine() {\n");
            out.write("\t\tScanner scanner = new Scanner(System.in);\n");
            out.write("\t\treturn scanner.nextLine();\n");
            out.write("\t}\n\n");
            out.write("\tpublic static int readInt() {\n");
            out.write("\t\tScanner scanner = new Scanner(System.in);\n");
            out.write("\t\treturn scanner.nextInt();\n");
            out.write("\t}\n\n");
            out.write("\tpublic static String readLine(String message) {\n");
            out.write("\t\tSystem.out.println(message);\n");
            out.write("\t\tScanner scanner = new Scanner(System.in);\n");
            out.write("\t\treturn scanner.nextLine();\n");
            out.write("\t}\n\n");
            out.write("\tpublic static int readInt(String message) {\n");
            out.write("\t\tSystem.out.println(message);\n");
            out.write("\t\tScanner scanner = new Scanner(System.in);\n");
            out.write("\t\treturn scanner.nextInt();\n");
            out.write("\t}\n\n");
            out.write("\t\tpublic static double readDouble() {\n");
            out.write("\t\tScanner scanner = new Scanner(System.in);\n");
            out.write("\t\treturn scanner.nextDouble();\n");
            out.write("}\n\n");
            out.write("\tpublic static double readDouble(String message) {\n");
            out.write("\t\tSystem.out.println(message);\n");
            out.write("\t\tScanner scanner = new Scanner(System.in);\n");
            out.write("\t\treturn scanner.nextDouble();\n");
            out.write("\t}\n\n");
        }
        out.write("}\n");
        out.close();
        System.out.println("Compiled "+line+" => "+compiled.getName().substring(0, compiled.getName().length()-5)+".class");
    }

    /*
    * Our readline function
    * Goes through every line in the file
    * if the line begins with "no std" then sets global boolean to false so
    * that the readline() function and java.util.*;/java.io.*; libraries are not imported
    * #define lines create a macro that replaces all words with another word
    * import does what you would expect
    * use signals object files, not libraries being used so they can be compiled
    *
     */
    public static String readLines(String filename, HashMap<String, String> macros, ArrayList<String> dependencies, HashSet<String> libraries) throws IOException {
        BufferedReader br  = new BufferedReader(new FileReader(filename));
        StringBuilder page = new StringBuilder();
        //reading first line for standard/no standard.
        String line;
        //reading macros
        //reading the body
        while((line = br.readLine()) != null) {
            if(line.trim().equals("#no std;")) {
                standard = false;
            }
            else if(line.startsWith("import")) {
                libraries.add(line);
            }
            //read in dependencies
            else if(line.startsWith("use ")) {
                dependencies.add(line.substring(3).trim());
            }
            //read in macros
            else if(line.startsWith("#define")) {
                String[] macro = line.substring(7).trim().split("as");
                macros.put(macro[0].trim(), macro[1].trim());
            }
            //if the line is not a comment
            else if ((line = line.trim()).length() >= 2 && (line.charAt(0) != '/' || line.charAt(1) != '/'))
                page.append(line);
        }
        br.close();
        return page.toString();
    }

    /*
    * Evaluates every line of input. Extremely basic and not really working now
     * Next work on standard in and out after variables are done: with lambdas too :(
     * stdin should be like var x = readInt("Input name");
     * stdout should be like print(); and println();
     * math is as easy as it seems, most things can be directly turned into java
     * if condition {
     * }
     * for condition in conditions {
     * }
     * pub stat fn FN_NAME(arg 1, arg 2) => return type {
     *  //fn body
     * }
     * objects to be finished after I actually implement objects lmfao
     */
    public static String evaluateStatement(String inputLine) {
        // ARRAYS MUST BE TYPECAST PLS FIX NOT DONE
        String expression = "";
        // let x: mut int = 5 -> ["let", "x:", "mut", "int", "=", "5"]
        String[] tokens = inputLine.split(" ");
        //checks if the statement is assignment
        if(tokens[0].equals("let")) {
            expression = evaluateAssignment(inputLine.trim(), tokens);
        } else if(inputLine.length() >= 6 && (inputLine.startsWith("print(") || inputLine.startsWith("println("))) {
            //get the implicit arrays.tostring
            expression = "System.out."+inputLine;
        } else if((inputLine.startsWith("stdin "))) {
            //tenative, to fix
            expression = "Scanner "+inputLine.substring(6) + "= new Scanner(System.in) ";
        } else if(inputLine.startsWith("File")) {

        }
        return expression;
    }
    /**
     * Takes in the right hand side of a variable assignment and returns the type
     * Regular expressions to start by checking for int then char then double then String then object
     */
//[a-zA-Z0-9$_-]{1}\(+[^~]*"[^"]*"
    public static String inferType(String rightHandSide) {
        if(variableTypes.containsKey(rightHandSide))
            return variableTypes.get(rightHandSide);
        String type;
//        Pattern objectNotString = Pattern.compile("[a-zA-Z\\d$_-]\\(+[^~]*\"[^\"]*\"");
//        Matcher matcher = objectNotString.matcher(rightHandSide);
        if(rightHandSide.contains("if(")) {
            // this is a ternary operator
            type = "var";
        }
        else if(isString(rightHandSide.trim())) {
            //if our matcher returns false and there is a " then this must be a string
            type = "String";
        } else if(isChar(rightHandSide.trim())){
            type = "char";
        } else if(isInt(rightHandSide.trim())) {
            type = "int";
        } else if(isDouble(rightHandSide.trim())) {
            type = "double";
        } else if(isBoolean(rightHandSide.trim())) {
            type = "boolean";
        } else /* is object */ {
            // INDEX OUT OF BOUNDS ERROR HERE
            Pattern objectNamePattern = Pattern.compile("[a-zA-Z\\d$-_<>]+\\(");
            Matcher objectName = objectNamePattern.matcher(rightHandSide);
            if(objectName.find()) {
                type = objectName.group(0).substring(0, objectName.group(0).length() - 1) + " ";
                if(!objectNames.contains(type))
                    type = "var";
            }
            else
                type = "var";
        }
        return type;
    }
    public static String evaluateAssignment(String inputLine, String[] tokens) {
        StringBuilder expressionBuilder = new StringBuilder();
        StringBuilder nameBuilder = new StringBuilder();
        for(int i = inputLine.indexOf(" ")+1; (i<inputLine.length() && i>=0) && (inputLine.charAt(i) != ' ' && inputLine.charAt(i) != ':') ; i++) {
            nameBuilder.append(inputLine.charAt(i));
        }
        String name = nameBuilder.toString();
        //looks for type
        if(tokens[1].charAt(tokens[1].length()-1) == ':' && tokens[2].equals("mut") && !tokens[3].equals("=")) {
            //if variable is declared mutable
            //let x: mut int = 5;
            String type = (tokens[3].equals("string")? "String": (tokens[3].equals("string[]")? "String[]" : tokens[3]));
            expressionBuilder.append(type).append(" ").append(tokens[1].substring(0, tokens[1].length()-1)).append(" ");
            variableTypes.put(name.trim(), type.trim());
            for(int i = 4; i<tokens.length; i++)
                expressionBuilder.append(tokens[i]).append(" ");

            //if variable is not explicitly declared mutable
            //let x: int = 5;
        } else if(tokens[1].charAt(tokens[1].length()-1) == ':' && !tokens[2].equals("mut")) {
            String type = (tokens[2].equals("string")? "String": (tokens[2].equals("string[]")? "String[]" : tokens[2]));
            expressionBuilder.append("final ").append(type).append(" ").append(tokens[1].substring(0, tokens[1].length()-1)).append(" ");
            variableTypes.put(name.trim(), type.trim());
            for(int i = 3; i< tokens.length; i++)
                expressionBuilder.append(tokens[i]).append(" ");
        }
        //if no type given
        else {
            // if the variable is declared mutable
            // let x: mut = 5;
            String[] split = inputLine.split("=");
            StringBuilder rightHandSide = new StringBuilder();
            if(tokens[2].equals("mut")) {
                //when look for the type, the entire left side(let x: mut) is useless
                for(int i = 1; i< split.length; i++)
                    rightHandSide.append(split[i]);
                String type = inferType(rightHandSide.toString().trim());
                variableTypes.put(name.trim(), type.trim());
                expressionBuilder.append(type).append(" ").append(tokens[1].substring(0, tokens[1].length() - 1)).append(" ");
                for(int i = 3; i<tokens.length; i++)
                    expressionBuilder.append(tokens[i]).append(" ");
            }
            // if the variable is not explicitly declared mutable
            // let x = 5;
            else {
                for(int i = 1; i< split.length; i++)
                    rightHandSide.append(split[i]);
                String type = inferType(rightHandSide.toString().trim());
                expressionBuilder.append("final ").append(type).append(" ").append(tokens[1]).append(" ");
                variableTypes.put(name.trim(), type.trim());
                for(int i = 2; i<tokens.length; i++)
                    expressionBuilder.append(tokens[i]).append(" ");
            }
        }
        String expression = expressionBuilder.toString();
        //just some array stuff
        //if the array is declared normally like let z = int[x];, just plop a new in front of the int[ and everything is good
        //actually lets change from let z = int[x] to let z: int[] = [x]
        if(expression.contains("[") && !expression.contains(",")) {
            int equals = expression.indexOf("=");
            String type = (tokens[2].equals("mut")? tokens[3]: tokens[2]);
            String substring = type.substring(0, type.length() - 2);
            expression = expression.substring(0, equals+1)+" new "+(substring.equals("string")? "String": substring) +expression.substring(equals+1);
            // look for default values or lambdas
            // Regular expression that looks for a colon some amount of characters and then ]
            // for example would match :i=>(i*i)] or : nw87t328t=>?~@$@!} ]
            String lambda = "";
            Pattern pattern = Pattern.compile(":.*\\]");
            Matcher matcher = pattern.matcher(expression);
            //extract default expression
            if(matcher.find()) {
                lambda = matcher.group(0);
                expression = matcher.replaceAll("]");
            }
            if(lambda.contains("=>")) {
                String[] lambdas = lambda.split("=>");
                String iterationVar = lambdas[0].replace(":", "").trim();
                //need to get the variable between [] in expression
                StringBuilder sizeBuilder = new StringBuilder();
                int firstSquareBracket = expression.indexOf("[", expression.indexOf("[")+1)+1;
                while(expression.charAt(firstSquareBracket) != ']') {
                    sizeBuilder.append(expression.charAt(firstSquareBracket));
                    firstSquareBracket++;
                }
                String size = sizeBuilder.toString();
                //somehow turning the lambdas into a for loop, idk???????
                expression+=";\nfor(int "+iterationVar+"= 0; "+iterationVar+"<"+size.substring(0,size.length())+"; "+iterationVar+"++) "+name+"["+ iterationVar +"] ="+lambdas[1].substring(0, lambdas[1].length()-1);

            }

            // String s = "this is code and /* this is a comment and */
            // within code comments are not read";
            // String re = "/\\*[^()]*\\*\\/";
            // Pattern p = Pattern.compile(re);
            // Matcher m = p.matcher(s);
            // while (m.find()) {
            // s = m.replaceAll("");
            // m = p.matcher(s);

        }
        //otherwise if it is like let z: int[] = [a, b, c, d, e, f, g]; fixes are needed
        else if(expression.contains("[") && expression.contains(",")) {
            expression = expression.replace("[", "{").replace("]", "}");
            expression = expression.substring(0, expression.indexOf("{"))+"[]"+expression.substring(expression.indexOf("{")+2);
        }
        Pattern objectPattern = Pattern.compile("[a-zA-Z\\d$_\\-\\<\\>]*\\(");
        Matcher object = objectPattern.matcher(inputLine);
        if(object.find()) {
            String objectName = object.group(0).substring(0, object.group(0).length()-1);
            if(objectNames.contains(objectName)) {
                String[] sides = expression.split("=");
                StringBuilder rhssb = new StringBuilder();
                for(int i = 1; i< sides.length; i++)
                    rhssb.append(sides[i]);
                sides[0] = sides[0].trim();
                String rhs = rhssb.toString().trim();
                expression = sides[0]+" = new "+rhs;
            }
        }
        return expression;
    }
   /**/
    /*
    * These methods are responsible for figuring out whether the right hand side would evaluate to a string, char, boolean, double, or int
    */
    public static boolean isChar(String rightHandSide) {
        // same idea except with ' for char, these two are the easiest to ascertain
        Pattern objectNotChar = Pattern.compile("[a-zA-Z\\d$_\\-\\<\\>]\\(+[^~]*'[^']*'");
        Matcher matcher = objectNotChar.matcher(rightHandSide);
        return !matcher.find() && rightHandSide.contains("'");
    }
    public static boolean isString(String rightHandSide) {
        //match Object(x, c, 2, z,"something") but not "something to distinguish between
        //object creation and just strings, using regex magic
        Pattern objectNotString = Pattern.compile("[a-zA-Z\\d$_\\-\\<\\>]\\(+[^~]*\"[^\"]*\"");
        Matcher matcher = objectNotString.matcher(rightHandSide);
        String[] tokens;
        boolean anyStrings = false;
        if(rightHandSide.contains("+")) {
            tokens = rightHandSide.split("\\+");
            for (String token : tokens)
                if (variableTypes.containsKey(token.trim()) && variableTypes.get(token.trim()).equals("String"))
                    anyStrings = true;
        }
        if(variableTypes.containsKey(rightHandSide.trim()) && variableTypes.get(rightHandSide.trim()).equals("String"));
        return !matcher.find() && (rightHandSide.contains("\"") || anyStrings);
    }

    // THIS MUST BE SUBORDINATE TO STRING AS IT WILL CLASSIFY STRINGS AS INTS, MEANING YOU MUST CLASSIFY STRINGS BEFORE USING THIS
    public static boolean isInt(String rightHandSide) {
        Pattern objectPattern = Pattern.compile("[a-zA-Z\\d$_\\-\\<\\>]\\(");
        Matcher object = objectPattern.matcher(rightHandSide);
        Pattern isDouble = Pattern.compile("\\d\\.\\d]");
        Matcher matchDouble = isDouble.matcher(rightHandSide);
        Pattern numberPattern = Pattern.compile("\\d");
        Matcher number = numberPattern.matcher(rightHandSide);
        boolean isInt = false;
        if(rightHandSide.contains("+")) {
            String[] vars = rightHandSide.split("\\+");
            for(String i: vars)
                if(variableTypes.containsKey(i) && variableTypes.get(i).equals("int"))
                    isInt = true;
            for(String i: vars)
                if(variableTypes.containsKey(i) && variableTypes.get(i).equals("double"))
                    isInt = false;
        } else {
            if (variableTypes.containsKey(rightHandSide.trim()))
                isInt = (variableTypes.get(rightHandSide.trim()).trim().equals("int"));
        }
        boolean mathExp = rightHandSide.contains("/") || rightHandSide.contains("-")
                || rightHandSide.contains("%") || rightHandSide.contains("<<") ||
                rightHandSide.contains(">>") || rightHandSide.contains("*")
                || rightHandSide.contains("^") || rightHandSide.contains("~");
        return (!rightHandSide.contains("'") && !rightHandSide.contains("\"")
                && !object.find() && !matchDouble.find() && (number.find() || mathExp)) || isInt;
    }

    public static boolean isDouble(String rightHandSide) {
        Pattern objectPattern = Pattern.compile("[a-zA-Z\\d$_\\-\\<\\>]\\(");
        Matcher object = objectPattern.matcher(rightHandSide);
        Pattern isDouble = Pattern.compile("\\d\\.\\d]");
        Matcher matchDouble = isDouble.matcher(rightHandSide);
        boolean mathExp = rightHandSide.contains("/") || rightHandSide.contains("-")
                || rightHandSide.contains("%") || rightHandSide.contains("<<") ||
                rightHandSide.contains(">>") || rightHandSide.contains("*")
                || rightHandSide.contains("^") || rightHandSide.contains("~") ||
                rightHandSide.contains("<<<") || rightHandSide.contains(">>>");
        return !rightHandSide.contains("'") && !rightHandSide.contains("\"")
                && !object.find() && matchDouble.find() &&  mathExp;
    }

    public static boolean isBoolean(String rightHandSide) {
        if(rightHandSide.contains("if ")) return false;
        else {
            return (rightHandSide.contains(">") && !rightHandSide.contains("<") || rightHandSide.contains("<") && !rightHandSide.contains(">")) || (rightHandSide.contains("||") || rightHandSide.contains(">=") || rightHandSide.contains("<=") || rightHandSide.contains("&&") || rightHandSide.contains(".equals()") || rightHandSide.equals("false") || rightHandSide.equals("true"));
        }
    }

    /**/
    public static String[] bracketSplit(String input) {
        int openingBrackets = 0, closingBrackets = 0;
        for(int i = 0; i < input.length(); i++) {
            //
        }
        return new String[]{"hi"};
    }
}
