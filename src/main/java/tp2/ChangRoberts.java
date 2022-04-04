package tp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class ChangRoberts extends Node {

	public static List<Integer> moyenne;
	public static int max;
	public static int min;
	public static int nbIteration;
	public static int nbMessage;
	private String etat;
	private int leader;

	public void onStart() {
		this.nbMessage = 0;
		this.etat = "CANDIDAT";
		this.leader = this.getID();
	}

	public void onMessage(Message message) {
		boolean voisinElu = ((ContenuMessageChangRoberts) message.getContent()).getElu();
		int voisinId = ((ContenuMessageChangRoberts) message.getContent()).getId();

		/**
		 * Si je reçois un message de type Elu(id)
		 * et que l'id est différent du mien, je partage la nouvelle !
		 */
		if (voisinElu && voisinId != this.getID()) {
			//System.out.println("Le noeud " + voisinId + " est élu, je partage la nouvelle");
			nbMessage++;
			sendAll(message);
		}

		/**
		 * Si mon ID est supérieur à celui de mon père,
		 * si je ne suis pas candidat je le deviens
		 * et je dis à mon voisin que je suis maintenant candidat
		 */
		else if (this.getID() > voisinId) {
			if (!this.etat.equals("CANDIDAT")) {
				this.etat = "CANDIDAT";
				//System.out.println("Le noeud " + this.getID() + " est à présent candidat");
			}
			nbMessage++;
			sendAll(new Message(new ContenuMessageChangRoberts(false, this.leader)));
		}

		/**
		 * Si mon id est inférieur à celui de mon père,
		 * je le désigne comme mon leader et je passe dans l'état PERDU.
		 * Je transmets à mon voisin l'id de mon leader.
		 */
		else if (this.getID() < voisinId) {
			//System.out.println("Le noeud " + this.getID() + " a perdu et change de couleur");
			this.etat = "PERDU";
			this.leader = voisinId;
			this.setColor(message.getSender().getColor());
			nbMessage++;
			sendAll(new Message(new ContenuMessageChangRoberts(false, this.leader)));
		}
		/**
		 * Si mon id est égal à celui de mon père et que je ne suis pas élu,
		 * je passe dans l'état ELU et je transmets un message de type
		 * ELU(id) à mon voisin.
		 */
		else if (!voisinElu) {
			//System.out.println("Le noeud " + this.getID() + " est elu");
			this.etat = "ELU";
			nbMessage++;
			sendAll(new Message(new ContenuMessageChangRoberts(true, this.leader)));
		}
		/**
		 * Si mon id est égal à celui de mon père et que je suis élu,
		 * j'arrête d'envoyer des messages et j'affiche le nombre total
		 * de messages envoyés.
		 */
		else {
//			System.out.println("Nombre de messages : " + nbMessage);

			/**
			 * Start over here !
			 */

			if (nbMessage > max) max = nbMessage;
			if (nbMessage < min) min = nbMessage;
			this.moyenne.add(nbMessage);

			if (nbIteration-- > 0) {
				reshuffle();
				nbMessage = 1;
				sendAll(new Message(new ContenuMessageChangRoberts(false, this.leader)));
			}
			else {
				System.out.println("Message min : " + min + " - Message max : " + max + " - Moyenne : " + this.moyenne.stream().mapToDouble(integer -> integer).average().getAsDouble());
			}

		}
	}

	public void onSelection() {
		this.moyenne = new ArrayList<>();
		this.nbIteration = 100;
		this.min = this.getTopology().getNodes().size() * this.getTopology().getNodes().size();
		this.max = 0;
		//System.out.println("Le noeud " + this.getID() + " envoie son message");
		nbMessage++;
		sendAll(new Message(new ContenuMessageChangRoberts(false, this.leader)));
	}

	public void reshuffle() {
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

		for (int i = 0; i < ids.size(); i++) {
			getTopology().getNodes().get(i).setID(ids.get(i));
			getTopology().getNodes().get(i).setColor(Color.getIndexedColors().get(i % Color.getIndexedColors().size()));
		}
		this.getTopology().restart();
	}

}
