package drawables;

import utils.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class NPolygon implements Drawable {

    private List<Point> points;
    private boolean done;

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
                    renderer.lineDDA(new Point(bef_x, bef_y), new Point(p.getX(), p.getY()), getColor());
                    bef_x = p.getX();
                    bef_y = p.getY();
                }
                renderer.lineDDA(new Point(bef_x, bef_y), new Point(getPoint(0).getX(), getPoint(0).getY()), getColor());
            }

    }

    @Override
    public void modifyLastPoint(Point p) {
        points.get(getNumberOfPoints()-1).setX(p.getX()).setY(p.getY());
    }

    @Override
    public int getColor() {
        return Color.RED.getRGB();
    }

    @Override
    public int getFillColor() {
        return Color.YELLOW.getRGB();
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public List<Point> getPoints() {
        return points;
    }

    public NPolygon setPoints(List<Point> points) {
        this.points = points;
        return this;
    }
}
