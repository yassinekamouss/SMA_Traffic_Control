package ma.fstt.sma;
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class CarAgent extends Agent {
    protected void setup() {
        String targetLight = (String) getArguments()[0]; // Récupère l'axe assigné
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID(targetLight, AID.ISLOCALNAME));
                msg.setContent("CAR_ARRIVED");
                send(msg);
            }
        });
    }
}