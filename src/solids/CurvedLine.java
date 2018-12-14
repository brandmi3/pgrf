package solids;

import transforms.Point3D;

public class CurvedLine extends SolidData {

    /**
     * Spolupráce s Lukášem Kopeckým
     ***/

    public CurvedLine() {

        for (double angle = 0; angle <= 2 * Math.PI; angle += 0.01) {
            double z = 1;

            double x = 1 * Math.cos(angle);
            double y = 1 * Math.sin(angle);

            z += Math.cos(angle * 10);
            verticies.add(new Point3D(x, y, z));

        }
        int bla = 0;
        for (int i = 1; i < verticies.size(); i++) {

            indicies.add(bla);
            indicies.add(i);
            bla = i;
        }
    }
}