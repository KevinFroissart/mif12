package tp2;

import java.util.Random;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class ItaiRodeh extends Node {

	private boolean candidat;
	private int phase;
	private int id;

	public void onStart() {
		getRandomId();
		this.phase = 1;
		this.candidat = new Random().nextInt(5) < 2;
		System.out.println(this.id + " est candidat : " + (this.candidat ?  "oui" : "non"));
	}

	public void onMessage(Message message) {
		int id = ((ContenuMessageItaiRodeh) message.getContent()).getId();
		int phase = ((ContenuMessageItaiRodeh) message.getContent()).getPhase();
		int nbTransmission = ((ContenuMessageItaiRodeh) message.getContent()).getNbTransmission();
		boolean unique = ((ContenuMessageItaiRodeh) message.getContent()).isUnique();
		boolean fini = ((ContenuMessageItaiRodeh) message.getContent()).isFini();

		if (fini && id != this.id) {
			System.out.println(this.id + ": L'algo est fini, je me met de la même couleur que le leader");
			this.setColor(message.getSender().getColor());
			sendAll(new Message(new ContenuMessageItaiRodeh(id, phase, 1, unique, true)));
		}
		else if (!candidat) {
			System.out.println(this.id + ": Je ne suis pas candidat, je retransmet");
			sendAll(new Message(new ContenuMessageItaiRodeh(id, phase, ++nbTransmission, unique, fini)));
		}
		else if (nbTransmission == this.getTopology().getNodes().size()) {
			if (unique) {
				System.out.println(this.id + ": J'ai fait un tour complet et je suis unique, j'ai fini");
				sendAll(new Message(new ContenuMessageItaiRodeh(id, phase, 1, true, true)));
			}
			else {
				System.out.println(this.id + ": J'ai fait un tour complet mais je ne suis pas unique, je passe à la phase suivante");
				sendAll(new Message(new ContenuMessageItaiRodeh(getRandomId(), ++phase, 1, false, fini)));
			}
		}
		else if (id == this.id && this.phase == phase) {
			System.out.println(this.id + ": Un candidat avec mon ID existe déjà, j'en toruve un autre");
			sendAll(new Message(new ContenuMessageItaiRodeh(id, phase, ++nbTransmission, false, fini)));
		}
		else if (!fini) {
			System.out.println(this.id + ": J'abandonne ma candidature");
			this.candidat = false;
			sendAll(new Message(new ContenuMessageItaiRodeh(id, phase, ++nbTransmission, unique, fini)));
		}
	}

	public void onSelection() {
		this.candidat = true;
		sendAll(new Message(new ContenuMessageItaiRodeh(getRandomId(), this.phase, 1, true, false)));
	}

	public int getRandomId() {
		this.id = new Random().nextInt(20);
		return this.id;
	}

}
