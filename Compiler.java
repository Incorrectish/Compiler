import java.io.*;
import java.util.*;
import java.util.regex.*;
public class Compiler {
    static HashMap<String, String> variableTypes = new HashMap<String, String>();
    static HashSet<String> functionDefinitions = new HashSet<>();
    static HashSet<String> objectNames = new HashSet<>();
    static boolean standard = true;

    public static void main(String[] args) throws IOException {
        String line = args[0];
        //change this
        //getting macros
        HashMap<String, String> macros = new HashMap<>();
        HashSet<String> libraries = new HashSet<>();
        ArrayList<String> dependencies = new ArrayList<>();
        // Reading the file
        String page = readLines(line, macros, dependencies, libraries);
        page = page.replace(": bool", ": boolean");
        page = page.replace("double(", "Double.parseDouble(");
        page = page.replace("int(", "Integer.parseInt(");
        //removing comments in the form /* */ regular expression. Change it later.
        Pattern pattern = Pattern.compile("/\\*[^~]*\\*/");
        Matcher matcher = pattern.matcher(page);
        page = matcher.replaceAll("");
        String className = line.split("\\.")[0];
        //replacing macros, switches out the first stuff with the target.
        for(String macro: macros.keySet()) {
            page = page.replace(macro, macros.get(macro));
        }
        for(var dependency: dependencies)
            evaluateClass(dependency);
        functionDefinitions = captureFunctions(page);
        // Creating compiler target file
        File compiled = new File(className+".java");
        compiled.createNewFile();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(compiled)));

//        evaluateFunctions(compiled);

        //splitting the file on semicolons: to change later
        if(standard) {
            out.write("import java.util.*;\nimport java.io.*;\n");
            variableTypes.put("readLine", "String");
            variableTypes.put("readInt", "int");
            variableTypes.put("readDouble", "int");
        }
        //write libraries, also you gotta figure out a language name
        for(String library: libraries)
            out.write(library.replace(" I.", " java.")+"\n");
        //boiler plate
        out.write("public class "+className+" {\n");
