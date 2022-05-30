package tp3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class NoeudCoordinateur extends Noeud {

	private Map<Integer, Set<MessageCoordinateur>> estimationsReceived;
	private Map<Integer, MessageCoordinateur> propsReceived;
	private Set<MessageCoordinateur> acksReceived;
	private int v;
	private int ts;
	private int ronde;

	/**
	 * 1. Estimation
	 * 2. Proposition
	 * 3. Acquittement
	 * 4. Décision
	 */
	private int phase;

	public void onStart() {
		super.onStart();
		this.ts = 0;
		this.v = (int) Math.round(Math.random() * 100);
		this.ronde = 0;
		this.phase = 1;
		estimationsReceived = new HashMap<>();
		propsReceived = new HashMap<>();
		acksReceived = new HashSet<>();
		nouvelleRonde();
	}

	public void onClock() {
		if (!alive) return;
		super.onClock();
		if (!trusted.contains(coordinateur(ronde))) {
			nouvelleRonde();
		}
	}

	public void onMessage(Message m) {
		MessageCoordinateur message;
		if (!alive) return;
		if (m.getContent().equals("Ping") || m.getContent().equals("Ping")) {
			super.onMessage(m);
		}
		/* Estimation */
		else if ((message = (MessageCoordinateur) m.getContent()).phase == 1) {
			if (this.estimationsReceived.get(message.ronde) == null) {
				this.estimationsReceived.put(message.ronde, new HashSet<>());
			}
			if (message.ronde > ronde) return;
			if (message.ronde <= ronde) this.estimationsReceived.get(message.ronde).add((MessageCoordinateur) m.getContent());

			// Majorité
			else if (this.estimationsReceived.get(ronde).size() + 1 > this.getTopology().getNodes().size() / 2) {
				MessageCoordinateur messageAPrendre = this.estimationsReceived.get(ronde).stream().findFirst().get();
				for (MessageCoordinateur messageCoordinateur : this.estimationsReceived.get(ronde)) {
					if (messageCoordinateur.rondeProposition > messageAPrendre.rondeProposition) {
						messageAPrendre = messageCoordinateur;
					}
				}
				this.v = messageAPrendre.v;
				this.ts = messageAPrendre.rondeProposition;
				System.out.println("Majorité d'estimation reçu");
				// Proposition
				acksReceived.clear();
				sendAll(new Message(new MessageCoordinateur(this.v, this.ronde)));
			}
		}
		/* Proposition */
		else if (message.phase == 2) {
			if (message.ronde > ronde) this.send(coordinateur(ronde), new Message(new MessageCoordinateur(ronde, false)));
			if (message.ronde < ronde) propsReceived.put(message.ronde, message);
			else {
				this.v = message.v;
				this.ts = message.ronde;
				// Ack
				this.send(coordinateur(ronde), new Message(new MessageCoordinateur(ronde, true)));
				nouvelleRonde();
			}
		}
		/* Ack */
		else if (message.phase == 3) {
			if (message.ronde != ronde) return;
			this.acksReceived.add(message);
			if (this.acksReceived.size() + 1 > this.getTopology().getNodes().size() /2) {
				boolean hasNacks = false;
				System.out.println("Majorité de ACK reçu");
				for (MessageCoordinateur messageCoordinateur : this.acksReceived) {
					if (!messageCoordinateur.accepte) hasNacks = true;
				}
				if (hasNacks) nouvelleRonde();
				else {
					System.out.println("Valeur v choisie : " + v);
					sendAll(new Message(new MessageCoordinateur(v)));
				}
			}
		}
		/* Décision */
		else if (message.phase == 4) {
			this.v = message.v;
			System.out.println("Noeud " + this.getID() + " choisi pour valeur v : " + this.v);
			this.alive = false;
		}
	}

	private void nouvelleRonde() {
		this.ronde++;
		System.out.println("Le noeud " + this.getID() + " passe à la ronde " + this.ronde);
		this.phase = 1;
		this.send(coordinateur(ronde), new Message(new MessageCoordinateur(v, ts, ronde)));
		if (this.propsReceived.get(ronde) != null) {
			MessageCoordinateur message = this.propsReceived.get(ronde);
			this.v = message.v;
			this.ts = message.ronde;
			// Ack
			this.send(coordinateur(ronde), new Message(new MessageCoordinateur(ronde, trusted.contains(coordinateur(ronde)))));
			nouvelleRonde();
		}
	}

	private Node coordinateur(int ronde) {
		int id = ronde % getTopology().getNodes().size();
		return getTopology().findNodeById(id);
	}
}
