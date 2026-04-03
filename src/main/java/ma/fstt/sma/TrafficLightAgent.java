package ma.fstt.sma;
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.HashSet;
import java.util.Set;

public class TrafficLightAgent extends Agent {
    private String currentState = "RED";
    private Set<AID> waitingCars = new HashSet<>();
    private String partnerName;
    private static TrafficGUI gui = new TrafficGUI();

    // ✅ Méthode déplacée ici, au niveau de l'Agent
    private void update(String s) {
        currentState = s;
        gui.update(getLocalName(), s, waitingCars.size());
    }

    private void forceGreen(String reason) {
        System.out.println("🚨 [ALERTE] " + reason);
        update("GREEN");
        try {
            // On laisse l'ambulance passer pendant 4 secondes
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        update("RED");
        System.out.println("🚦 [SYSTEM] Reprise du flux normal.");
    }


    protected void setup() {
        if (getArguments() != null && getArguments().length > 0) {
            partnerName = (String) getArguments()[0];
        } else {
            System.err.println("❌ Erreur : " + getLocalName() + " n'a pas de partenaire !");
            doDelete(); // On arrête l'agent proprement s'il n'est pas configuré
            return;
        }
        update("RED");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();

                    if ("CAR_ARRIVED".equals(content)) {
                        waitingCars.add(msg.getSender());
                        update(currentState);
                    } else if ("EMERGENCY".equals(content)) {
                        forceGreen("URGENCE AMBULANCE");
                    }

                    if (msg.getPerformative() == ACLMessage.QUERY_IF) {
                        ACLMessage reply = msg.createReply();
                        // Je donne la permission si je suis au ROUGE
                        reply.setPerformative(currentState.equals("RED") ? ACLMessage.CONFIRM : ACLMessage.DISCONFIRM);
                        reply.setContent("PERMISSION_REPLY");
                        send(reply);
                    }
                } else {
                    // Intelligence : On ne demande la permission QUE s'il y a des voitures et qu'on est au ROUGE
                    if (!waitingCars.isEmpty() && currentState.equals("RED")) {
                        checkPartnerAndGo();
                    }
                    block();
                }
            }

            private void checkPartnerAndGo() {
                ACLMessage query = new ACLMessage(ACLMessage.QUERY_IF);
                query.addReceiver(new AID(partnerName, AID.ISLOCALNAME));
                send(query);
            }
        });

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive(
                        jade.lang.acl.MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)
                );
                if (msg != null) {
                    executeGreenCycle();
                } else block();
            }

            private void executeGreenCycle() {
                update("GREEN"); // ✅ Fonctionne maintenant
                try { Thread.sleep(3000); } catch (Exception e) {}
                int count = waitingCars.size();
                waitingCars.clear();
                update("RED");  // ✅ Fonctionne maintenant

                ACLMessage stat = new ACLMessage(ACLMessage.INFORM);
                stat.addReceiver(new AID("Superviseur", AID.ISLOCALNAME));
                stat.setContent("RELEASED:" + count);
                send(stat);
            }
        });
    }
}