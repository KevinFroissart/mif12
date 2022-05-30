package tp3;

public class MessageCoordinateur {

	int phase;
	public int v;
	public int ronde;
	public boolean accepte;
	public int rondeProposition;
	public int rondeEstimation;

	public MessageCoordinateur(int v, int rondeProposition, int rondeEstimation) {
		this.v = v;
		this.rondeProposition = rondeProposition;
		this.rondeEstimation = rondeEstimation;
		this.phase = 1;
	}

	public MessageCoordinateur(int v, int ronde) {
		this.v = v;
		this.ronde = ronde;
		this.phase = 2;
	}

	public MessageCoordinateur(int ronde, boolean accepte) {
		this.ronde = ronde;
		this.accepte = accepte;
		this.phase = 3;
	}

	public MessageCoordinateur(int v) {
		this.v = v;
		this.phase = 4;
	}
}
