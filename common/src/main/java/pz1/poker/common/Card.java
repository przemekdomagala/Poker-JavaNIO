package pz1.poker.common;

import lombok.Getter;
import lombok.Setter;

/**Class representing Card Object in Poker game.**/
@Getter
@Setter
public class Card implements Comparable<Card>{
    /**Card's rank.**/
    protected Rank rank;
    /**Card's suit.**/
    protected Suit suit;

    /**Constructor used to create Card object.
     * @param rank Rank that we want created Card to have,
     * @param suit Suit that we want created Card to have**/
    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }

    /**Method returning string with Card's rank and suit used to print it.
     * @return String representing this Card.**/
    public String show(){
        return this.rank+"-"+this.suit;
    }

    /**Overridden method used to check whether to Card's are equal.
     * @param o Card that we compare this Card to.
     * @return boolean true when Cards are equal, false otherwise.**/
    @Override
    public boolean equals(Object o){
        if(this==o){
            return true;
        }
        if(o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if(!(((Card) o).suit.equals(this.suit))){
            return false;
        }
        return ((Card) o).rank == this.rank;
    }


    /**Overridden method used to compare two Cards.
     * @param o Card compared to this Card.
     * @return int 1 when this is bigger, 0 when Cards are equal, -1 otherwise.**/
    @Override
    public int compareTo(Card o) {
        if(this.rank.ordinal()>o.rank.ordinal()){
            return 1;
        }
        else if(this.rank.ordinal()<o.rank.ordinal()){
            return -1;
        }
        return 0;
    }

    /**Overridden method used to generate hash code of this card.
     * @return int this Card's hashcode.**/
    @Override
    public int hashCode() {
        int result = getRank().hashCode();
        result = 31 * result + getSuit().hashCode();
        result = 31 * result + getRank().hashCode();
        return result;
    }
}
