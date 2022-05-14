import java.util.regex.*;
public class test {
    public static void main(String[] args) {
        Pattern objectNotString = Pattern.compile("[a-zA-Z\\d$_-]\\(+[^~]*'[^']*'");
        Matcher matcher = objectNotString.matcher("let x = \'c\';");
        Matcher matcher1 = objectNotString.matcher("let x = Object(5, \"string\");");
        System.out.println("Hello 123832".replaceAll("\\d", ""));
        System.out.println("hello 123832".contains("\\d"));
    }
}