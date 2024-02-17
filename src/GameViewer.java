// GameViewer.java for Palace by Sohum Berry

import javax.swing.*;
import java.awt.*;

public class GameViewer extends JFrame {
    public static final int WINDOW_WIDTH = 1480,
                            WINDOW_HEIGHT = 870;
    Game game;
    Player[] players;
    Deck pile;
    Deck gameDeck;
    boolean isDeckEmpty;
    int winState;


    public GameViewer(Game game) {
        this.game = game;
        this.players = game.getPlayers();
        this.pile = game.getPile();
        this.gameDeck = game.getGameDeck();
        isDeckEmpty = !gameDeck.isEmpty();
        winState = game.getWonState();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Palace");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        g.setColor(Color.BLACK);
        Font winFont = new Font("Serif", Font.BOLD, 80);
        if (winState == Game.UNFINISHED) {
            // 1. Draw the gameDeck
            if (!gameDeck.isEmpty()) {
                g.drawImage(Game.back, Game.GAME_DECK_X, Game.GAME_DECK_Y, Game.CARD_WIDTH, Game.CARD_HEIGHT,
                        this);
            }
            else {
                g.setColor(new Color(10, 115, 25));
                g.fillRect(Game.GAME_DECK_X, Game.GAME_DECK_Y, Game.CARD_WIDTH, Game.CARD_HEIGHT);
            }
            // 2. Draw the number of cards left
            g.drawString(gameDeck.getCardsLeft() + "\nCards Left", Game.GAME_DECK_X - 4,
                    Game.GAME_DECK_Y + Game.CARD_HEIGHT + 15);
            // 3. Draw the pile
            if (!pile.isEmpty()) {
                pile.getCard(pile.size()-1).draw(g, true, Game.PILE_X, Game.PILE_Y, this);
            }
            else {
                g.setColor(new Color(10, 115, 25));
                g.fillRect(Game.PILE_X, Game.PILE_Y, Game.CARD_WIDTH, Game.CARD_HEIGHT);
            }
            // 4. Draw the player's hands
            for (Player player : players) {
                player.draw(g, this);
            }
        } else {
            g.setFont(winFont);
            g.drawString(players[winState - 1].getName() + " Won!", WINDOW_WIDTH / 2 - 70, WINDOW_HEIGHT / 2);
        }
    }
}
