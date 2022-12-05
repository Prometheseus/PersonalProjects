import java.awt.*;
import javax.swing.*;

public class Sierpinski extends Canvas
{
    public int n;
    public int size;

    public static void drawTriangle(Graphics g, int x1, int x2, int x3, int y1, int y2, int y3)
    {
        g.drawPolygon(new int[] {x1,x2,x3}, new int[] {y1,y2,y3}, 3);
    }

    public static void recursiveTriangle(Graphics g, int n, int x1, int x2, int x3, int y1, int y2, int y3, int length)
    {
        // Base case   
        if (n == 0)
            return;
        else {
            // Drawing and recursion of the bottom left triangle
            drawTriangle(g,x1-(length/4),x1,x1+(length/4),(y1+y2)/2,y2,(y1+y2)/2);
            recursiveTriangle(g,n-1,x1-(length/4),x1,x1+(length/4),(y1+y2)/2,y2,(y1+y2)/2, length/2);
            // Drawing and recursion of the upper triangle
            drawTriangle(g,x1+(length/4),x2,x3-(length/4),y1-(length/2),y1,y1-(length/2));
            recursiveTriangle(g,n-1,x1+(length/4),x2,x3-(length/4),y1-(length/2),y1,y1-(length/2),length/2);
            // Drawing and recursion of the bottom right triangle
            drawTriangle(g,(x2+x3)/2,x3,x3+(length/4),(y2+y3)/2,y2,(y3+y2)/2);
            recursiveTriangle(g,n-1,(x2+x3)/2,x3,x3+(length/4),(y2+y3)/2,y2,(y3+y2)/2,length/2);
        }
    }
    public void paint(Graphics g)
    {
        // Initial coordinates
        int x1 = 0;
        int x2 = 350;
        int x3 = 700;
        int y1 = 700;
        int y2 = 0;
        int y3 = 700;
        int length = (x1+x3)/2;

        // First two triangles of n=0 and n=1 are not recursive. After n is 2 or greater, recursion starts
        if (n >= 0)
            drawTriangle(g,x1,x2,x3,y1,y2,y3);
        if (n >= 1)
            drawTriangle(g,(x1+x2)/2,(x1+x3)/2,(x2+x3)/2,(y1+y2)/2,(y1+y3)/2,(y2+y3)/2);
        if (n >= 2) {
            recursiveTriangle(g,n-1,(x1+x2)/2,(x1+x3)/2,(x2+x3)/2,(y1+y2)/2,(y1+y3)/2,(y2+y3)/2, length); }
        else
            return;
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Sierpinski drawing = new Sierpinski();
        drawing.n = Integer.parseInt(args[0]);
        drawing.size = 700;
        drawing.setSize(drawing.size, drawing.size);
        frame.add(drawing);
        frame.pack();
        frame.setVisible(true);
    }
}