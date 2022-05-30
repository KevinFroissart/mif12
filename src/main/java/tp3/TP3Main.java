package tp3;

import io.jbotsim.contrib.messaging.AsyncMessageEngine;
import io.jbotsim.core.Link;
import io.jbotsim.core.MessageEngine;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.List;

public class TP3Main {

    public static final int TAILLE_ANNEAU = 5;
    public static final int TAILLE_FENETRE = 400;

    private static double getAngle(int index) {
        return Math.toRadians((360 / TAILLE_ANNEAU) * index);
    }

    private static double getAbscisse(double angle) {
        return Math.cos(angle) * (TAILLE_FENETRE / 4) + (TAILLE_FENETRE / 2);
    }

    private static double getOrdonnee(double angle) {
        return Math.sin(angle) * (TAILLE_FENETRE / 4) + (TAILLE_FENETRE / 2);
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.disableWireless();
        MessageEngine me = new AsyncMessageEngine(tp, 5, AsyncMessageEngine.Type.FIFO);
        tp.setMessageEngine(me);

        List<Node> noeuds = new ArrayList<>();

        for (int i = 0; i < TAILLE_ANNEAU; i++) {
            noeuds.add(new NoeudCoordinateur());
            tp.addNode(getAbscisse(getAngle(i)), getOrdonnee(getAngle(i)), noeuds.get(i));
        }

        for (int i = 1; i <= TAILLE_ANNEAU; i++) {
            for (int j = 1; j <= TAILLE_ANNEAU; j++) {
                tp.addLink(new Link(noeuds.get(i - 1), noeuds.get(j == TAILLE_ANNEAU ? 0 : j), Link.Orientation.UNDIRECTED));
            }
        }

        tp.setTimeUnit(500);
        new JViewer(tp);
        tp.start();
    }
}
