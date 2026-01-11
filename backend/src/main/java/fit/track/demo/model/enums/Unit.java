package fit.track.demo.model.enums;

public enum Unit {

    GRAM("g"),
    MILLILITER("ml"),
    PIECE("pcs");

    private final String symbol;

    Unit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

