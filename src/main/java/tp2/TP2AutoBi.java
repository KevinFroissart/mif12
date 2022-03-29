package tp2;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import tp1.NoeudTestExo4;

public class TP2AutoBi {

    public static final int TAILLE_ANNEAU = 10;
    public static final int TAILLE_FENETRE = 400;

    /*
    Abscisse = cos(angle) × rayon + abscisse_centre
    Ordonnée = sin(angle) × rayon + ordonnee_centre
     */

    public static void main(String[] args) {
        Topology tp = new Topology();

        for (int i = 0; i < TAILLE_ANNEAU; i++) {
            double angle = Math.toRadians((360 / TAILLE_ANNEAU) * i);
            double abscisse = Math.cos(angle) * (TAILLE_FENETRE / 4) + (TAILLE_FENETRE / 2);
            double ordonnee = Math.sin(angle) * (TAILLE_FENETRE / 4) + (TAILLE_FENETRE / 2);
            tp.addNode(abscisse, ordonnee, new NoeudTestExo4());
        }

//        int cpt = 1;
//        for (int i = 0; i < TAILLE_ANNEAU; i++) {
//            int x = (TAILLE_FENETRE / (TAILLE_ANNEAU / 2)) + ((TAILLE_FENETRE / (TAILLE_ANNEAU / 2)) * i) % (TAILLE_FENETRE);
//            int tmp = ((TAILLE_FENETRE / 2) / (TAILLE_ANNEAU / 2));
//
//            int delta = tmp * (i + 1);
//
//            if (delta >= (TAILLE_FENETRE / 2) + 1) delta = delta == TAILLE_FENETRE ? - delta : - delta % (TAILLE_FENETRE / 2);
//
//            int z = delta;
//
//            if(i == TAILLE_ANNEAU / 2) cpt = 1;
//
//            if (Math.abs(delta) > ((TAILLE_FENETRE / 4) + (TAILLE_FENETRE / TAILLE_ANNEAU / 2))) {
//                z = ((TAILLE_FENETRE / 4) + (TAILLE_FENETRE / TAILLE_ANNEAU / 2));
//                z = delta < 0 ? - z + (tmp * cpt++) : z - (tmp * cpt++);
//            }
//            System.out.println(x + ":" + (TAILLE_FENETRE / 2 - z));
//            tp.addNode(x, TAILLE_FENETRE / 2 - z, new NoeudTestExo4());
//        }

        tp.setTimeUnit(500);
        tp.disableWireless();
        new JViewer(tp);
        tp.start();
    }

}
