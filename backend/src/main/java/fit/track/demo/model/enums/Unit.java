package fit.track.demo.model.enums;

public enum Unit {
    GRAM("g", true),
    MILLILITER("ml", false),
    PIECE("pcs", false);

    private final String symbol;
    private final boolean massBased;

    Unit(String symbol, boolean massBased) {
        this.symbol = symbol;
        this.massBased = massBased;
    }

    public String getSymbol() { return symbol; }
    public boolean isMassBased() { return massBased; }
}
