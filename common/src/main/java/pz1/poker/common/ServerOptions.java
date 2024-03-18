package pz1.poker.common;

/**Class representing Server Options.**/
public class ServerOptions {
    /**Private Constructor.**/
    private ServerOptions(){}
    /**Amount of Players that can Play the game (keep it between 2 and 4).**/
    public static int MAX_ID = 4;
    /**Number of currently connected clients to the Server.**/
    public static int connectedClients;
}
