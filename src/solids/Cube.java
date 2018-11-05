package solids;

import transforms.Point3D;

public class Cube extends SolidData {

    public Cube(double size) {

        double half = size / 2;
        verticies.add(new Point3D(0, 0, 0)); //0
        verticies.add(new Point3D(0, size, 0)); //1.
        verticies.add(new Point3D(size, 0, 0)); //2.
        verticies.add(new Point3D(size, size, 0)); //3.

        verticies.add(new Point3D(half, half, 0)); //0
        verticies.add(new Point3D(half, size+half, 0)); //1.
        verticies.add(new Point3D(size+half, half, 0)); //2.
        verticies.add(new Point3D(size+half, size+half, 0)); //3.
        //todo

        indicies.add(0);
        indicies.add(1);//prvni usecka


    }
}
