package drawables;

public class Clipper {
    NPolygon clipper;

    public Clipper(NPolygon pol) {
        this.clipper = pol;
    }

    public NPolygon clipPoly(NPolygon clipPoly) {
        NPolygon out = new NPolygon(clipPoly);

        for (int i = 0; i < clipper.getNumberOfPoints(); i++) {
            Edge cutter = new Edge(clipper.getPoints().get(i), clipper.getPoints().get((i + 1) % clipper.getNumberOfPoints()));

            clipPoly = new NPolygon(out);
            out.getPoints().clear();
            if (clipPoly.getPoints().size() > 0) {
                Point v1 = new Point(clipPoly.getPoints().get(clipPoly.getNumberOfPoints() - 1));
                for (int j = 0; j < clipPoly.getNumberOfPoints(); j++) {
                    Point v2 = new Point(clipPoly.getPoints().get(j));

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
            }else{
                System.out.println("chyba");
            }
        }
        return out;
    }
}
