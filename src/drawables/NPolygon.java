package drawables;

import utils.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class NPolygon implements Drawable {

    private List<Point> points;
    private boolean done;
    private int borderColor;
    private int fillColor;
    private boolean filled;

    public NPolygon() {
        this.borderColor = Color.RED.getRGB();
        this.fillColor = Color.DARK_GRAY.getRGB();
        points = new ArrayList<>();
    }

    public NPolygon(NPolygon clipedPoly) {
        points = new ArrayList<>();
        for (int i = 0; i < clipedPoly.getPoints().size(); i++) {
            this.points.add(clipedPoly.getPoint(i));
        }
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

    public void fillScanLine(Renderer renderer) {
        renderer.scanLine(getPoints(), borderColor, fillColor);
    }

    @Override
    public void modifyLastPoint(Point p) {
        points.get(getNumberOfPoints() - 1).setX(p.getX()).setY(p.getY());
    }

    @Override
    public int getColor() {
        return borderColor;
    }

    @Override
    public int getFillColor() {
        return fillColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public NPolygon setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public NPolygon setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
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

    @Override
    public void setColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public NPolygon setPoints(List<Point> points) {
        this.points = points;
        return this;
    }

    public boolean isFilled() {
        return filled;
    }

    public NPolygon setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }
}
