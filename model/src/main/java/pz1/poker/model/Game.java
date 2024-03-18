package pz1.poker.model;

import lombok.Getter;
import lombok.Setter;
import pz1.poker.common.Card;
import pz1.poker.common.Deck;
import pz1.poker.common.Player;
import pz1.poker.common.ServerOptions;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Game {
    private List<Player> players;
    private static List<Card> cardsTrade;
    private Deck gameDeck;
    private int pot = 0;
    private static final int ANTE = 50;
    private Dealer dealer;
    private int currentBet;
    int next;
    private ResultType winnerRes = ResultType.DEFAULT;
    private int winnerID = 0;
    private Card[] winnerHand;
    private int nextRoundCounter;
    protected int foldCounter;

    public Game(){
        nextRoundCounter = 0;
        currentBet = 0;
        foldCounter = 0;
        players = new ArrayList<>();
        cardsTrade = new ArrayList<>();
        dealer = new Dealer();
        gameDeck = new Deck();
        gameDeck.shuffle();
        players.add(dealer);
        winnerHand = new Card[5];
        for(int i = 1; i< ServerOptions.MAX_ID; i++){
            Player player = new Player();
            players.add(player);
        }
        next = dealer.getId()+1;
    }

    public void setStartingBalance(int id){
        for(Player player: players){
            if(player.getId()==id){
                player.setCoins(1000);
            }
        }
    }

    public void anteHandler(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                player.setCoins(player.getCoins() - ANTE);
                pot += ANTE;
            }
        }
    }

    public boolean tradeHandler(List<Integer> buffer, int id) {
        if(buffer.size()>3){
            return false;
        }
        Player player1 = new Player();
        for (Player player : players) {
            if (player.getId() == id) {
                player1 = player;
            }
        }
        dealer.handleTrade(buffer, player1, cardsTrade, gameDeck);
        return true;
    }

    public void dealerHandler(){
        dealer.dealCards(players, gameDeck);
    }

    public String checkHandler(int id){
        Player player1 = new Player();
        for (Player player : players) {
            if (player.getId() == id) {
                player1 = player;
            }
        }
        if(this.currentBet==0 && !player1.isChecked()){
            return "You checked";
        }
        else{
            return "You can't check now!";
        }
    }

    public boolean betHandler(int bet, int id){
        for (Player player : players) {
            if(player.getId()==id && bet+player.getCurrentRoundBet()<this.currentBet){
                return false;
            }
            if(player.getId()==id && player.getCoins()<bet){
                return false;
            }
            if(player.getId()==id && player.getCurrentRoundBet()==this.currentBet && this.currentBet!=0){
                return false;
            }
            if(player.getId() == id && player.getCurrentRoundBet() == this.currentBet && player.isChecked()){
                return false;
            }
            if (player.getId() == id) {
                player.setCurrentRoundBet(bet+player.getCurrentRoundBet());
                player.setCoins(player.getCoins()-bet);
                pot+=bet;
                this.currentBet = player.getCurrentRoundBet();
            }
        }
        return true;
    }

    public boolean roundOfBetsFinished(int id) {
        Player player1 = new Player();
        for (Player player : players) {
            if (player.getId() == id) {
                player1 = player;
            }
        }
        return player1.getCurrentRoundBet() == this.getCurrentBet() && (player1.getCurrentRoundBet()!=0 || player1.isChecked());
    }

    public void allPlayersFinishedBets(){
        this.currentBet=0;
    }

    public void playerFinishedBets(int id){
        Player player1 = new Player();
        for (Player player : players) {
            if (player.getId() == id) {
                player1 = player;
            }
        }
        player1.setCurrentRoundBet(0);
    }

    public String evaluateHand(int id){
        Player player1 = new Player();
        for (Player player : players) {
            if (player.getId() == id) {
                player1 = player;
            }
        }
        if (HandClassifier.handEvaluator(player1.getPlayerHand()).ordinal()>this.winnerRes.ordinal()){
            winnerRes = HandClassifier.handEvaluator(player1.getPlayerHand());
            winnerID = id;
            winnerHand = player1.getPlayerHand();
        }
        else if(HandClassifier.handEvaluator(player1.getPlayerHand()).ordinal()==this.winnerRes.ordinal()){
            if(HandClassifier.chooseWinner(player1.getPlayerHand(), winnerHand) == 2){
                winnerRes = HandClassifier.handEvaluator(player1.getPlayerHand());
                winnerID = id;
                winnerHand = player1.getPlayerHand();
            }
        }
        return HandClassifier.handEvaluator(player1.getPlayerHand()).toString();
    }

    public String chooseWinner(){
        return "Winner is "+this.winnerID+" with result "+this.winnerRes;
    }
}
