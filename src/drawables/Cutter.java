package drawables;

public class Cutter {
    NPolygon orezovaMaska;

    public Cutter(NPolygon pol) {
        this.orezovaMaska = pol;
    }

    public NPolygon clipPoly(NPolygon celyPolygon) {
        NPolygon out = new NPolygon(celyPolygon);

        for (int i = 0; i < orezovaMaska.getNumberOfPoints(); i++) {
            Edge cutter = new Edge(orezovaMaska.getPoints().get(i), orezovaMaska.getPoints().get((i + 1) % orezovaMaska.getNumberOfPoints()));

            celyPolygon = new NPolygon(out);
            out.getPoints().clear();
            if (celyPolygon.getPoints().size() > 0) {
                Point v1 = new Point(celyPolygon.getPoints().get(celyPolygon.getNumberOfPoints() - 1));
                for (int j = 0; j < celyPolygon.getNumberOfPoints(); j++) {
                    Point v2 = new Point(celyPolygon.getPoints().get(j));
                    if (cutter.isInside(v2)) {
                        if (!cutter.isInside(v1))
                            out.getPoints().add(cutter.intersection(v1, v2));
                        out.getPoints().add(v2);
                    } else {
                        if (cutter.isInside(v1)) {
                            out.getPoints().add(cutter.intersection(v1, v2));
                        }
                    }
                    v1 = v2;
                }
            }
        }
        return out;
    }
}
