package pz1.poker.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

/**Custom JSONParser used to convert tokens to strings, strings to tokens or extract certain tokens.**/
@Data
@NoArgsConstructor
public class MyJSONParser {
    /**Method used to parse string to PassedTokens.
     * @param string String to parse.
     * @return PassedTokens passed between Client and Server.**/
    public static PassedTokens parse(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, PassedTokens.class);
    }

    /**Method used to stringify PassedTokens.
     * @param passedTokens Tokens passed between Client and Server.
     * @return ByteBuffer that is i.e. partially printed for Client.**/
    public static ByteBuffer stringify(PassedTokens passedTokens){
        Gson gson = new Gson();
        String message = gson.toJson(passedTokens);
        return ByteBuffer.wrap(message.getBytes());
    }

    /**Method used to extractID from string.
     * @param responseStr String that Client/Server received.
     * @return int containing Player's ID.**/
    public static int extractID(String responseStr){
        JsonObject jsonObject = JsonParser.parseString(responseStr).getAsJsonObject();
        return Integer.parseInt(jsonObject.get("playerID").getAsString());
    }

    /**Method used to extract action from received String.
     * @param args Received String.
     * @return String containing ActionType**/
    public static String getAction(String args){
        JsonObject jsonObject = JsonParser.parseString(args).getAsJsonObject();
        return jsonObject.get("actionType").getAsString();
    }

    /**Method used to extract actionParameters from received String.
     * @param args Received String.
     * @return String containing Parameters**/
    public static String extractParameters(String args){
        JsonObject jsonObject = JsonParser.parseString(args).getAsJsonObject();
        return jsonObject.get("actionParameters").getAsString();
    }
}
