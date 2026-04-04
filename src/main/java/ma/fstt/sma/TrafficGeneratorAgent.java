package ma.fstt.sma;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.util.Random;

public class TrafficGeneratorAgent extends Agent {
    private Random random = new Random();
    protected void setup() {
        addBehaviour(new TickerBehaviour(this, 2000) {
            protected void onTick() {
                try {
                    String axis = random.nextBoolean() ? "Axe_A" : "Axe_B";
                    String id = "V" + System.currentTimeMillis() % 10000;

                    if (random.nextInt(10) < 9) {
                        getContainerController().createNewAgent("V"+id, "ma.fstt.sma.CarAgent", new Object[]{axis}).start();
                        // Mise à jour de l'interface graphique après création d'une voiture
                        if (TrafficLightAgent.gui != null) {
                            TrafficLightAgent.gui.addCar(axis, false);
                        }
                    } else {
                        getContainerController().createNewAgent("Amb"+id, "ma.fstt.sma.AmbulanceAgent", new Object[]{axis}).start();
                        // Mise à jour de l'interface graphique après création d'une ambulance
                        if (TrafficLightAgent.gui != null) {
                            TrafficLightAgent.gui.addCar(axis, true);
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }
}