package tp1;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class NoeudTest extends Node {
	private static int compte;
	private static int nbReceived;
	private boolean received;

	public void onStart() {
		received = false;
		compte = 0;
		nbReceived = 0;
	}

	public void onMessage(Message m) {
		if (!received) {
			received = true;
			nbReceived++;
			setColor(Color.RED);
			compte += getNeighbors().size();

			if(nbReceived == getTopology().getNodes().size()) {
				System.out.println("compte : " + compte);
			}
			System.out.println(getID() + " envoie " +  getNeighbors().size() + " messages");
			System.out.println("compte cournat : " + compte);
			sendAll(m);
		}
	}

	public void onSelection() {
		compte += getNeighbors().size();
		System.out.println(getID() + " envoie " +  getNeighbors().size() + " messages");
		System.out.println("compte cournat : " + compte);
		sendAll(new Message());
	}

	/**
	 * Q4)
	 *  - circulaire 6 : 14
	 *  - circulaire 5 : 12
	 *  - circulaire 4 : 10
	 *  - fortement connexe 6 : 35
	 *  - fortement connexe 5 : 24
	 *  - fortement connexe 4 : 15
	 *
	 *	Soit, pour le circulaire : n*2 + 2
	 *	pour le fortement connexe : n² - 1
	 *
	 * Q5) sur un arbre fortement connexe de 4 nœuds, il faut 15 messages
	 */
}

