package drawables;

public class Edge {

    int x1, x2, y1, y2;
    float k, q;

    public Edge(Point p1, Point p2) {
        this.x1 = p1.getX();
        this.y1 = p1.getY();
        this.x2 = p2.getX();
        this.y2 = p2.getY();
    }

    public boolean isHorizontal() {
        return false; //TODO
    }

    public void order() {
        //TODO
    }

    public void cut() {
        //TODO oriznuti posledniho pixelu
    }

    public void compute() {
        //TODO vypocet k a q
    }

    public int findX(int y) {
        return 0;//todo vypocet x dle y,k,q
    }

    public boolean isIntersection(int y) {
        return false;//todo - true kdy y > y1 && y < y2
    }

    public int yMin(int yMin) {
        return 0; //todo dke y1,y2 a yMin rozhondout, ktere vracíme

    }

    public int yMax(int yMax) {
        return 0; //todo dke y1,y2 a yMax vratit max hodnotu

    }


}
