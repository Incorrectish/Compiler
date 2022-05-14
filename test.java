import java.io.*;
public class test {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("toCompile.txt"));
        StringBuilder pageBuilder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            if ((line = line.trim()).length() >= 2 && (line.charAt(0) != '/' || line.charAt(1) != '/')) {
                pageBuilder.append(line).append("\n");
            }
        }
        System.out.println(pageBuilder.toString());
    }
}