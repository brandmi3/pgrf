package drawables;

import utils.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class NPolygon implements Drawable {

    List<Point> points;

    public NPolygon() {
        points = new ArrayList<>();
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public Point getPoint(int i) {
        return points.get(i);
    }

    public int getNumberOfPoints() {
        return points.size();
    }

    public void clear() {
        points.clear();
    }

    @Override
    public void draw(Renderer renderer) {
        if (getNumberOfPoints() >= 2) {
            int size = getNumberOfPoints();
            int bef_x = getPoint(size - 1).getX();
            int bef_y = getPoint(size - 1).getY();


            for (int i = 0; i < size; i++) {
                Point p = getPoint(i);
                renderer.lineDDA(bef_x, bef_y, p.getX(), p.getY());//todo
                bef_x = p.getX();
                bef_y = p.getY();
            }
            renderer.lineDDA(bef_x, bef_y, getPoint(0).getX(), getPoint(0).getY());//todo
        }
    }
}
