package pz1.poker.common;

import lombok.NoArgsConstructor;

import java.util.Scanner;

/**MyScanner class used in Application.**/
@NoArgsConstructor
public class MyScanner {
    /**Scanner that MyScanner class uses.**/
    private static final Scanner scanner = new Scanner(System.in);
    /**Wrapper for scanner's nextLine Method
     * @return String received in input.**/
    public static String nextLine(){
        return scanner.nextLine();
    }
    /**Wrapper for scanner's nextInt Method
     * @return Integer received in input.**/
    public static Integer nextInt(){ return Integer.parseInt(scanner.nextLine()); }
    /**Wrapper for scanner's hasNextInt Method
     * @return boolean checking whether passed input is parsable to integer.**/
    public static Boolean hasNextInt(){ return scanner.hasNextInt(); }
}
