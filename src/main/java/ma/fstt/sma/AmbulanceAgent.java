package ma.fstt.sma;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AmbulanceAgent extends Agent {
    protected void setup() {
        // On récupère l'axe cible (Axe_A ou Axe_B) passé en argument
        final String target = (getArguments() != null) ? (String) getArguments()[0] : "Axe_A";

        addBehaviour(new OneShotBehaviour() {
            public void action() {
                System.out.println("🚨 [AMBULANCE] Urgence sur " + target);
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM); // Utilise INFORM ou REQUEST
                msg.addReceiver(new AID(target, AID.ISLOCALNAME));
                msg.setContent("EMERGENCY");
                send(msg);
            }
        });
    }
}