import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.Point2D;

class Point {    private double x = 0;    private double y = 0;
    public Point(double x, double y) {    this.x = x;  this.y = y;    }
    public Point() {    }
    public double getX() {        return this.x;    }

    public double getY() {        return this.y;    }    }

class setOfPoints { //массив точек
    Point[] points = null;    int max = 0;    Point[] resultpoints = null;
    public double getMAX() {        return this.max;    }
    setOfPoints(Point[] points) {        this.points = points;    }

    public Point Center(Point []circle){ //вычисляем центр окружности
        double ma = AngleCoef(circle[0],circle[1]);
        double mb = AngleCoef(circle[1],circle[2]);
        if (ma == 0.0 || ma ==0.0 || mb == 0.0 || mb == 0.0) return new Point (-2000,-2000);
        double xx = (ma * mb * (circle[0].getY() - circle[2].getY()) +mb*(circle[0].getX()+circle[1].getX())-ma*(circle[1].getX()+circle[2].getX()))/(2*(mb - ma));
        double yy = (-1/ma)*(xx - (circle[0].getX()+circle[1].getX())/2) + (circle[0].getY()+circle[1].getY())/2;
        Point result = new Point(xx,yy);        return result;    }

    public double AngleCoef(Point one,Point two){ //коэффициент наклона
        return (two.getY()-one.getY())/(two.getX()- one.getX());    }

    public Point[] initPoint(int i,int j,int k) { //массив точек для построения окружности
        Point[] result = new Point[3];
        result[0] = new Point(points[i].getX(),points[i].getY());
        result[1] = new Point(points[j].getX(),points[j].getY());
        result[2] = new Point(points[k].getX(),points[k].getY());
        return result;    }

    public int countInnerPoints(Point center, double radius){ //вычисляем количество точек на окружности
        int result = 0;
        for (int i = 0; i < points.length;i++){
            double res  = Math.sqrt(((points[i].getX()-center.getX())* (points[i].getX()-center.getX()))+((points[i].getY()-center.getY())* (points[i].getY()-center.getY())));
            if ((res < radius+0.5) && (res > radius-0.5))     result++;        }
        return result;    }

    public Point[] maxPoints(Point center, double radius){ //запоминаем точки на окружности
        Point[] pp = new Point[points.length];        int j = 0;
        for (int i = 0; i < points.length; i++){
            double res  = Math.sqrt(((points[i].getX()-center.getX())* (points[i].getX()-center.getX()))+((points[i].getY()-center.getY())* (points[i].getY()-center.getY())));
            if ((res < radius+0.5) && (res > radius-0.5)) {
                pp[j] = new Point(points[i].getX(), points[i].getY());  j++; } }  return pp; }

    public double distance(Point one,Point two){ //расстояние между двумя точками
        double res = Math.sqrt(((one.getX() - two.getX())*(one.getX() - two.getX()))+ ((one.getY() - two.getY())*(one.getY() - two.getY())));        return res;    }

    public  Point[] SearchMaxArea(){ //поиск центра и радиуса окружности, чтобы максимум внутри точек было
        Point[] pointResult=null;
        Point[] tempCircle = null; //массив, по которым строим окружность
        Point center = null; //центр окружности
        double raduis = 0; //радиус окружности
        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < points.length;j++)
                for (int k = 0; k < points.length; k++) {
                    if( i!=j  && i!=k && j!=k) {
                        tempCircle = initPoint(i, j, k); //задаем три точки для поиска окружности
                        center = Center(tempCircle); //вычисляем центр
                        raduis = distance(center, points[i]); //вычисляем радиус окружности
                        if (countInnerPoints(center, raduis) > max) {
                            resultpoints = maxPoints(center, raduis);
                            max = countInnerPoints(center, raduis);
                            System.out.println("max = " + max);
                            System.out.println("radius = " + raduis);
                            pointResult = tempCircle;            }       }  }
            } return pointResult; }  //возвращаем массив точек, которые на окружности лежат

        public static Point[] GetTestExample(){  // для примера
            Point[] result = new Point[19];       result[0] = new Point(0, 141);
            result[1] = new Point(0, -141);       result[2] = new Point(141, 0);
            result[3] = new Point(-141,0);        result[4] = new Point(100, 100);
            result[5] = new Point(-100, -100);    result[6] = new Point(-100, 100);
            result[7] = new Point(100, -100);     result[8] = new Point(250, -100);
            result[9] = new Point(10, 70);        result[10] = new Point(100, 55);
            result[11] = new Point(-175, 96);     result[12] = new Point(-63, -90);
            result[13] = new Point(0, -115);      result[14] = new Point(9, 99);
            result[15] = new Point(200,150);      result[16] = new Point(163, 80);
            result[17] = new Point(85, -163);     result[18] = new Point(-20, 60);
            return result;    }

    public static class Main {
        public static void main(String argc[]){
            MenuFrame f = new MenuFrame(); f.setSize(800, 700); f.setVisible(true); }}}
