package pz1.poker.server;

import pz1.poker.common.ActionType;
import pz1.poker.common.PassedTokens;
import pz1.poker.common.Player;
import pz1.poker.common.ServerOptions;
import pz1.poker.model.Game;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**Class helping Server with messages etc.**/
public class ServerManager {
    /**Tokens object used in class.**/
    private static final PassedTokens passedTokens = new PassedTokens();

    /**Method used to operate turn correctly.
     * @param id id of Player that made move.
     * @param server Server Game operates on.**/
    protected static void turnHandler(Server server, int id){
        for(Player player: server.game.getPlayers()){
            if(player.getId()==id && id==server.game.getPlayers().size()){
                server.game.setNext(server.game.getPlayers().get(0).getId());
            }
            else if(player.getId() == id){
                server.game.setNext(server.game.getPlayers().get(id).getId());
            }
        }
    }

    /**Method handling Clients request to connect.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onConnect(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.CONNECT);
        passedTokens.setActionParameters("Connected. Your role: "+server.game.getPlayers().get(id-1).getRole()+" Your ID: "+
                server.game.getPlayers().get(id-1).getId());
        turnHandler(server, id);
        server.send(key, passedTokens);
        System.out.println("Client "+ id +" connected");
        server.game.setStartingBalance(id);
        server.connections++;
        if(server.connections==ServerOptions.MAX_ID){
            turnHandler(server, server.game.getDealer().getId());
        }
    }

    /**Method handling Clients request to deal.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onDeal(SelectionKey key, int id, Server server) throws IOException {
        server.game.anteHandler(id);
        server.game.dealerHandler();
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.CHECK_HAND);
        passedTokens.setActionParameters("Cards dealt");
        turnHandler(server, id);
        server.send(key, passedTokens);
    }

    /**Method handling Clients request to check hand first time.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onFirstCheckHand(SelectionKey key, int id, Server server) throws IOException {
        onCheckHand(key, id, server);
        turnHandler(server, id);
    }

    /**Method handling Clients request to check cards on hand.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onCheckHand(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.CHECK_HAND);
        passedTokens.setActionParameters(server.game.getPlayers().get(id-1).showHand());
        server.send(key, passedTokens);
    }

    /**Method handling Clients move not in his turn.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onIncorrectTurn(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.NOT_YOUR_TURN);
        passedTokens.setActionParameters("It's not your turn.");
        server.send(key, passedTokens);
    }

    /**Method handling Clients request to disconnect.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onDisconnect(SelectionKey key, int id, Server server) throws IOException {
        server.connectedClients--;
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.DISCONNECT);
        passedTokens.setActionParameters("You've been disconnected");
        System.out.println("Client "+ id +" disconnected");
        turnHandler(server, id);
        server.send(key, passedTokens);
        key.channel().close();
        if(server.connectedClients==0){
            server.game = new Game();
            server.game.setNext(-1);
            Server.setMap(new HashMap<>());
            server.connections = 0;
            server.finishedCount = 0;
            server.finished = false;
            server.buffer.clear();
        }
    }

    /**Method handling Clients request to bet.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.
     * @param bet amount of coins Player wants to bet.**/
    protected static void onBet(SelectionKey key, int id, Server server, String bet) throws IOException {
        passedTokens.setPlayerID(id);
        if(server.game.betHandler(Integer.parseInt(bet), id)){
            passedTokens.setActionType(ActionType.BET);
            passedTokens.setActionParameters("Bet is placed.");
            turnHandler(server, id);
        }
        else{
            passedTokens.setActionType(ActionType.NOT_YOUR_TURN);
            passedTokens.setActionParameters("Invalid bet. Current bet: "+server.game.getCurrentBet()+" Your current " +
                    "bet: "+server.game.getPlayers().get(id-1).getCurrentRoundBet()+" Your balance: "+
                    server.game.getPlayers().get(id-1).getCoins());
        }
        server.send(key, passedTokens);
    }

