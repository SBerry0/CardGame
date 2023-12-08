import java.util.Scanner;

public class Game {
    private final String[] ordering = {"first", "second", "third", "fourth", "fifth", "sixth"};
    private Deck gameDeck;
    private Deck pile;
    private Player[] players;
    private int numPlayers;
    private Scanner s = new Scanner(System.in);

    public Game() {
        pile = new Deck(true);
        gameDeck = new Deck(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
                new String[]{"Hearts", "Spades", "Diamonds", "Clubs"},
                new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"});
        gameDeck.shuffle();
        pile.addCard(gameDeck.deal());
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

    public boolean canPlayCard(Deck hand) {
        for (int i = 0; i < hand.getCardsLeft(); i++) {
            if (hand.getCard(i).isGreater(pile.getCard())) {
                return true;
            }
        }
        return false;
    }

    public void playGame() {
        shufflePlayers();
        for (int j = 0; j < players.length; j++) {
            Player player = players[j];
            System.out.println(player.getName() + ", are you ready?");
            String ans = s.nextLine();
            player.getHand().toggleVisibility();
            printGame(player);
            // TODO: check if they can play a card
            if (canPlayCard(player.getHand())) {
                String[] indicesArray;
                int[] indices;
                boolean first = true;
                do {
                    if (!first) {
                        System.out.println("Invalid indices. Multiple cards must be of the same rank.");
                    }
                    System.out.println("Choose which card(s) you would like to play. (1-" + player.getHand().getCardsLeft() + ")");
                    String indicesString = s.nextLine();
                    indicesArray = indicesString.split(", ");
                    indices = new int[indicesArray.length];
                    for (int i = 0; i < indicesArray.length; i++) {
                        indices[i] = Integer.valueOf(indicesArray[i]) - 1;
                    }
                    first = false;
                } while (
                        indicesArray.length > player.getHand().getCardsLeft() ||
                                !isSameIndices(player.getHand(), indices)
                );
                for (int i = 0; i < indices.length; i++) {
                    boolean isPlayed = playCard(player, player.getHand().getCard(indices[i]));
                    if (!isPlayed) {
                        System.out.println("You can't play that card! Try again.");
                        j--;
                    }
                }
            }
            else {
                System.out.println("You cannot play a card greater than a " + player.getHand().getCard().toString());
                System.out.println("You get the pile! You now have " + pile.getCardsLeft() + player.getHand().getCardsLeft() + " cards");
                for (int i = 0; i < pile.getCardsLeft(); i++) {
                    player.getHand().addCard(pile.takeCard(pile.getCard(i)));
                }
            }
        }
    }

    public boolean isSameIndices(Deck hand, int[] indices) {
        for (int i = 0; i < indices.length; i++) {
            for (int j = 0; j < indices.length; j++) {
                if (!hand.getCard(i).equals(hand.getCard(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printGame() {
        System.out.println("___________________________________");
        // OR, in the actual terminal to clear screen:
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        for (int i = 0; i < numPlayers - 1; i++) {
            System.out.println(players[i]);
        }
        // In order to have the right spacing
        System.out.println(players[players.length - 1]);
        System.out.println("\n\nTop Card: " + pile.getCard());
    }

    public void printGame(Player player) {
        System.out.println("___________________________________");
        System.out.println(player);
        System.out.println("\n\nTop Card: " + pile.getCard());
    }

    public boolean playCard(Player player, Card card) {
        // Take from player's hand and make it the top card after checking for special cases
        if (player.hasCard(card)) {
            if (card.isGreater(pile.getCard())) {
                player.getHand().takeCard(card);
                pile.addCard(new Card(card.getSuit(), card.getRank(), card.getPoint()));
                // TODO: Check for special cards 2 and 10
                return true;
            }
        }
        return false;
    }

    public void play() {
        deal();
        playGame();
        printGame();
        // loop through players and play a card each time.

    }
    public static void main(String[] args) {
        Game g = new Game();
        g.play();
    }
}
