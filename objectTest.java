import java.util.regex.*;
import java.util.*;
import java.io.*;
public class objectTest {
private int  speed;

public int getInt() {
return  speed;
}

public void setInt(int  speed) {
this.speed =  speed;
}

private String name;

public String getString() {
return name;
}

public void setString(String name) {
this.name = name;
}

private Pattern  regex;

public Pattern  getPattern () {
return regex;
}

public void setPattern (Pattern  regex) {
this.regex = regex;
}

public objectTest (int  speed, String name, Pattern regex) {
this.speed =  speed;
this.name = name;
this.regex = regex;
}

public String toString() {
return speed+" "+name;
}
public int speedUp() {
return ++speed;
}
}