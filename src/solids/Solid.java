package solids;

import transforms.Point3D;

import java.awt.*;
import java.util.List;

public interface Solid {

    List<Point3D> getVerticies(); //list bodu v objektu

    List<Integer> getIndicies(); // list indexu, ktere spolu tvoří hranu

    default int getColorByEdge(int index) {
        return Color.BLACK.getRGB();
    }

}
