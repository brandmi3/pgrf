package solids;

import transforms.Cubic;
import transforms.Point3D;

import java.util.List;

public class FergusonCubic extends CubicData {


    public FergusonCubic() {


        verticies.add(new Point3D(0, 1, 2));
        verticies.add(new Point3D(2, 0, 2));
        verticies.add(new Point3D(3, 1, 0));
        verticies.add(new Point3D(3, 0, 0));
        verticies.add(new Point3D(1, 2, 1));
        verticies.add(new Point3D(1, 3, 3));
        verticies.add(new Point3D(3, 2, 2));
        verticies.add(new Point3D(3, 0, 2));


        for (int i = 0; i < verticies.size() - 4; i += 4) {
            cubics.add(new Cubic(Cubic.FERGUSON,
                    verticies.get(i),
                    verticies.get(i + 1),
                    verticies.get(i + 2),
                    verticies.get(i + 3)));
        }
    }
}