//        out.write("\t public static void main(String[] args) {\n");
        //evaluating all the functions
        for(String function: functionDefinitions) {
            String[] statements = function.split("[\r\n]+");
            for(String statement: statements) {
                String state = evaluateStatement(statement.trim()).trim();
                out.write(state+"\n");
            }
        }
        //
        /*
        * Most likely deprecating this
        for (String s : lines)
            out.write("\t\t"+evaluateStatement(s.trim()).trim() + ";\n");
        */

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

        /*
        out.write("\t}\n");
        */

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
                dependencies.add(line);
            }
            //read in macros
            else if(line.startsWith("#define")) {
                String[] macro = line.substring(7).trim().split("as");
                macros.put(macro[0].trim(), macro[1].trim());
            }
            //if the line is not a comment
            else if (((line = line.trim()).length() >= 2 && (line.charAt(0) != '/' || line.charAt(1) != '/')) || line.equals("{") || line.equals("}")) {
                page.append(line).append("\n");
            }
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
        String expression = "";
        // let x: mut int = 5 -> ["let", "x:", "mut", "int", "=", "5"]
        //checks if the statement is assignment
        String replace = inputLine.trim().replace("<string>", "<String>").replace("<int>", "<Integer>");
        if(inputLine.startsWith("let")) {
            String sentInput = replace.replace("<double>", "<Double>").replace("string[]", "String[]");
            sentInput = evaluateRHS(sentInput);
            expression = checkObjects(evaluateAssignment(sentInput, sentInput.split(" ")));
        } else if(inputLine.startsWith("print(") || inputLine.startsWith("println(")) {
            //get the implicit arrays.tostring
            expression = "System.out."+evaluateRHS(inputLine);
        }  else if((inputLine.contains(" = "))) {
            String sentInput = replace.replace("<double>", "<Double>").replace("string[]", "String[]");
            // left hand side stuff
           expression = checkObjects(evaluateLHS(sentInput));
            //
        } else if(inputLine.contains("struct ")) {
            if(inputLine.startsWith("pub "))
                expression+="public ";
            else
                expression+="private ";
            if(inputLine.contains("stat "))
                expression+="static ";
            String[] tokens = inputLine.split(" ");
            String name = tokens[tokens.length-1];
            expression+=(name+" {");
        } else if(inputLine.contains("fn ")) {
            inputLine = inputLine.replace("string", "String");
          if(inputLine.contains("pub "))
              expression+="public ";
          else
              expression+="private ";
          if(inputLine.contains("stat "))
              expression+="static ";
          Pattern returnPattern = Pattern.compile("=>\\s[\\w]+");
          Matcher returnMatcher = returnPattern.matcher(inputLine);
          String returnType;
          if(returnMatcher.find()) {
              returnType = returnMatcher.group(0).substring(3);
              expression+=returnType+" ";
          }
          else {
              expression += "void ";
              returnType = "void";
          }
          Pattern namePattern = Pattern.compile("[\\w]+\\s?\\(");
          Matcher nameMatcher = namePattern.matcher(inputLine);
          String name="";
          if(nameMatcher.find())
            name = nameMatcher.group(0).substring(0, nameMatcher.group(0).length()-1).trim();
          expression+=name;
          variableTypes.put(name, returnType);
          if(inputLine.contains("()"))
              expression+="() {";
          else {
              Pattern parameterPattern = Pattern.compile("\\([\\w:,\\s\\[\\]]+\\)");
              Matcher parameterMatcher = parameterPattern.matcher(inputLine);
              String parameters = "";
              if(parameterMatcher.find())
                  parameters = parameterMatcher.group(0);
              expression+="(";
              Pattern parameter = Pattern.compile("[\\w]+:\\s[\\w\\[\\]<>]+");
              Matcher paramMatcher = parameter.matcher(parameters);
              while(paramMatcher.find()) {
                  String[] paramTypes = paramMatcher.group(0).split(":");
                  expression+=(paramTypes[1]+" "+paramTypes[0]+", ");
              }
              expression = expression.substring(0, expression.length()-2)+") {";
          }
          if(inputLine.trim().equals("fn main() {") || inputLine.trim().equals("fn main () {"))
              expression = "public static void main(String[] args) {";
        }  else if(inputLine.startsWith("for ")) {
            expression = evaluateForLoop(inputLine);
        } else if(inputLine.startsWith("do ")) {
            expression = inputLine;
        } else if(inputLine.startsWith("while ")) {
            expression = evaluateConditional(inputLine);
        } else if(inputLine.startsWith("if ")) {
            expression = evaluateConditional(inputLine);
        } else if(inputLine.startsWith("else if ")) {
            expression = evaluateConditional(inputLine);
        } else if(inputLine.startsWith("else ")) {
            expression = inputLine;
        } else if(inputLine.startsWith("}")) {
            if(inputLine.trim().equals("}"))
                expression = "}";
            else
                expression = "} "+evaluateConditional(inputLine.substring(1).trim());
        } else {
            Pattern namePattern = Pattern.compile("[\\w]+:\\s[\\w]+;");
            Matcher nameMatcher = namePattern.matcher(inputLine);
            String[] tokens;
            if(nameMatcher.find()) {
                String declare = nameMatcher.group(0).replace(";", "");
                String[] tokens1 = declare.split(": ");
                expression = inputLine.replace(nameMatcher.group(0), tokens1[1]+" "+tokens1[0]);
            } else {
                expression = inputLine;
            }

        }
        return evaluateLHS(expression);
    }

    private static String checkObjects(String inputLine) {
        Pattern pattern = Pattern.compile("[\\w<>]+\\(");
        Matcher matcher = pattern.matcher(inputLine);
        Pattern pattern1 = Pattern.compile("\\.[\\w<>]+\\(");
        Matcher matcher1 = pattern1.matcher(inputLine);
        if(matcher.find() && !variableTypes.containsKey(matcher.group(0).substring(0, matcher.group(0).length()-1)) && !matcher1.find()) {
            int toSplit = inputLine.indexOf("=");
            inputLine = inputLine.substring(0, toSplit+1) + " new "+inputLine.substring(toSplit+1);
        }
        return inputLine;
    }

    private static String evaluateConditional(String inputLine) {
        Pattern withinPattern = Pattern.compile("[\\w\\.]+:\\s(\\[|\\()[\\w\\.]+,\\s[\\w\\.]+(\\]|\\))");
        Matcher withinMatcher = withinPattern.matcher(inputLine);
        while(withinMatcher.find()) {
            String[] tokens = withinMatcher.group(0).split(":");
            String variable = tokens[0].trim();
            String[] bounds = tokens[1].split(",");
            String within = "("+variable+(bounds[0].charAt(0) == '['? ">=": ">")+bounds[0].substring(2)+" && "+variable+(bounds[1].charAt(bounds[1].length()-1) == ']'? "<=": "<")+bounds[1].substring(0, bounds[1].length()-1)+")";
            inputLine = inputLine.replace(withinMatcher.group(0), within);
        }
        inputLine = evaluateRHS(inputLine);
        if(inputLine.startsWith("if")) {
            inputLine = inputLine.substring(0, 2)+"("+inputLine.substring(3, inputLine.length()-2)+") {";
        } else if(inputLine.startsWith("else if")) {
            inputLine = inputLine.substring(0, 7)+"("+inputLine.substring(8, inputLine.length()-2)+") {";
        } else if(inputLine.startsWith("while")) {
            inputLine = inputLine.substring(0, 5)+"("+inputLine.substring(6, inputLine.length()-2)+") {";
        }
        return inputLine;
    }

    /**
     * Takes in the right hand side of a variable assignment and returns the type
     * Regular expressions to start by checking for int then char then double then String then object
     */
