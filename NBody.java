import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class NBody extends JPanel implements ActionListener
{
    public int n;
    public int nIncrement;
    public double dt;
    
    // Creating separate ArrayLists to store position, velocity, mass, and color properties for each star
    public ArrayList<Integer> pos = new ArrayList<Integer>();
    public ArrayList<Double> vel = new ArrayList<Double>();
    public ArrayList<Integer> mass = new ArrayList<Integer>();
    public ArrayList<Integer> color = new ArrayList<Integer>();

    // Methods to randomly generate integers and doubles.
    public static int randomInt(int x, int y) {
        return ThreadLocalRandom.current().nextInt(x,y); }
    public static double randomDouble(double x, double y) {
        return ThreadLocalRandom.current().nextDouble(x,y); }
    
    public void init(int n)
    {
        this.n = n;
        int starsLeft = n;
        if (n <= 0)
            return;
        
        // Adds randomly generated numbers to various ArrayLists to act as the position, velocity, mass, and color of the generated stars
        // Each ArrayList is operating on increments of 3 to allow for accurate for loop iteration. Empty spots in each increment are filled with null values
        while (true) {
            
            pos.add(randomInt(10,790));
            pos.add(randomInt(10,790));
            pos.add(null);
            vel.add(randomDouble(-5,5));
            vel.add(randomDouble(-5,5));
            vel.add(null);
            mass.add(randomInt(2,10));
            mass.add(null);
            mass.add(null);
            color.add(randomInt(128,256));
            color.add(randomInt(128,256));
            color.add(randomInt(128,256));

            starsLeft--;
            if (starsLeft == 0)
                break; }

    }

    // Draw a circle centered at (x, y) with radius r
    public void drawCircle(Graphics g, int x, int y, int r)
    {
        int d = 2*r;
        g.fillOval(x - r, y - r, d, d);
    }

    public void paintComponent(Graphics g)
    {
        // Clear the screen
        super.paintComponent(g);

        // Draws circles based on properties of the star taken from the generated ArrayLists
        for (int i = 0; i < pos.size(); i +=3 ) {
            g.setColor(new Color(color.get(i),color.get(i+1),color.get(i+2)));
            drawCircle(g, pos.get(i), pos.get(i+1), mass.get(i)); }

    }

    public void actionPerformed(ActionEvent e)
    {

        // F = (G*m1*m2)/r^2
        // a = F / m
        // deltaV = a * dt

        // Combined equation: deltaV = (G*m1*m2*dt) / (m1 * r^2)
        
        // This bounces stars back from the walls if they get too close to leaving the screen
        // This is totally optional and only serves to showcase the gravitational attraction, as otherwise the stars leave the screen fairly quickly
        for (int i = 0; i < pos.size(); i +=3 ) {
            if (pos.get(i) >= 790 || pos.get(i) <= 10)
                vel.set(i, -1 * vel.get(i));
            if (pos.get(i+1) >= 790 || pos.get(i+1) <= 10)
                vel.set(i+1, -1 * vel.get(i+1)); }
        
        // This uses a nested for loop to ascertain the gravitational force between each pair of stars
        // The x and y velocities for each star are then modified based on direction and force
        for (int i = 0; i < pos.size(); i +=3 ) {
            for (int j = 0; j < pos.size(); j +=3 ) {
                
                // This discards duplicates (comparing a star to itself)
                if (i == j)
                    continue;
                
                int xDistance = (pos.get(i)-pos.get(j));
                int yDistance = (pos.get(i+1)-pos.get(j+1));
                double r = Math.sqrt((xDistance)*(xDistance) + (yDistance)*(yDistance));
                
                // This creates an x and y component ratio to in order to scale deltaV
                // This serves as the replacement for the vector/magnitude system
                double xRatio = Math.abs((double) xDistance / r);
                double yRatio = 1 - xRatio;

                if (r < 5)
                    r = 5;
                
                // I set g to be 500 instead of 100 because otherwise the gravitational attraction is very subtle
                int g = 500;
                double deltaV = (g*(mass.get(i))*(mass.get(j))*dt)/((r * r) * mass.get(i));

                // The deltaV gets multiplied by the component ratio before it is added
                // For example, if the two stars are directly above/below each other, then logically all of the force is pointing up/down and none is pointing horizontally
                // In that scenario, the y ratio is 1.0 so all of deltaV gets applied to the y velocity and the x ratio is 0 so none of the deltaV gets applied to the x velocity
                // In another example, if the x and y distance between two stars are equal, then each ratio is 0.5 and the deltaV is evenly split between x and y velocities
                // This splitting may be why g must be set higher than 100 to get a noticeable effect
                if (xDistance > 0) {
                    vel.set(i, (vel.get(i) - (xRatio * deltaV))); }
                if (xDistance < 0) {
                    vel.set(i, (vel.get(i) + (xRatio * deltaV))); }
                if (yDistance > 0) {
                    vel.set(i+1, (vel.get(i+1) - (yRatio * deltaV))); }
                if (yDistance < 0) {
                    vel.set(i+1, (vel.get(i+1) + (yRatio * deltaV))); }
                }}
        
        // This constantly adjusts the star's positions based on their velocities
        for (int i = 0; i < pos.size(); i +=3 ) {
            pos.set(i, (pos.get(i) + (int) Math.round(vel.get(i))));
            pos.set(i+1, (pos.get(i+1) + (int) Math.round(vel.get(i+1)))); }

        // Repaint the screen
        repaint();
        Toolkit.getDefaultToolkit().sync();
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int n = Integer.parseInt(args[0]);
        
        NBody nbody = new NBody();
        nbody.setBackground(Color.BLACK);
        nbody.dt = 0.1;
        nbody.setPreferredSize(new Dimension(800, 800));
        nbody.init(n);

        frame.add(nbody);
        frame.pack();

        Timer timer = new Timer(16, nbody);
        timer.start();

        frame.setVisible(true);

    }
}