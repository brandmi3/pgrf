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

    public double calculateArea() {
        double value = 0;
        if (getNumberOfPoints() >= 3) {
            Point def = points.get(0);
            Point bef = points.get(1);
            for (int i = 2; i < points.size(); i++) {
                Point next = points.get(i);
                value += calculateAreaOfTriangle(def, bef, next);
                bef = next;
            }
        }

        return value;
    }

    private double calculateAreaOfTriangle(Point p1, Point p2, Point p3) {
//       p=(a+b+c)/2
//       2/a odmoc.(p*(p-a)(p-b)(p-c))
        double a = Math.sqrt(Math.pow(Math.abs(p1.getX() - p2.getX()), 2) + Math.pow(Math.abs(p1.getY() - p2.getY()), 2));
        double b = Math.sqrt(Math.pow(Math.abs(p2.getX() - p3.getX()), 2) + Math.pow(Math.abs(p2.getY() - p3.getY()), 2));
        double c = Math.sqrt(Math.pow(Math.abs(p3.getX() - p1.getX()), 2) + Math.pow(Math.abs(p3.getY() - p1.getY()), 2));
        double p = (a + b + c) / 2;
        double value = (Math.sqrt(p * (p - a) * (p - b) * (p - c)));
        return Math.round(value*10)/10.0;
    }

    @Override
    public void draw(Renderer renderer) {
        if (getNumberOfPoints() >= 2) {
            int size = getNumberOfPoints();
            int bef_x = getPoint(size - 1).getX();
            int bef_y = getPoint(size - 1).getY();


            for (int i = 0; i < size; i++) {
                Point p = getPoint(i);
                renderer.lineDDA(new Point(bef_x, bef_y), new Point(p.getX(), p.getY()));
                bef_x = p.getX();
                bef_y = p.getY();
            }
            renderer.lineDDA(new Point(bef_x, bef_y), new Point(getPoint(0).getX(), getPoint(0).getY()));
        }
    }

    @Override
    public void modifyLastPoint(Point p) {

    }
}
