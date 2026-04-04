package ma.fstt.sma;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupervisorAgent extends Agent {
    private int totalCars = 0;
    private long startTime;

    protected void setup() {
        startTime = System.currentTimeMillis();
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getContent().startsWith("RELEASED:")) {
                        int num = Integer.parseInt(msg.getContent().split(":")[1]);
                        totalCars += num;
                        double flow = totalCars / ((System.currentTimeMillis() - startTime) / 1000.0);
                        System.out.println("📊 [KPI] Total voitures: " + totalCars + " | Flux: " + String.format("%.2f", flow) + " v/s");
                        
                        // Append to CSV
                        try (PrintWriter out = new PrintWriter(new FileWriter("simulation_stats.csv", true))) {
                            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            out.println(timestamp + ";" + totalCars + ";" + String.format("%.2f", flow));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else block();
            }
        });
    }
}