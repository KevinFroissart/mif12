package tp2;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TP2Main {

    public static final int TAILLE_ANNEAU = 10;
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

        /**
         * Question 1
         */
        Node noeud0 = new ItaiRodeh();
        Node noeud1 = new ItaiRodeh();
        Node noeud2 = new ItaiRodeh();
        Node noeud3 = new ItaiRodeh();
        Node noeud4 = new ItaiRodeh();
        Node noeud5 = new ItaiRodeh();
        Node noeud6 = new ItaiRodeh();
        Node noeud7 = new ItaiRodeh();
        Node noeud8 = new ItaiRodeh();
        Node noeud9 = new ItaiRodeh();

        /**
         * Question 4
         */
        tp.addNode(getAbscisse(getAngle(0)), getOrdonnee(getAngle(0)), noeud0);
        tp.addNode(getAbscisse(getAngle(1)), getOrdonnee(getAngle(1)), noeud1);
        tp.addNode(getAbscisse(getAngle(2)), getOrdonnee(getAngle(2)), noeud2);
        tp.addNode(getAbscisse(getAngle(3)), getOrdonnee(getAngle(3)), noeud3);
        tp.addNode(getAbscisse(getAngle(4)), getOrdonnee(getAngle(4)), noeud4);
        tp.addNode(getAbscisse(getAngle(5)), getOrdonnee(getAngle(5)), noeud5);
        tp.addNode(getAbscisse(getAngle(6)), getOrdonnee(getAngle(6)), noeud6);
        tp.addNode(getAbscisse(getAngle(7)), getOrdonnee(getAngle(7)), noeud7);
        tp.addNode(getAbscisse(getAngle(8)), getOrdonnee(getAngle(8)), noeud8);
        tp.addNode(getAbscisse(getAngle(9)), getOrdonnee(getAngle(9)), noeud9);

        /**
         * Question 2
         */
        tp.addLink(new Link(noeud0, noeud1, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud1, noeud2, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud2, noeud3, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud3, noeud4, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud4, noeud5, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud5, noeud6, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud6, noeud7, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud7, noeud8, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud8, noeud9, Link.Orientation.DIRECTED));
        tp.addLink(new Link(noeud9, noeud0, Link.Orientation.DIRECTED));

        /**
         * Question 3
         */
        List<Integer> ids = new ArrayList<>();
        ids.add(0);
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);
        ids.add(5);
        ids.add(6);
        ids.add(7);
        ids.add(8);
        ids.add(9);

        Collections.shuffle(ids);

        noeud0.setID(ids.get(0));
        noeud1.setID(ids.get(1));
        noeud2.setID(ids.get(2));
        noeud3.setID(ids.get(3));
        noeud4.setID(ids.get(4));
        noeud5.setID(ids.get(5));
        noeud6.setID(ids.get(6));
        noeud7.setID(ids.get(7));
        noeud8.setID(ids.get(8));
        noeud9.setID(ids.get(9));

        noeud0.setColor(Color.BLUE);
        noeud1.setColor(Color.RED);
        noeud2.setColor(Color.YELLOW);
        noeud3.setColor(Color.WHITE);
        noeud4.setColor(Color.GREEN);
        noeud5.setColor(Color.GRAY);
        noeud6.setColor(Color.BLACK);
        noeud7.setColor(Color.ORANGE);
        noeud8.setColor(Color.CYAN);
        noeud9.setColor(Color.MAGENTA);

        tp.setTimeUnit(200);
        new JViewer(tp);
        tp.start();
    }

}
