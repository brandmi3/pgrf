package solids;

        import transforms.Point3D;

public class Pyramid extends SolidData {


    public Pyramid(double size) {

        verticies.add(new Point3D(size, size, size));     // 0.
        verticies.add(new Point3D(0, size, size));    // 1.
        verticies.add(new Point3D(size, 0, size));     // 0.
        verticies.add(new Point3D(0, 0, size));    //3
        verticies.add(new Point3D(size/2, size/2, 0));     //4


        indicies.add(0);        indicies.add(1);
        indicies.add(1);        indicies.add(3);
        indicies.add(2);        indicies.add(3);
        indicies.add(2);        indicies.add(0);

        indicies.add(4);        indicies.add(0);
        indicies.add(4);        indicies.add(1);
        indicies.add(4);        indicies.add(2);
        indicies.add(4);        indicies.add(3);


    }
}

