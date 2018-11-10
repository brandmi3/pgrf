package drawables;

public enum FillType {
    //  LINE("kreslení čáry"),
    SEED_FILL("seed fill"),
    SCAN_LINE("scan line");

    private String popis;

    FillType(String popis) {
        this.popis = popis;
    }

    public String getPopis() {
        return popis;
    }
}
