package xadrez;

import jogotabuleiro.Peca;
import jogotabuleiro.Tabuleiro;

public class PecaDoXadrez extends Peca {

	private Cor cor;

	public PecaDoXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
}
