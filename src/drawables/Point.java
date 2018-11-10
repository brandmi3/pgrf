package drawables;

import java.awt.*;

public class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point(Point p){
        this.x = p.getX();
        this.y = p.getY();
    }

    public int getX() {
        return x;
    }

    public Point setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Point setY(int y) {
        this.y = y;
        return this;
    }

    private void scanline() {
/*




        for (int y = minY; y <= maxY; y++) {

            List<Integer> intersections = new ArrayList<>();
            // projít všechny hrany
            // pokud hrana má průsečík pro dané Y
            // tak vypočítáme průsečík a uložíme hodnotu do seznamu

            Collections.sort(intersections);
            // nebo volitelně implementovat vlastní algoritmus na seřazení

            // vybarvení mezi průsečíky
            // spojení vždy sudého s lichým
            // 0. a 1.; 2. a 3.;...
        }

        // obtáhnutí hranice
        //renderer.drawPolygon(points, edgeColor);
    */
    }
}
