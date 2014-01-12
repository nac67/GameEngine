package model;

import java.awt.Point;

public class Utils {
    
    public static void p(String s){
        System.out.println(s);
    }

    public static boolean circle_collision (double x1, double y1, double r1, 
                                            double x2, double y2, double r2){
        double distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        return distance<= r1+r2;
    }
    
    public static boolean circle_collision (int x1, int y1, int r1, 
                                            int x2, int y2, int r2){
        double distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        return distance<= r1+r2;
    }
    
    public static boolean circle_collision (Point p1, int r1, Point p2, int r2){
        double distance = Math.sqrt((p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)*(p2.y-p1.y));
        return distance<= r1+r2;
    }
    
    public static boolean rect_collision (Point p1, int w1, int h1, Point p2, int w2, int h2){
        return !(p1.x > p2.x+w2 || p1.x+w1 < p2.x || p1.y > p2.y+h2 || p1.y+h1 < p2.y);
    }
    
    public static Point addPoints(Point p1, Point p2){
        return new Point(p1.x+p2.x,p1.y+p2.y);
    }

}