    /**Method handling Clients request to check.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onCheck(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.CHECK);
        if(!server.game.checkHandler(id).equals("You checked")){
            passedTokens.setActionParameters(server.game.checkHandler(id));
        }
        else{
            passedTokens.setActionParameters(server.game.checkHandler(id));
            server.game.getPlayers().get(id-1).setChecked(true);
            turnHandler(server, id);
        }
        server.send(key, passedTokens);
    }

    /**Method handling Clients request to go to next round.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onNextRound(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.NEXT_ROUND);
        if(server.game.roundOfBetsFinished(id)){
            server.game.setNextRoundCounter(server.game.getNextRoundCounter()+1);
            passedTokens.setActionParameters("Round of bets is finished.");
            turnHandler(server, id);
        }
        else{
            passedTokens.setActionParameters("You can't continue now.");
            passedTokens.setActionType(ActionType.NOT_YOUR_TURN);
        }
        server.send(key, passedTokens);
        if(server.game.getNextRoundCounter()== ServerOptions.MAX_ID){
            server.game.setNext(2);
            server.game.allPlayersFinishedBets();
        }
    }

    /**Method handling Clients request to bet ante.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onAnte(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.ANTE);
        passedTokens.setActionParameters("Ante bet.");
        server.send(key, passedTokens);
        turnHandler(server, id);
        server.game.anteHandler(id);
    }

    /**Method handling Clients request to trade.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.
     * @param cardsToTrade String containing Number of cards on hand player wants to trade.**/
    protected static void onTrade(SelectionKey key, int id, Server server, String cardsToTrade) throws IOException {
        server.game.getPlayers().get(id-1).setChecked(false);
        server.game.playerFinishedBets(id);
        server.game.setNextRoundCounter(0);
        List<Integer> buffer = convertStringToList(cardsToTrade);
        passedTokens.setPlayerID(id);
        if(server.game.tradeHandler(buffer, id)){
            passedTokens.setActionType(ActionType.TRADE);
            passedTokens.setActionParameters("Cards traded. "+server.game.getPlayers().get(id-1).showHand());
            turnHandler(server, id);
        }
        else{
            passedTokens.setActionType(ActionType.NOT_YOUR_TURN);
            passedTokens.setActionParameters("Trade request invalid.");
        }
        server.send(key, passedTokens);
    }

    /**Method handling Clients request to stand pot.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    protected static void onStandPot(SelectionKey key, int id, Server server) throws IOException {
        server.game.getPlayers().get(id-1).setChecked(false);
        server.game.playerFinishedBets(id);
        server.game.setNextRoundCounter(0);
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.STAND_POT);
        passedTokens.setActionParameters("No Cards were traded. Round of bets started.");
        server.send(key, passedTokens);
        turnHandler(server, id);
    }

    /**Helper method converting string to List
     * @param input String converted
     * @return List<Integer> after converting.**/
    private static List<Integer> convertStringToList(String input) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char digitChar = input.charAt(i);
            int digit = Character.getNumericValue(digitChar);
            result.add(digit);
        }
        return result;
    }

    /**Method handling Clients request to show hand to table.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    public static void onShowHand(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setActionType(ActionType.SHOW_HAND);
        passedTokens.setPlayerID(id);
        passedTokens.setActionParameters(server.game.evaluateHand(id));
        server.send(key, passedTokens);
        turnHandler(server, id);
    }

    /**Method handling Clients request to check game result.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    public static void onShowResult(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.SHOW_RESULT);
        passedTokens.setActionParameters(server.game.chooseWinner());
        if(server.game.getWinnerID()==id){
            passedTokens.setActionParameters("You Won "+server.game.getPot()+" coins!");
        }
        server.send(key, passedTokens);
        turnHandler(server, id);
    }

    /**Method handling Clients request to fold.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    public static void onFold(SelectionKey key, int id, Server server) throws IOException {
        server.game.setFoldCounter(server.game.getFoldCounter()+1);
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.FOLD);
        passedTokens.setActionParameters("Folded");
        if(server.game.getFoldCounter()==ServerOptions.MAX_ID-1){
            server.finished = true;
            server.finishedCount = ServerOptions.MAX_ID;
        }
        server.send(key, passedTokens);
        turnHandler(server, id);

    }

    /**Method handling Clients request to wait for betting round to finish.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    public static void onWait(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.WAIT);
        passedTokens.setActionParameters("waiting");
        server.send(key, passedTokens);
        turnHandler(server, id);
    }

    /**Method handling Clients request to move to next round.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    public static void onNextRoundFolded(SelectionKey key, int id, Server server, String param) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionType(ActionType.NEXT_ROUND_FOLDED);
        if(!param.isEmpty()){
            if(server.game.getNextRoundCounter()!=0 || server.game.getPlayers().get(id-1).getId()==server.game.getDealer().getId()+1){
                passedTokens.setActionParameters("Next");
                turnHandler(server, id);
                server.game.setNextRoundCounter(server.game.getNextRoundCounter()+1);
                if(server.game.getNextRoundCounter()== ServerOptions.MAX_ID){
                    server.game.setNext(2);
                    server.game.allPlayersFinishedBets();
                }
            }
            else{
                passedTokens.setActionType(ActionType.NOT_YOUR_TURN);
                passedTokens.setActionParameters("You can't continue now");
            }
        }
        else{
            passedTokens.setActionParameters("Next");
            turnHandler(server, id);
        }
        server.send(key, passedTokens);
    }

    /**Method handling Clients request to go to showing hand after all but one folded.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Assigned to channel.
     * @param server Server that Client sent message to.
     * @param id ID of Player that sent message.**/
    public static void onFinished(SelectionKey key, int id, Server server) throws IOException {
        passedTokens.setPlayerID(id);
        passedTokens.setActionParameters("Game Finished");
        passedTokens.setActionType(ActionType.FINISHED);
        server.send(key, passedTokens);
        turnHandler(server, id);
        server.finishedCount--;
    }
}
