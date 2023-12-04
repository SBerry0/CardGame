import java.util.Scanner;

public class Game {
    private final String[] ordering = {"first", "second", "third", "fourth", "fifth", "sixth"};
    private Deck gameDeck;
    private Deck discard;
    private Card topCard;
    private Player[] players;
    private int numPlayers;
    public Game() {
        discard = new Deck(true);
        gameDeck = new Deck(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
                new String[]{"Hearts", "Spades", "Diamonds", "Clubs"},
                new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"});
        gameDeck.shuffle();
        topCard = gameDeck.deal();
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
                players[i].addCard(gameDeck);
            }
            for (int j = 0; j < 3; j++) {
                players[i].addHiddenCard(gameDeck);
            }
        }
    }

    public void printGame() {
        System.out.println("__________________________________________________");
        // OR, in the actual terminal to clear screen:
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        for (int i = 0; i < numPlayers - 1; i++) {
            players[i].printHand();
            System.out.println("\n");
        }
        // In order to have the right spacing
        players[players.length - 1].printHand();
        System.out.println("\nTop Card: " + topCard);
    }

    public boolean playCard(Player player, Card card) {
        if (player.hasCard(card)) {
            if (card.isGreater(topCard)) {
                // take from player's hand and make it the top card after checking for special cases
            }
        }
        return false;
    }

    public void play() {
        deal();
        printGame();
        // alternate between players and play a card each time.
    }
    public static void main(String[] args) {
        Game g = new Game();
        g.play();
    }
}
