package ma.fstt.sma;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.Random;

public class TrafficGeneratorAgent extends Agent {
    private Random random = new Random();
    protected void setup() {
        addBehaviour(new TickerBehaviour(this, 2000) {
            protected void onTick() {
                try {
                    String axis = random.nextBoolean() ? "Axe_A" : "Axe_B";
                    String id = "V" + System.currentTimeMillis() % 10000;
                    // On passe le nom de l'axe en argument à la voiture
                    if (random.nextInt(10) < 9) {
                        getContainerController().createNewAgent("V"+id, "ma.fstt.sma.CarAgent", new Object[]{axis}).start();
                    } else {
                        // On envoie aussi l'axe à l'ambulance !
                        getContainerController().createNewAgent("Amb"+id, "ma.fstt.sma.AmbulanceAgent", new Object[]{axis}).start();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }
}