import java.io.*;
import java.util.*;
import java.util.regex.*;
public class Compiler {
    //static HashMap<String, String> variableTypes = new HashMap<String, String>();
    public static void main(String[] args) throws IOException {
        // Taking file to be compiled from user
        System.out.println("Enter the file to be compiled");
        // Efficient IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        // Reading the file
        String page = readLines(line);
        //removing comments in the form /* */ regular expression. Change it later.
         String regex = "/\\*.*\\*/";
         Pattern pattern = Pattern.compile(regex);
         Matcher matcher = pattern.matcher(page);
        page = matcher.replaceAll("");
        String className = line.split("\\.")[0];
        // Creating compiler target file
        File compiled = new File(className+".java");
        compiled.createNewFile();
        //splitting the file on semicolons: to change later
        String[] lines = page.split(";");
//        for(var i: lines) System.out.println(i);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(compiled)));
        //boiler plate
        out.write("public class "+className+" {\n");
        out.write("\t public static void main(String[] args) {\n");
        for (String s : lines)
            out.write(evaluateStatement(s) + ";\n");

        // Writing to the file
        //creating wrapper text for class and main

        // Creating actual java program

        //Splitting the input into the tokens, and statements to be evaluated, look for keywords: loop, for, while until '}'
       // String[] code = page.split("\\.");
        //for(String i: code)
          //  out.print("\t\t"+evaluate(i)+"\n");
        //first split on {}, {} blocks are the most important in terms of precedence
        //String[] code = bracketSplit(page);

        // removing comments regexp
        // String s = "this is code and /* this is a comment and */
        // within code comments are not read";
        // while (m.find()) {
        // s = m.replaceAll("");
        // m = p.matcher(s);
        // }
        // System.out.println(s);
         //

        // Ending wrapper text
        out.write("\t}\n");
        out.write("}\n");
        br.close();
        out.close();
        System.out.println("Compiled "+line+" => "+compiled.getName());
    }

    /*
    * Our readline function
    * Goes through every line in the file and adds it to a string result, then returns the result
     */
    public static String readLines(String filename) throws IOException {
        BufferedReader br  = new BufferedReader(new FileReader(filename));
        StringBuilder page = new StringBuilder();
        String line;
        while((line = br.readLine()) != null)
            //if the line is not a comment
            if(line.charAt(0) != '/')
                page.append(line);
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
        inputLine = inputLine.trim();
        String expression = "";
        // let x: mut int = 5 -> ["let", "x:", "mut", "int", "=", "5"]
        String[] tokens = inputLine.split(" ");
        //checks if the statement is assignment
        if(tokens[0].equals("let")) {
            String name = "";
            for(int i = inputLine.indexOf(" ")+1; (i<inputLine.length() && i>=0) && (inputLine.charAt(i) != ' ' && inputLine.charAt(i) != ':') ; i++) {
                name+=inputLine.charAt(i);
            }
            //looks for type
            if(tokens[1].charAt(tokens[1].length()-1) == ':' && !tokens[3].equals("=")) {
                //if variable is declared mutable
                //let x: mut int = 5;
                if (tokens[2].equals("mut")) {
                    expression += (tokens[3] + " " + tokens[1].substring(0, tokens[1].length()-1)+" ");
                    for(int i = 4; i<tokens.length; i++)
                        expression+=(tokens[i]+" ");

                }
                //if variable is not explicitly declared mutable
                //let x: int = 5;
                else {
                    expression += ("final " + tokens[2]+" "+tokens[1].substring(0, tokens[1].length()-1)+" ");
                    for(int i = 3; i< tokens.length; i++)
                        expression+=(tokens[i]+" ");
                }
            }
            //if no type given
            else {
                System.out.println(tokens[2]);
                // if the variable is declared mutable
                // let x: mut = 5;
                if(tokens[2].equals("mut")) {
                    expression += ("var " + tokens[1].substring(0, tokens[1].length() - 1)+" ");
                    for(int i = 3; i<tokens.length; i++)
                        expression+=(tokens[i]+" ");
                }
                // if the variable is not explicitly declared mutable
                // let x = 5;
                else {
                    System.out.println("here");
                    expression += ("final var " + tokens[1]+" ");
                    for(int i = 2; i<tokens.length; i++)
                        expression+=(tokens[i]+" ");
//                    System.out.println(expression);
                }
            }

            //just some array stuff
            //if the array is declared normally like let z = int[x];, just plop a new in front of the int[ and everything is good
            if(expression.contains("[") && !expression.contains(",")) {

                int equals = expression.indexOf("=");
                expression = expression.substring(0, equals+1)+" new"+expression.substring(equals+1);
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
				//if(lambda.contains("=>")) {
					String[] lambdas = lambda.split("=>");
					String iterationVar = lambdas[0].replace(":", "").trim();
					//need to get the variable between [] in expression
					String size = "";
					int firstSquareBracket = expression.indexOf("[")+1;
					while(expression.charAt(firstSquareBracket) != ']') {
                        size+=expression.charAt(firstSquareBracket);
						firstSquareBracket++;
					}
                    //somehow turning the lambdas into a for loop, idk???????
					expression+=";\nfor(int "+iterationVar+"= 0; "+iterationVar+"<"+size.substring(0,size.length())+"; "+iterationVar+"++) "+name+"["+ iterationVar +"] ="+lambdas[1].substring(0, lambdas[1].length()-1);

				//}

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
                int equals = expression.indexOf("=");
                expression = expression.replace("[", "{").replace("]", "}");
            }
        }
        return expression;
    }
    public static String[] bracketSplit(String input) {
        int openingBrackets = 0, closingBrackets = 0;
        for(int i = 0; i < input.length(); i++) {
            //
        }
        return new String[]{"hi"};
    }
}
