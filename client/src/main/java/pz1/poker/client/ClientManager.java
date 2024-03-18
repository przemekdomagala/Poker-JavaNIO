package pz1.poker.client;

import pz1.poker.common.PlayerType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Class used to help manage Client's input and received signals.**/
public class ClientManager {

    /**Method used to extract Player role from received from Server signal.
     * @param input String that Server sends to Client after it connects.
     * @return Method returns PlayerType (DEALER or PLAYER)**/
    public static PlayerType extractPlayerRole(String input) {
        Pattern pattern = Pattern.compile("Your role: (\\w+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            if(matcher.group(1).equals("PLAYER")){
                return PlayerType.PLAYER;
            }
            else if(matcher.group(1).equals("DEALER")){
                return PlayerType.DEALER;
            }
        } else {
            return null;
        }
        return null;
    }

    /**Method used to extract 'bet' or 'trade' part from command i.e. 'trade 3' or 'bet 150'
     * @param command Command that Client input.
     * @return String that is extracted part from command.**/
    public static String extract(String command){
        String[] parts = command.split("\\s+");
        for (String part : parts) {
            if (part.equals("bet") || part.equals("trade")) {
                return part;
            }
        }
        return "";
    }

    /**Method used to extract amount part from command i.e. 'trade 3' or 'bet 150'
     * @param command Command that Client input.
     * @return int that is extracted part from command.**/
    public static int amount(String command) {
        Pattern pattern = Pattern.compile("\\b(\\d+)\\b");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

}