//[a-zA-Z0-9$_-]{1}\(+[^~]*"[^"]*"

    public static String evaluateForLoop(String inputLine) {
        String expression = "";
        if(inputLine.contains(",")) {
            expression += "for(var ";
            Pattern iterVarPattern = Pattern.compile("\\s[\\w]+:");
            Matcher iterVarMatcher = iterVarPattern.matcher(inputLine);
            String iterVar = "";
            if(iterVarMatcher.find())
                iterVar = evaluateRHS(iterVarMatcher.group(0).trim().substring(0, iterVarMatcher.group(0).length()-2));
            expression+=iterVar;
            Pattern iterConstantPattern = Pattern.compile(";\\s?[\\w-]+\\s?\\{");
            Matcher iterConstantMatcher = iterConstantPattern.matcher(inputLine);
            String iterConstant = "; 1 {";
            if(iterConstantMatcher.find())
                iterConstant = iterConstantMatcher.group(0);
            Pattern range = Pattern.compile("(\\[|\\()[\\w\\.]+,\\s[\\w\\.]+(\\]|\\))");
            Matcher rangeMatcher = range.matcher(inputLine);
            String[] startAndEnd = null;
            if(rangeMatcher.find())
                startAndEnd = rangeMatcher.group(0).trim().split(",");
            String iterConstantSubStr = iterConstant.substring(1, iterConstant.length() - 2);
            expression+=(" = ("+(startAndEnd[0].charAt(0) == '[' ? startAndEnd[0].substring(1): startAndEnd[0].substring(1)+"+"+ iterConstantSubStr)+"); ");
            expression+=(iterVar+(startAndEnd[1].charAt(startAndEnd[1].length()-1) == ']'? (iterConstantSubStr.contains("-")? ">=" : "<="): (iterConstantSubStr.contains("-")? ">" : "<"))+ startAndEnd[1].substring(0, startAndEnd[1].length()-1)+"; "+iterVar+"+="+ iterConstantSubStr +") {");
        } else if(inputLine.contains(":")){
            expression += "for(var "+inputLine.substring(4, inputLine.length()-2)+") {";
        } else {
            expression = inputLine;
        }
        return expression;
    }

    public static String evaluateLHS(String sentInput) {
        Pattern collections = Pattern.compile("[\\w\\.:\\[\\]]+:[\\w\\.:\\[\\]]+\\s?=\\s?[\\w\\.:\\[\\]\"]+");
        Matcher collectionMatcher = collections.matcher(sentInput);
        String expr = "";
        ArrayList<String> matches = new ArrayList<>();
        if(collectionMatcher.find()) {
            expr = collectionMatcher.group(0);
        String[] sides = expr.split("=");
        String lhs = sides[0];
        StringBuilder rhsBuilder = new StringBuilder(sides[1]);
        if(sides.length > 2)
            for(int i = 2; i<sides.length; i++)
                rhsBuilder.append(sides[1]);
        String rhs = rhsBuilder.toString();
        rhs = evaluateRHS(rhs);
        Pattern namePattern = Pattern.compile("[\\w]+:\\[");
        Matcher nameMatcher = namePattern.matcher(lhs);
        String name = "";
        if(nameMatcher.find())
            name = nameMatcher.group(0).substring(0, nameMatcher.group(0).length()-2);
        if(sentInput.substring(sentInput.indexOf(name)-2, sentInput.indexOf(name)).equals("::"))
            name = "get"+name.substring(0, 1).toUpperCase()+name.substring(1);
        Pattern indexPattern = Pattern.compile("(::[\\w]+|:\\[[\\w\\.\"\\(\\)]+\\])");
        Matcher indexMatcher = indexPattern.matcher(lhs);
        while(indexMatcher.find())
            matches.add(indexMatcher.group(0));
        for(int i = 0; i<matches.size()-1;i++) {
            if(matches.get(i).startsWith("::")) {
                String objectVar = matches.get(i).substring(2);
                String getter = ".get"+objectVar.substring(0, 1).toUpperCase()+objectVar.substring(1)+"()";
                lhs = lhs.replaceFirst("::"+objectVar, getter);
            } else {
                String arrayVar = matches.get(i);
                String content = arrayVar.substring(2, arrayVar.length()-1);
                lhs = lhs.replace(arrayVar, (".get("+content+")"));
            }
        }
        if(matches.get(matches.size()-1).startsWith("::")) {
            String objectVar = matches.get(matches.size()-1).substring(2);
            String setter = ".set"+objectVar.substring(0, 1).toUpperCase()+objectVar.substring(1)+"("+rhs+")";
            lhs = lhs.replaceFirst("::"+objectVar, setter);
        } else {
            String arrayVar = matches.get(matches.size()-1);
            String content = arrayVar.substring(2, arrayVar.length()-1);
            if(variableTypes.get(name).contains("ArrayList")) {
                lhs = lhs.replace(arrayVar, (".set("+content+", "+rhs.trim()+")"));
            } else {
                lhs = lhs.replace(arrayVar, (".replace("+content+", "+rhs.trim()+")"));
            }
        }
        sentInput = lhs;
        }
        return sentInput;
    }

    public static String evaluateRHS(String sentInput) {
        Pattern objectPattern = Pattern.compile("::[\\w]+");
        Matcher objectMatcher = objectPattern.matcher(sentInput);
        while(objectMatcher.find()) {
            String objectVar = objectMatcher.group(0).substring(2);
            String getter = ".get"+objectVar.substring(0, 1).toUpperCase()+objectVar.substring(1)+"()";
            sentInput = sentInput.replaceFirst("::"+objectVar, getter);
        }
        Pattern arrayPattern = Pattern.compile(":\\[[\\w\\.\"\\(\\)]+\\]");
        Matcher arrayMatcher = arrayPattern.matcher(sentInput);
        while(arrayMatcher.find()) {
            String arrayVar = arrayMatcher.group(0).trim();
            String content = arrayVar.substring(2, arrayVar.length()-1);
            sentInput = sentInput.replace(arrayVar, (".get("+content+")"));
        }
        return sentInput;
    }

    public static String inferType(String rightHandSide) {
        if(variableTypes.containsKey(rightHandSide))
            return variableTypes.get(rightHandSide);
        String type;
//        Pattern objectNotString = Pattern.compile("[a-zA-Z\\d$_-]\\(+[^~]*\"[^\"]*\"");
//        Matcher matcher = objectNotString.matcher(rightHandSide);
        if(rightHandSide.contains("?")) {
            // this is a ternary operator
            type = "var";
        }
        else if(isString(rightHandSide.trim())) {
            //if our matcher returns false and there is a " then this must be a string
            type = "String";
        } else if(isChar(rightHandSide.trim())){
            type = "char";
        } else if(isDouble(rightHandSide.trim())) {
            type = "double";
        } else if(isInt(rightHandSide.trim())) {
            type = "int";
        } else if(isBoolean(rightHandSide.trim())) {
            type = "boolean";
        } else /* is object */ {
            Pattern objectNamePattern = Pattern.compile("[a-zA-Z\\d$_<>]+\\(");
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
        Pattern objectPattern = Pattern.compile("[a-zA-Z\\d$_<>]*\\(");
        Matcher object = objectPattern.matcher(inputLine);
        if(object.find() && object.group(0).trim().equals("for(")) {
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
        Pattern objectNotChar = Pattern.compile("[a-zA-Z\\d$_<>]\\(+[^~]*'[^']*'");
        Matcher matcher = objectNotChar.matcher(rightHandSide);
        return !matcher.find() && rightHandSide.contains("'");
    }
    public static boolean isString(String rightHandSide) {
        //match Object(x, c, 2, z,"something") but not "something to distinguish between
        //object creation and just strings, using regex magic
        Pattern objectNotString = Pattern.compile("[a-zA-Z\\d$_<>]\\(+[^~]*\"[^\"]*\"");
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
        Pattern objectPattern = Pattern.compile("[a-zA-Z\\d$_<>]\\(");
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
        Pattern objectPattern = Pattern.compile("[a-zA-Z\\d$_<>]\\(");
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
            return ((rightHandSide.contains(">") && !rightHandSide.contains("<")) || (rightHandSide.contains("<") && !rightHandSide.contains(">"))) || (rightHandSide.contains("||") || rightHandSide.contains(">=") || rightHandSide.contains("<=") || rightHandSide.contains("&&") || rightHandSide.contains(".equals()") || rightHandSide.equals("false") || rightHandSide.equals("true")) || rightHandSide.contains("!");
        }
    }

    /**/

    public static void evaluateFunctions(File compiled) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(compiled)));
        out.write("hi");
        for(String function: functionDefinitions) {
            String[] statements = function.split("[\r\n]+");
            for(String statement: statements) {
                String state = evaluateStatement(statement.trim()).trim();
                out.write(state+"\n");
            }
        }
        out.close();
    }
