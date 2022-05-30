package tp3;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.HashSet;
import java.util.Set;

public class Noeud extends Node {

    protected boolean alive;
    protected Set<Node> trusted;
    private Set<Node> answered;
    private int tick;
    private static boolean debug = true;

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
        if (debug) System.out.println(this.getID() + " received \"" + message.getContent() + "\" from " + message.getSender().getID());
        if (!alive){
            this.setLabel("Dead");
            return;
        }
        if (message.getContent().equals("Ping")) {
            if (debug) System.out.println(this.getID() + " sending Pong to " + message.getSender());
            send(message.getSender(), new Message("Pong"));
        }
        if (message.getContent().equals("Pong")) {
            if (debug) System.out.println(this.getID() + " adding " + message.getSender() + " to the answered set");
            answered.add(message.getSender());
        }
    }

    @Override
    public void onClock() {
        if (!alive) return;
        tick = (tick + 1) % 20;
        if (tick == 9 || tick == 19) {
            if (debug) System.out.println("Node " + this.getID() + " sendAll Ping");
            sendAll(new Message("Ping"));
        }
        if (tick == 19) {
            trusted = new HashSet<>(answered);
            answered.clear();
            this.setLabel(trusted.stream().sorted().map(e -> e.getID() + ",").reduce("", String::concat));
        }
    }

}
