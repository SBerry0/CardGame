import java.util.ArrayList;

public class Player {
    private String name;
    private Deck hand;
    private Deck hiddenHand;
    private Deck topHand;
    // Holds the player's phase. 1=mainGame, 2=topGame, 3=endGame
    private int gamePhase;

    public Player(String name) {
        this.name = name;
        this.hand = new Deck(false);
        this.topHand = new Deck(false);
        this.hiddenHand = new Deck(false);
        this.gamePhase = 1;
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

    public int getGamePhase() {
        return gamePhase;
    }

    public void setTopPhase() {
        gamePhase = 2;
    }

    public void setEndPhase() {
        gamePhase = 3;
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
        out += "Hand:\n";
        if (!hand.isEmpty() && hand.isVisible()) {
            out += hand.toString();
        }
        else {
            out += asterize(hand.size());
        }
        out += "\nPalace:\n";
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
        return out;
    }
}
