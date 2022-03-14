package tp1;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class NoeudTestExo4 extends Node {

	private static TreeSet<Node> arbre;
	private static int nbReponse;

	private boolean receivedJoin;
	private boolean isRacine;
	private Node pere;
	private HashMap<Node, Boolean> enfantsMap;
	private HashMap<Node, Boolean> enfantsEcho;

	public void onStart() {
		arbre = new TreeSet<>();
		enfantsMap = new HashMap<>();
		enfantsEcho = new HashMap<>();
		receivedJoin = false;
		isRacine = false;
		nbReponse = 0;
	}

	public void onMessage(Message m) {
		if (!receivedJoin && m.getContent().equals("JOIN")) {
			receivedJoin = true;
			arbre.add(this);
			pere = m.getSender();
			setColor(Color.YELLOW);
			getCommonLinkWith(pere).setColor(Color.CYAN);

			if (getNeighbors().size() == 1) {
				nbReponse++;
				send(pere, new Message("BACK"));
				setColor(Color.GREEN);

				send(pere, new Message("ECHO"));
			}
			else {
				getNeighbors()
						.stream()
						.filter(x -> !x.equals(m.getSender()))
						.forEach(x -> {
							nbReponse++;
							send(x, m);
							enfantsMap.put(x, false);
							enfantsEcho.put(x, false);
						});
			}
		}
		else if (m.getContent().equals("JOIN")) {
			send(m.getSender(), new Message("BACKNO"));
			nbReponse++;
		}
		else if (m.getContent().equals("BACK") || m.getContent().equals("BACKNO")) {
			enfantsMap.replace(m.getSender(), true);
			if (enfantsMap.values().stream().filter(v -> !v).collect(Collectors.toList()).isEmpty()) {
				setColor(Color.GREEN);
				if (!isRacine) {
					nbReponse++;
					send(pere, new Message("BACK"));
				}
			}
		}

		else if (m.getContent().equals("ECHO")) {
			enfantsEcho.replace(m.getSender(), true);
			if (enfantsEcho.values().stream().filter(v -> !v).collect(Collectors.toList()).isEmpty()) {
				if (!isRacine) {
					send(pere, m);
				}
				else {
					setColor(Color.BLUE);
					System.out.println("Total message : " + nbReponse);
				}
			}
		}
	}

	public void onSelection() {
		isRacine = true;
		arbre.add(this);
		setColor(Color.YELLOW);
		getNeighbors().forEach(e -> enfantsMap.put(e, false));
		enfantsEcho = enfantsMap;
		sendAll(new Message("JOIN"));
		nbReponse += getNeighbors().size();
	}

}