//    public static void captureFunctions(String page) {
//        Pattern fnTypePattern = Pattern.compile("fn\\s[\\w]+\\s?\\(([\\w:,\\s]+)?\\)(\\s?=>\\s?[\\w]+)?");
//        Matcher fnTypeMatcher = fnTypePattern.matcher(page);
//        while(fnTypeMatcher.find()) {
//            Pattern namePattern = Pattern.compile("[\\w]+\\(");
//            Matcher nameMatcher = namePattern.matcher(fnTypeMatcher.group(0));
//            String name = "";
//            if(nameMatcher.find())
//                name = nameMatcher.group(0).trim();
//            if(fnTypeMatcher.group(0).contains("=>")) {
//                String type = "";
//                if(nameMatcher.find()) {
//                    type = fnTypeMatcher.group(0).split("=>")[1].substring(0, fnTypeMatcher.group(0).split("=>")[1].length()-1).trim();
//                }
//                variableTypes.put(name, type);
//            } else {
//                variableTypes.put(name, "void");
//            }
//        }
//        boolean functionsRemaining = true;
//        while (functionsRemaining) {
//            int openingBrackets = 0, closingBrackets = 0;
//            if (page.contains("pub stat fn")) {
//                StringBuilder functionBuilder = new StringBuilder();
//                int begin = page.indexOf("pub stat fn");
//                begin = page.indexOf("n", begin)+1;
//                functionBuilder.append("pub stat fn");
//                while(page.charAt(begin) != '{') {
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                }
//                do {
//                    if(page.charAt(begin) == '{') {
//                        openingBrackets++;
//                    } else if(page.charAt(begin) == '}') {
//                        closingBrackets++;
//                    }
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                } while(openingBrackets > closingBrackets);
//                String function = functionBuilder.toString();
//                functionDefinitions.add(function);
//                page = page.replace(function, "");
//            } else if (page.contains("pub fn")) {
//                StringBuilder functionBuilder = new StringBuilder();
//                int begin = page.indexOf("pub fn");
//                begin = page.indexOf("n", begin)+1;
//                functionBuilder.append("pub fn");
//                while(page.charAt(begin) != '{') {
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                }
//                do {
//                    if(page.charAt(begin) == '{') {
//                        openingBrackets++;
//                    } else if(page.charAt(begin) == '}') {
//                        closingBrackets++;
//                    }
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                } while(openingBrackets > closingBrackets);
//                String function = functionBuilder.toString();
//                functionDefinitions.add(function);
//                page = page.replace(function, "");
//            } else if (page.contains("stat fn")) {
//                StringBuilder functionBuilder = new StringBuilder();
//                int begin = page.indexOf("stat fn");
//                begin = page.indexOf("n", begin)+1;
//                functionBuilder.append("stat fn");
//                while(page.charAt(begin) != '{') {
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                }
//                do {
//                    if(page.charAt(begin) == '{') {
//                        openingBrackets++;
//                    } else if(page.charAt(begin) == '}') {
//                        closingBrackets++;
//                    }
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                } while(openingBrackets > closingBrackets);
//                String function = functionBuilder.toString();
//                functionDefinitions.add(function);
//                page = page.replace(function, "");
//            } else if(page.contains("fn")) {
//                StringBuilder functionBuilder = new StringBuilder();
//                int begin = page.indexOf("fn");
//                begin = page.indexOf("n", begin)+1;
//                functionBuilder.append("fn");
//                while(page.charAt(begin) != '{') {
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                }
//                do {
//                    if(page.charAt(begin) == '{') {
//                        openingBrackets++;
//                    } else if(page.charAt(begin) == '}') {
//                        closingBrackets++;
//                    }
//                    functionBuilder.append(page.charAt(begin));
//                    begin++;
//                } while(openingBrackets > closingBrackets);
//                String function = functionBuilder.toString();
//                functionDefinitions.add(function);
//                page = page.replace(function, "");
//            } else /* no functions remaining */ {
//                functionsRemaining = false;
//            }
//        }
//    }
    public static void evaluateClass(String file) throws IOException {
        String classPath = file.substring(4, file.length()-1).trim();
        BufferedReader br = new BufferedReader(new FileReader(new File(classPath)));
        File compiledClass = new File(classPath.replace(".i", ".java"));
        compiledClass.createNewFile();
        String className = compiledClass.getName().substring(0, compiledClass.getName().length()-5);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(compiledClass)));
        boolean standardClass = true;
        HashMap<String, String> macrosClass = new HashMap<>();
        HashSet<String> librariesClass = new HashSet<>();
        String line;
        StringBuilder pageBuilder = new StringBuilder();
        while((line = br.readLine()) != null) {
            if(line.trim().equals("#no std;")) {
                standardClass = false;
            }
            else if(line.startsWith("import")) {
                librariesClass.add(line);
            }
            //read in dependencies
            //read in macros
            else if(line.startsWith("#define")) {
                String[] macro = line.substring(7).trim().split("as");
                macrosClass.put(macro[0].trim(), macro[1].trim());
            }
            //if the line is not a comment
            else if (((line = line.trim()).length() >= 2 && (line.charAt(0) != '/' || line.charAt(1) != '/')) || line.equals("{") || line.equals("}")) {
                pageBuilder.append(line).append("\n");
            }
        }
        String page = pageBuilder.toString();
        br.close();
        for(var i: macrosClass.keySet())
            page.replace(i, macrosClass.get(i));
        page = page.replaceAll("=>\\s?string", "==> String");
        page = page.replaceAll(": string", ": String");
        for(var i: librariesClass)
            out.write(i.replace(" I.", " java.")+"\n");
        if(standardClass)
            out.write("import java.util.*;\nimport java.io.*;\n");
        out.write("public class "+className+" {\n");
        Pattern attributesPattern = Pattern.compile("\\{[[\\w]+:\\s[\\w]+,\\s]+");
        Matcher attributeMatcher = attributesPattern.matcher(page);
        String attributeString = "";
        if(attributeMatcher.find())
            attributeString = attributeMatcher.group(0).substring(1);
        String[] attributes = attributeString.split(",\\s");
        StringBuilder constructor = new StringBuilder("public " + className + " (");
        StringBuilder constructorBody = new StringBuilder();
        for(String attribute: attributes) {
            StringBuilder expressionBuilder = new StringBuilder();
            String[] nameAndType = attribute.split(":\\s");
            constructor.append(nameAndType[1].trim()).append(" ").append(nameAndType[0]+", ");
            constructorBody.append("this.").append(nameAndType[0].trim()).append(" = ").append(nameAndType[0]).append(";\n");
            expressionBuilder.append("private ").append(nameAndType[1]).append(" ").append(nameAndType[0]).append(";\n\n");
            expressionBuilder.append("public ").append(nameAndType[1]).append(" get").append(nameAndType[1].substring(0, 1).toUpperCase()).append(nameAndType[1].substring(1)).append("() {\n").append("return ").append(nameAndType[0]).append(";\n").append("}\n\n");
            expressionBuilder.append("public void set").append(nameAndType[1].substring(0, 1).toUpperCase()).append(nameAndType[1].substring(1)).append("(").append(nameAndType[1]).append(" ").append(nameAndType[0]).append(") {\n").append("this.").append(nameAndType[0].trim()).append(" = ").append(nameAndType[0]).append(";\n").append("}\n");
            out.write(expressionBuilder.toString()+"\n");
        }
        String constructorString = constructor.toString();
        constructorString = constructorString.substring(0, constructorString.length()-2)+") {\n";
        constructorString = constructorString+constructorBody.toString()+"}\n";
        out.write(constructorString+"\n");
        HashSet<String> functionClassDefinitions = captureFunctions(page);
        for(String function: functionClassDefinitions) {
            String[] statements = function.split("[\r\n]+");
            for(String statement: statements) {
                String state = evaluateStatement(statement.trim()).trim();
                out.write(state+"\n");
            }
        }
        out.write("}");
        out.close();
    }
    public static HashSet<String> captureFunctions(String page) {
        HashSet<String> functionClassDefinitions = new HashSet<>();
        Pattern fnTypePattern = Pattern.compile("fn\\s[\\w]+\\s?\\(([\\w:,\\s]+)?\\)(\\s?=>\\s?[\\w]+)?");
        Matcher fnTypeMatcher = fnTypePattern.matcher(page);
        while(fnTypeMatcher.find()) {
            Pattern namePattern = Pattern.compile("[\\w]+\\(");
            Matcher nameMatcher = namePattern.matcher(fnTypeMatcher.group(0));
            String name = "";
            if(nameMatcher.find())
                name = nameMatcher.group(0).trim();
            if(fnTypeMatcher.group(0).contains("=>")) {
                String type = "";
                if(nameMatcher.find()) {
                    type = fnTypeMatcher.group(0).split("=>")[1].substring(0, fnTypeMatcher.group(0).split("=>")[1].length()-1).trim();
                }
                variableTypes.put(name, type);
            } else {
                variableTypes.put(name, "void");
            }
        }
        boolean functionsRemaining = true;
        while (functionsRemaining) {
            int openingBrackets = 0, closingBrackets = 0;
            if (page.contains("pub stat fn")) {
                StringBuilder functionBuilder = new StringBuilder();
                int begin = page.indexOf("pub stat fn");
                begin = page.indexOf("n", begin)+1;
                functionBuilder.append("pub stat fn");
                while(page.charAt(begin) != '{') {
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                }
                do {
                    if(page.charAt(begin) == '{') {
                        openingBrackets++;
                    } else if(page.charAt(begin) == '}') {
                        closingBrackets++;
                    }
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                } while(openingBrackets > closingBrackets);
                String function = functionBuilder.toString();
                functionClassDefinitions.add(function);
                page = page.replace(function, "");
            } else if (page.contains("pub fn")) {
                StringBuilder functionBuilder = new StringBuilder();
                int begin = page.indexOf("pub fn");
                begin = page.indexOf("n", begin)+1;
                functionBuilder.append("pub fn");
                while(page.charAt(begin) != '{') {
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                }
                do {
                    if(page.charAt(begin) == '{') {
                        openingBrackets++;
                    } else if(page.charAt(begin) == '}') {
                        closingBrackets++;
                    }
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                } while(openingBrackets > closingBrackets);
                String function = functionBuilder.toString();
                functionClassDefinitions.add(function);
                page = page.replace(function, "");
            } else if (page.contains("stat fn")) {
                StringBuilder functionBuilder = new StringBuilder();
                int begin = page.indexOf("stat fn");
                begin = page.indexOf("n", begin)+1;
                functionBuilder.append("stat fn");
                while(page.charAt(begin) != '{') {
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                }
                do {
                    if(page.charAt(begin) == '{') {
                        openingBrackets++;
                    } else if(page.charAt(begin) == '}') {
                        closingBrackets++;
                    }
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                } while(openingBrackets > closingBrackets);
                String function = functionBuilder.toString();
                functionClassDefinitions.add(function);
                page = page.replace(function, "");
            } else if(page.contains("fn")) {
                StringBuilder functionBuilder = new StringBuilder();
                int begin = page.indexOf("fn");
                begin = page.indexOf("n", begin)+1;
                functionBuilder.append("fn");
                while(page.charAt(begin) != '{') {
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                }
                do {
                    if(page.charAt(begin) == '{') {
                        openingBrackets++;
                    } else if(page.charAt(begin) == '}') {
                        closingBrackets++;
                    }
                    functionBuilder.append(page.charAt(begin));
                    begin++;
                } while(openingBrackets > closingBrackets);
                String function = functionBuilder.toString();
                functionClassDefinitions.add(function);
                page = page.replace(function, "");
            } else /* no functions remaining */ {
                functionsRemaining = false;
            }
        }
        return functionClassDefinitions;
    }
}
