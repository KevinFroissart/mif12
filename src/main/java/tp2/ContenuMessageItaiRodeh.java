package tp2;

public class ContenuMessageItaiRodeh {
	private int id;
	private int phase;
	private int nbTransmission;
	private boolean unique;
	private boolean fini;

	public ContenuMessageItaiRodeh(int id, int phase, int nbTransmission, boolean unique, boolean fini) {
		this.id = id;
		this.phase = phase;
		this.nbTransmission = nbTransmission;
		this.unique = unique;
		this.fini = fini;
	}

	public int getId() {
		return id;
	}

	public int getPhase() {
		return phase;
	}

	public int getNbTransmission() {
		return nbTransmission;
	}

	public boolean isUnique() {
		return unique;
	}

	public boolean isFini() {
		return fini;
	}
}
