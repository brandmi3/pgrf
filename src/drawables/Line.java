package drawables;

import utils.Renderer;

public class Line implements Drawable {

   Point p1;
   Point p2;

    public Line() {
    }

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.drawLineBresenham(p1.getX(),p1.getY(),p2.getX(),p2.getY());
    }

    public Point getP1() {
        return p1;
    }

    public Line setP1(Point p1) {
        this.p1 = p1;
        return this;
    }

    public Point getP2() {
        return p2;
    }

    public Line setP2(Point p2) {
        this.p2 = p2;
        return this;
    }
}
