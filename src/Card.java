public class Card {
    private int point;
    private String suit;
    private String rank;

    public Card(String suit, String rank, int point) {
        this.point = point;
        this.rank = rank;
        this.suit = suit;
    }

    public void setSuit(String newSuit) {
        suit = newSuit;
    }

    public String getSuit() {
        return suit;
    }

    public void setRank(String newRank) {
        rank = newRank;
    }
    public String getRank() {
        return rank;
    }

    public void setPoint(int newPoint) {
        point = newPoint;
    }
    public int getPoint() {
        return point;
    }

    public boolean isGreater(Card other) {
        return this.point >= other.point;
    }

    public boolean equals(Card card) {
        return card.point == this.point;
    }
    @Override
    public String toString() {
        return rank;
    }
}
