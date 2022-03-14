package tp1;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class TP1Main {
	public static void main(String[] args){ // On déclare le programme principal
		Topology tp = new Topology(); // Création d’un nouveau système distribué
		tp.setDefaultNodeModel(NoeudTest.class);
		tp.setTimeUnit(1500);
		new JViewer(tp); // On active l’interface graphique de JBotSim
		tp.start(); // On démarre le tout
	}
}
