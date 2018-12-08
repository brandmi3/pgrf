package solids;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public abstract class SolidData implements Solid {
    protected List<Point3D> verticies;
    protected List<Integer> indicies;

    public SolidData() {
        verticies = new ArrayList<>();
        indicies = new ArrayList<>();
    }

    @Override
    public List<Point3D> getVerticies() {
        return verticies;
    }

    @Override
    public List<Integer> getIndicies() {
        return indicies;
    }

    @Override
    public String toString() {
        return getClass().getName().substring(getClass().getName().indexOf(".")+1);
    }
}
