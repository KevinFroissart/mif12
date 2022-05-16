package tp3;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.HashSet;
import java.util.Set;

public class Noeud extends Node {

    private boolean alive;
    private Set<Node> trusted;
    private Set<Node> answered;
    private int tick;

    @Override
    public void onStart() {
        trusted = new HashSet<>();
        answered = new HashSet<>();
        alive = true;
        tick = 0;
    }

    @Override
    public void onSelection() {
        this.alive = false;
        this.setColor(Color.RED);
    }

    @Override
    public void onMessage(Message message) {
        System.out.println(this.getID() + " received \"" + message.getContent() + "\" from " + message.getSender().getID());
        if (!alive) return;
        if (message.getContent().equals("Ping")) {
            System.out.println(this.getID() + " sending Pong to " + message.getSender());
            send(message.getSender(), new Message("Pong"));
        }
        if (message.getContent().equals("Pong")) {
            System.out.println(this.getID() + " adding " + message.getSender() + " to the answered set");
            answered.add(message.getSender());
        }
    }

    @Override
    public void onClock() {
        if (!alive) return;
        tick = (tick + 1) % 20;
        //System.out.println(tick);
        if (tick == 9 || tick == 19) {
            System.out.println("Node " + this.getID() + " sendAll Ping");
            if(this.getID() == 0 ) sendAll(new Message("Ping"));
            // Il faut trouver un moyen d'envoyer à TOUS les noeuds et pas juste les voisins
        }
        if (tick == 19) {
            trusted = answered;
            answered.clear();
            System.out.println(trusted.stream().map(e -> e.getID() + ",").reduce("", String::concat));
            this.setLabel(trusted.stream().map(e -> e.getID() + ",").reduce("", String::concat));
        }
    }

}
