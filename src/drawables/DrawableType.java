package drawables;

public enum DrawableType {

    //  LINE("kreslení čáry"),
    N_OBJECT("Nepravidelný Polygon - LM - Přidání bodu, drag - návrh čáry"),
    POLYGON("Pravidelný Polygon - 1. klik střed, 2.klik poloměr, 3. počet stran"),
    LINE("Přímka - 1. klik 1. bod, 2.klik - druhý bod");

    private String popis;

    DrawableType(String popis) {
        this.popis = popis;
    }

    public String getPopis() {
        return popis;
    }


}
