package solids;

import transforms.Point3D;

import java.awt.*;

public class Axis extends SolidData {


    public Axis() {
        verticies.add(new Point3D(0, 0, 0));    // 0.
        verticies.add(new Point3D(1, 0, 0));    // x
        verticies.add(new Point3D(0, 1, 0));    // y
        verticies.add(new Point3D(0, 0, 1));    // z

        indicies.add(0);
        indicies.add(1);
        indicies.add(0);
        indicies.add(2);
        indicies.add(0);
        indicies.add(3);

    }

    @Override
    public int getColorByEdge(int index) {
        switch (index) {
            case 0:
                return Color.RED.getRGB();
            case 1:
                return Color.GREEN.getRGB();
            case 2:
                return Color.BLACK.getRGB();
            default:
                return Color.BLACK.getRGB();
        }
    }

}
