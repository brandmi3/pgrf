package drawables;

import utils.Renderer;

import java.awt.*;

public class RegularPolygon implements Drawable {

    private Point center;
    private Point radius;
    private Point distance;

    public RegularPolygon() {
    }

    public RegularPolygon(Point center, Point radius) {
        this.center = center;
        this.radius = radius;
        this.distance = radius;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.drawPolygon(center, radius, distance,getColor());
    }

    public Point getCenter() {
        return center;
    }

    public RegularPolygon setCenter(Point center) {
        this.center = center;
        return this;
    }

    public Point getRadius() {
        return radius;
    }

    public RegularPolygon setRadius(Point radius) {
        this.radius = radius;
        return this;
    }

    public Point getDistance() {
        return distance;
    }

    public RegularPolygon setDistance(Point distance) {
        this.distance = distance;
        return this;
    }

    @Override
    public int getColor() {
        return Color.blue.getRGB();
    }

    @Override
    public void modifyLastPoint(Point p) {

    }

    public void clearPoints() {
        this.center=null;
        this.radius=null;
        this.distance=null;
    }
}
