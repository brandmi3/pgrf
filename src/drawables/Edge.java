package drawables;

public class Edge {

    int x1, x2, y1, y2;
    float k, q;

    public Edge(Point p1, Point p2) {
        this.x1 = p1.getX();
        this.y1 = p1.getY();
        this.x2 = p2.getX();
        this.y2 = p2.getY();
        compute();
    //    cut();
    }


    public boolean isHorizontal() {
        return k == 0;
    }

    public void order() {
        if (y2 < y1) {

            int p = x1;
            x1 = x2;
            x2 = p;

            p = y1;
            y1 = y2;
            y2 = p;
        }
    }

    public void cut() {
        x1 += 1;
        x2 -= 1;

    }

    public void compute() {

        int dx = x2 - x1;
        int dy = y2 - y1;

        k = (float) dy / (float) dx;

        q = y1 - k * x1;

    }

    public int findX(int y) {
        if (isVertical()) {
            k = (float) (x2 - x1) / (float) (y2 - y1);
            q = x1 - k * y1;
            return (int) (k * y + q);
        }
        return (int) ((y - q) / k);
    }

    public boolean isIntersection(int y) {
        order();

        return (y > y1 && y <= y2);
    }

    public boolean isVertical() {
        return x1 == x2;
    }

    public int yMin(int yMin) {
        return Math.min(Math.min(y1, y2), yMin);

    }

    public int yMax(int yMax) {
        return Math.max(Math.max(y1, y2), yMax);

    }



    public boolean isInside(Point p) {
        int x = p.getX();
        int y = p.getY();
        int vX = x1;
        int vY = y1;
        int nX = x2;
        int nY = y2;

        int side = ((nX - vX) * (y - vY) - (nY - vY) * (x - vX));
        return side < 0;
    }

    public Point intersection(Point v1, Point v2) {
        int x, y;
        int x1 = this.x1;
        int y1 = this.y1;
        int x2 = this.x2;
        int y2 = this.y2;
        int x3 = v2.getX();
        int y3 = v2.getY();
        int x4 = v1.getX();
        int y4 = v1.getY();

        x = ((x1 * y2 - x2 * y1) * (x3 - x4) - (x3 * y4 - x4 * y3) * (x1 - x2))
                / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        y = ((x1 * y2 - x2 * y1) * (y3 - y4) - (x3 * y4 - x4 * y3) * (y1 - y2))
                / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        return new Point(x, y);
    }
}
