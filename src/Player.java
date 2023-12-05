import java.util.ArrayList;

public class Player {
    private String name;
    private Deck hand;
    private Deck hiddenHand;
    private Deck topHand;

    public Player(String name) {
        this.name = name;
        this.hand = new Deck(false);
        this.topHand = new Deck(false);
        this.hiddenHand = new Deck(false);
    }
    public String getName() {
        return name;
    }

    public Deck getHand() {
        return hand;
    }
    public Deck getHiddenHand() {
        return hiddenHand;
    }

    public Deck getTopHand() {
        return topHand;
    }

    public boolean hasCard(Card card) {
        if (hiddenHand.isVisible()) {
            return hiddenHand.hasCard(card) || hand.hasCard(card);
        }
        return hand.hasCard(card);
    }

    public String asterize(int count) {
        String out = "";
        for (int i = 0; i < count; i++) {
            out += "* ";
        }
        return out;
    }

    @Override
    public String toString() {
        String out = name + ":\n";
        out += "Hand: ";
        if (!hand.isEmpty() && hand.isVisible()) {
            out += hand.toString();
        }
        else {
            out += asterize(hand.size());
        }
        out += "\n";
        if (!topHand.isEmpty() && topHand.isVisible()) {
            out += topHand.toString();
        }
        else {
            out += asterize(topHand.size());
        }
        out += "\n";
        if (!hiddenHand.isEmpty() && hiddenHand.isVisible()) {
            out += hiddenHand.toString();
        }
        else {
            out += asterize(hiddenHand.size());
        }
        return out.substring(0, out.length()-1);
    }
}
