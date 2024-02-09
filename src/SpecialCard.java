public class SpecialCard extends Card {
    private boolean doesClear;
    public SpecialCard(String suit, String rank, int point, boolean doesClear) {
        super(suit, rank, point);
        this.doesClear = doesClear;
    }

    public boolean isDoesClear() {
        return doesClear;
    }
}
