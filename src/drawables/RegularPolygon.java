package drawables;

import utils.Renderer;

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
        renderer.drawPolygon(center, radius, distance);
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
}
