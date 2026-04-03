package ma.fstt.sma;
import javax.swing.*;
import java.awt.*;

public class TrafficGUI extends JFrame {
    private int queueA = 0, queueB = 0;
    private String stateA = "RED", stateB = "RED";

    public TrafficGUI() {
        setTitle("STI - Carrefour Intelligent Multi-Axes");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void update(String axis, String state, int queue) {
        if (axis.equals("Axe_A")) { stateA = state; queueA = queue; }
        else { stateB = state; queueB = queue; }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Axe A (Vertical)
        drawLight(g, 100, 100, stateA, "Axe A", queueA);
        // Axe B (Horizontal)
        drawLight(g, 250, 100, stateB, "Axe B", queueB);
    }

    private void drawLight(Graphics g, int x, int y, String state, String label, int q) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, 60, 150);
        g.setColor(Color.GRAY);
        g.fillOval(x+10, y+10, 40, 40); g.fillOval(x+10, y+55, 40, 40); g.fillOval(x+10, y+100, 40, 40);

        if(state.equals("RED")) { g.setColor(Color.RED); g.fillOval(x+10, y+10, 40, 40); }
        if(state.equals("YELLOW")) { g.setColor(Color.YELLOW); g.fillOval(x+10, y+55, 40, 40); }
        if(state.equals("GREEN")) { g.setColor(Color.GREEN); g.fillOval(x+10, y+100, 40, 40); }

        g.setColor(Color.BLACK);
        g.drawString(label, x, y - 20);
        g.drawString("Attente: " + q, x, y + 170);
    }
}