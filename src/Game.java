import java.util.Scanner;

public class Game {
    private final String[] ordering = {"first", "second", "third", "fourth", "fifth", "sixth"};
    private Deck gameDeck;
    private Deck pile;
    private Player[] players;
    private int numPlayers;
    public Game() {
        pile = new Deck(true);
        gameDeck = new Deck(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
                new String[]{"Hearts", "Spades", "Diamonds", "Clubs"},
                new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"});
        gameDeck.shuffle();
        pile.addCard(gameDeck.deal());
        Scanner s = new Scanner(System.in);
        do {
            System.out.println("How many people are playing?");
            numPlayers = s.nextInt();
            s.nextLine();
        } while (numPlayers < 2 || numPlayers > 6);
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("What's the " + ordering[i] + " player's name?");
            String name = s.nextLine();
            players[i] = new Player(name);
        }
    }

    public void deal() {
        for (int i = 0; i < numPlayers; i++) {
            gameDeck.shuffle();
            for (int j = 0; j < 6; j++) {
                players[i].getHand().addCard(gameDeck.deal());
            }
            for (int j = 0; j < 3; j++) {
                players[i].getHiddenHand().addCard(gameDeck.deal());
            }
        }
    }

    public void shufflePlayers() {
        for (int i = 0; i < numPlayers; i++) {
            int remaining = numPlayers - i;

            int r = (int) (Math.random() * remaining);
            if (r == remaining) {
                break;
            }
            Player temp = players[i];
            players[i] = players[r];
            players[r] = temp;
        }
    }

    public void initGame() {
        shufflePlayers();
        for (Player player :
                players) {
//            player.getHand().toggleVisibility();

        }
    }

    public void printGame() {
        System.out.println("___________________________________");
        // OR, in the actual terminal to clear screen:
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        for (int i = 0; i < numPlayers - 1; i++) {
            System.out.println(players[i]);
            System.out.println("\n");
        }
        // In order to have the right spacing
        System.out.println(players[players.length - 1]);
        System.out.println("\n\nTop Card: " + pile.getTopCard());
    }

    public int playCard(Player player, Card card) {
        // Take from player's hand and make it the top card after checking for special cases
        if (player.hasCard(card)) {
            if (card.isGreater(pile.getTopCard())) {
                player.getHand().takeCard(card);
                pile.addCard(new Card(card.getSuit(), card.getRank(), card.getPoint()));
                // TODO: Check for special cards 2 and 10
                return 0;
            }
            return 2;
        }
        return 1;
    }

    public void play() {
        deal();
        initGame();
        printGame();
        // loop through players and play a card each time.

    }
    public static void main(String[] args) {
        Game g = new Game();
        g.play();
    }
}
