package ma.fstt.sma;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrafficGUI extends JFrame {
    private String stateA = "RED", stateB = "RED";
    // Liste thread-safe pour éviter les erreurs pendant l'animation
    private List<VisualCar> cars = new CopyOnWriteArrayList<>();

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

        VisualCar(String axis) {
            this.axis = axis;
            if (axis.equals("Axe_A")) { x = 285; y = 0; } // Nord -> Sud
            else { x = 0; y = 285; } // Ouest -> Est
        }
    }

    public void addCar(String axis) {
        cars.add(new VisualCar(axis));
    }

    public void updateLights(String axis, String state) {
        if (axis.equals("Axe_A")) stateA = state;
        else stateB = state;
    }

    private void moveCars() {
        for (VisualCar car : cars) {
            if (car.axis.equals("Axe_A")) {
                // Stop au feu rouge de l'Axe A
                if (stateA.equals("RED") && car.y > 200 && car.y < 210) car.moving = false;
                else car.moving = true;
                
                if (car.moving) car.y += 3;
                if (car.y > 600) cars.remove(car); // Disparaît après le carrefour
            } else {
                // Stop au feu rouge de l'Axe B
                if (stateB.equals("RED") && car.x > 200 && car.x < 210) car.moving = false;
                else car.moving = true;

                if (car.moving) car.x += 3;
                if (car.x > 600) cars.remove(car);
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
        // 1. Dessin des routes
        g.setColor(Color.GRAY);
        g.fillRect(0, 250, 600, 100); // Route Horizontale
        g.fillRect(250, 0, 100, 600); // Route Verticale
        
        g.setColor(Color.WHITE); // Lignes blanches
        g.drawLine(0, 300, 600, 300);
        g.drawLine(300, 0, 300, 600);

        // 2. Dessin des Feux
        drawLight(g, 210, 180, stateA); // Feu Axe A
        drawLight(g, 360, 360, stateB); // Feu Axe B

        // 3. Dessin des Voitures
        g.setColor(Color.BLUE);
        for (VisualCar car : cars) {
            g.fillRect(car.x, car.y, 30, 20);
        }
    }

    private void drawLight(Graphics g, int x, int y, String state) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, 30, 70);
        g.setColor(state.equals("RED") ? Color.RED : Color.DARK_GRAY);
        g.fillOval(x+5, y+5, 20, 20);
        g.setColor(state.equals("YELLOW") ? Color.YELLOW : Color.DARK_GRAY);
        g.fillOval(x+5, y+27, 20, 20);
        g.setColor(state.equals("GREEN") ? Color.GREEN : Color.DARK_GRAY);
        g.fillOval(x+5, y+49, 20, 20);
    }
}