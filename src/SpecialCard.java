import java.awt.*;

public class SpecialCard extends Card {
    private boolean doesClear;
    public SpecialCard(String suit, String rank, int point, Image face, boolean doesClear) {
        super(suit, rank, point, face);
        this.doesClear = doesClear;
    }

    public boolean isDoesClear() {
        return doesClear;
    }
}
