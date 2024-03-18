package pz1.poker.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**Class representing deck of cards.**/
@Getter
public class Deck {
    /**Cards in deck.**/
    @Setter
    private Card[] cards;

    /**Constructor adding all cards to cards.**/
    public Deck(){
        cards = new Card[52];
        int counter = 0;
        for(Suit suit: Suit.values()){
            for(Rank rank: Rank.values()){
                cards[counter] = new Card(suit, rank);
                counter++;
            }
        }
    }

    /**Method used to shuffle deck.**/
    public void shuffle(){
        List<Card> buffer = new ArrayList<>();
        Collections.addAll(buffer, cards);
        Collections.shuffle(buffer);
        int counter = 0;
        for(Card card: buffer){
            cards[counter] = card;
            counter++;
        }
    }
}
