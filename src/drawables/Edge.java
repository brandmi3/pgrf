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
        // cut();
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

//    public void cut() {
//        x1 += 1;
//        x2 -= 1;
//
//        TODO oriznuti posledniho pixelu
//    }

    public void compute() {

        int dx = x2 - x1;
        int dy = y2 - y1;

        k = (float) dy / (float) dx;

        q = y1 - k * x1;

    }

    public int findX(int y) {
        if (isVertical()) {
            k = (float) (x2 - x1) / (float) (y2 - y1);
            q = x1-k*y1;
            return (int)(k*y + q);
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
        return Math.min(Math.min(y1, y2), yMin); //todo dle y1,y2 a yMin rozhondout, ktere vracíme

    }

    public int yMax(int yMax) {
        return Math.max(Math.max(y1, y2), yMax); //todo dke y1,y2 a yMax vratit max hodnotu

    }

    public int getX1() {
        return x1;
    }

    public Edge setX1(int x1) {
        this.x1 = x1;
        return this;
    }

    public int getX2() {
        return x2;
    }

    public Edge setX2(int x2) {
        this.x2 = x2;
        return this;
    }

    public int getY1() {
        return y1;
    }

    public Edge setY1(int y1) {
        this.y1 = y1;
        return this;
    }

    public int getY2() {
        return y2;
    }

    public Edge setY2(int y2) {
        this.y2 = y2;
        return this;
    }

    public float getK() {
        return k;
    }

    public Edge setK(float k) {
        this.k = k;
        return this;
    }

    public float getQ() {
        return q;
    }

    public Edge setQ(float q) {
        this.q = q;
        return this;
    }

    public Boolean isInside(Point v) {

        int x = v.getX();
        int y = v.getY();
        int aX = x1;
        int aY = y1;
        int bX = x2;
        int bY = y2;


        int side = ((bX - aX) * (y - aY) - (bY - aY) * (x - aX));
        // 3 stavy : 0 na primce, <0 na jedne strane primky, >0 na druhe strane primky
        return side < 0;

    }

    public Point intersection(Point v1, Point v2) {
        // metoda pro vypocet pruseciku pomoci primky a dvou bodu
        int x0, y0;
        int x1 = this.x1;
        int y1 = this.y1;
        int x2 = this.x2;
        int y2 = this.y2;
        int x3 = v2.getX();
        int y3 = v2.getY();
        int x4 = v1.getX();
        int y4 = v1.getY();


        x0 = ((x1 * y2 - x2 * y1) * (x3 - x4) - (x3 * y4 - x4 * y3) * (x1 - x2))
                / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        y0 = ((x1 * y2 - x2 * y1) * (y3 - y4) - (x3 * y4 - x4 * y3) * (y1 - y2))
                / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        return new Point(x0, y0);
    }
}
