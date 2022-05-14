import java.util.*;
import java.util.regex.*;
public class test {
//    public static void main(String[] args) {
//        Pattern objectNotString = Pattern.compile("[a-zA-Z\\d$_-]\\(+[^~]*'[^']*'");
//        Matcher matcher = objectNotString.matcher("let x = \'c\';");
//        Matcher matcher1 = objectNotString.matcher("let x = Object(5, \"string\");");
//        System.out.println("Hello 123832".replaceAll("\\d", ""));
//        System.out.println("hello 123832".contains("\\d"));
//    }
    private int[][] grid;
    private test obj;
    private ArrayList<String> al;
    private HashSet<String> set;
    private HashMap<String, Integer> map;

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public int[][] getGrid() {
        return grid;
    }

    public test getObj() {
        return obj;
    }

    public void setObj(test obj) {
        this.obj = obj;
    }

    public ArrayList<String> getAl() {
        return al;
    }

    public void setAl(ArrayList<String> al) {
        this.al = al;
    }

    public HashSet<String> getSet() {
        return set;
    }

    public void setSet(HashSet<String> set) {
        this.set = set;
    }

    public HashMap<String, Integer> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Integer> map) {
        this.map = map;
    }
}