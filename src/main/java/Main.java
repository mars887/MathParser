import java.text.ParseException;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws ParseException {
        Scanner scan = new Scanner(System.in);
        int prest = 100;
        while(true) {
            System.out.println(Parser.parse(scan.nextLine(),prest).toString(true));
        }
    }
}
