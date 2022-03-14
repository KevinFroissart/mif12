package tp1;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class NoeudTestExo4 extends Node {

	private static TreeSet<Node> arbre;
	private static int nbBCAST;
	private static int nbReponse;

	private boolean receivedJoin;
	private boolean isRacine;
	private Node pere;
	private HashMap<Node, Boolean> enfantsMap;

	public void onStart() {
		arbre = new TreeSet<>();
		enfantsMap = new HashMap<>();
		receivedJoin = false;
		isRacine = false;
		nbReponse = 0;
		nbBCAST = 0;
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
			}
			else {
				getNeighbors()
						.stream()
						.filter(x -> !x.equals(m.getSender()))
						.forEach(x -> {
							nbReponse++;
							send(x, m);
							enfantsMap.put(x, false);
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
				else {
					setColor(Color.RED);
					sendAll(new Message("BCAST"));
				}
			}
		}
		else if (m.getContent().equals("BCAST")) {
			setColor(Color.RED);
			enfantsMap.keySet().forEach(e -> send(e, m));
			nbBCAST ++;
		}

		if(nbBCAST == getTopology().getNodes().size() - 1) {
			System.out.println("BCAST : " + nbBCAST);
			System.out.println("Messages envoyés : " + nbReponse);
		}
	}

	public void onSelection() {
		isRacine = true;
		arbre.add(this);
		setColor(Color.YELLOW);
		getNeighbors().forEach(e -> enfantsMap.put(e, false));
		sendAll(new Message("JOIN"));
		nbReponse += getNeighbors().size();
	}

}

