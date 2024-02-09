// Card.java for Palace by Sohum Berry
public class Card {
    private int point;
    private String suit;
    private String rank;
    // Constructor
    public Card(String suit, String rank, int point) {
        this.point = point;
        this.rank = rank;
        this.suit = suit;
    }

    // Getters
    public String getSuit() {
        return suit;
    }
    public String getRank() {
        return rank;
    }
    public int getPoint() {
        return point;
    }

    // Setters
    public void setSuit(String newSuit) {
        suit = newSuit;
    }
    public void setRank(String newRank) {
        rank = newRank;
    }
    public void setPoint(int newPoint) {
        point = newPoint;
    }

    // Determines which card is the bigger one
    public boolean isGreater(Card other) {
        return other == null || this.point >= other.point;
    }
    // Determines if a card is the same rank as another
    public boolean equals(Card card) {
        return card.rank.equals(this.rank);
    }
    @Override
    public String toString() {
        return rank;
    }
}
