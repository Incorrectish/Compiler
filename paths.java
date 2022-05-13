public class paths {
    public static void main(String[] args) {
        final int PATH_LENGTH = 15;
        String x = "", y = "";
        for(int i = 0; i<PATH_LENGTH*2; i++)
            x+=" ";
        for(int i = 0; i<PATH_LENGTH; i++) {
            System.out.println(y+"\\ \\" + x + "/ /");
            x = x.substring(2);
            y+=" ";
        }
        for(int i = 0; i<PATH_LENGTH; i++)
            System.out.println(y+"|    |");
    }
}
