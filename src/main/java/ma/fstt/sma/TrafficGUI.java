package ma.fstt.sma;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrafficGUI extends JFrame {
    private String stateA = "RED", stateB = "RED";
    // Liste thread-safe pour éviter les erreurs pendant l'animation
    private List<VisualCar> cars = new CopyOnWriteArrayList<>();

    // On-Screen Dashboard data
    private int totalVehicles = 0;
    private double avgFlow = 0.0;

    public TrafficGUI() {
        setTitle("STI - Simulateur de Carrefour Intelligent");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Timer d'animation (60 FPS pour la fluidité)
        new Timer(16, e -> {
            moveCars();
            repaint();
        }).start();

        setVisible(true);
    }

    // Classe interne pour représenter une voiture sur l'écran
    class VisualCar {
        int x, y;
        String axis;
        boolean moving = true;
        boolean isAmbulance;

        VisualCar(String axis, boolean isAmbulance) {
            this.axis = axis;
            this.isAmbulance = isAmbulance;
            if (axis.equals("Axe_A")) {
                x = 285;
                y = 0;
            } // Nord -> Sud
            else {
                x = 0;
                y = 285;
            } // Ouest -> Est
        }
    }

    public void addCar(String axis, boolean isAmbulance) {
        cars.add(new VisualCar(axis, isAmbulance));
    }

    public void updateLights(String axis, String state) {
        if (axis.equals("Axe_A"))
            stateA = state;
        else
            stateB = state;
    }

    public synchronized void updateKPIs(int total, double flow) {
        this.totalVehicles = total;
        this.avgFlow = flow;
        repaint();
    }

    private void moveCars() {
        for (VisualCar car : cars) {
            if (car.axis.equals("Axe_A")) {
                // Check for car ahead on Axe A
                boolean carAhead = false;
                for (VisualCar other : cars) {
                    if (other != car && other.axis.equals("Axe_A") && other.y > car.y && other.y - car.y < 45) {
                        carAhead = true;
                        break;
                    }
                }

                // Stop au feu rouge de l'Axe A ou si une voiture est devant
                if (carAhead || (stateA.equals("RED") && car.y > 200 && car.y < 210))
                    car.moving = false;
                else
                    car.moving = true;

                if (car.moving)
                    car.y += 3;
                if (car.y > 600)
                    cars.remove(car); // Disparaît après le carrefour
            } else {
                // Check for car ahead on Axe B
                boolean carAhead = false;
                for (VisualCar other : cars) {
                    if (other != car && other.axis.equals("Axe_B") && other.x > car.x && other.x - car.x < 45) {
                        carAhead = true;
                        break;
                    }
                }

                // Stop au feu rouge de l'Axe B ou si une voiture est devant
                if (carAhead || (stateB.equals("RED") && car.x > 200 && car.x < 210))
                    car.moving = false;
                else
                    car.moving = true;

                if (car.moving)
                    car.x += 3;
                if (car.x > 600)
                    cars.remove(car);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        // Double buffering pour éviter le scintillement
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        draw(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    public void draw(Graphics g) {
        // City Background (Dark Green)
        g.setColor(new Color(34, 139, 34)); // Forest Green
        g.fillRect(0, 0, getWidth(), getHeight());

        // 1. Dessin des routes
        g.setColor(Color.GRAY);
        g.fillRect(0, 250, 600, 100); // Route Horizontale
        g.fillRect(250, 0, 100, 600); // Route Verticale

        g.setColor(Color.WHITE); // Lignes blanches
        g.drawLine(0, 300, 600, 300);
        g.drawLine(300, 0, 300, 600);

        // Zebra Crossings (White stripes just before stop lines)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(
                new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f }, 0.0f));
        // North entry
        g2d.drawLine(250, 230, 350, 230);
        g2d.drawLine(250, 240, 350, 240);
        // South entry
        g2d.drawLine(250, 360, 350, 360);
        g2d.drawLine(250, 370, 350, 370);
        // West entry
        g2d.drawLine(230, 250, 230, 350);
        g2d.drawLine(240, 250, 240, 350);
        // East entry
        g2d.drawLine(360, 250, 360, 350);
        g2d.drawLine(370, 250, 370, 350);
        g2d.setStroke(new BasicStroke(1)); // Reset stroke

        // 2. Dessin des Feux
        drawLight(g, 210, 180, stateA); // Feu Axe A
        drawLight(g, 360, 360, stateB); // Feu Axe B

        // 3. Dessin des Voitures
        for (VisualCar car : cars) {
            if (car.isAmbulance) {
                g.setColor(new Color(220, 20, 60)); // Crimson Red
            } else {
                g.setColor(new Color(30, 144, 255)); // Dodger Blue
            }

            if (car.axis.equals("Axe_A")) {
                // Vertical car, size 20x30
                g.fillRoundRect(car.x + 5, car.y, 20, 30, 10, 10); // +5 to center in lane
                // Headlights
                g.setColor(Color.YELLOW);
                g.fillOval(car.x + 7, car.y + 24, 4, 4);
                g.fillOval(car.x + 19, car.y + 24, 4, 4);
            } else {
                // Horizontal car, size 30x20
                g.fillRoundRect(car.x, car.y + 5, 30, 20, 10, 10); // +5 to center in lane
                // Headlights
                g.setColor(Color.YELLOW);
                g.fillOval(car.x + 24, car.y + 7, 4, 4);
                g.fillOval(car.x + 24, car.y + 19, 4, 4);
            }
        }

        // 4. On-Screen Dashboard (Overlay)
        g2d.setColor(new Color(0, 0, 0, 180)); // Semi-transparent black
        g2d.fillRoundRect(10, 35, 200, 80, 15, 15);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("SYSTEM STATUS: ACTIVE", 20, 60);
        g2d.drawString("TOTAL VEHICLES: " + totalVehicles, 20, 80);
        g2d.drawString(String.format("AVG FLOW: %.2f v/s", avgFlow), 20, 100);
    }

    private void drawLight(Graphics g, int x, int y, String state) {
        g.setColor(Color.DARK_GRAY); // Metallic look
        g.fillRect(x, y, 30, 70);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 30, 70);

        g.setColor(state.equals("RED") ? Color.RED : Color.BLACK);
        g.fillOval(x + 5, y + 5, 20, 20);
        g.setColor(state.equals("YELLOW") ? Color.YELLOW : Color.BLACK);
        g.fillOval(x + 5, y + 27, 20, 20);
        g.setColor(state.equals("GREEN") ? Color.GREEN : Color.BLACK);
        g.fillOval(x + 5, y + 49, 20, 20);
    }
}