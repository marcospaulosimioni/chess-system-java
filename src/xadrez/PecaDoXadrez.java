package xadrez;

import jogotabuleiro.Peca;
import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;

public abstract class PecaDoXadrez extends Peca {

	private Cor cor;
	private int qtdMovimentos;

	public PecaDoXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public int getQtdMovimentos() {
		return qtdMovimentos;
	}
	
	public void incrementarQtdMovimentos() {
		qtdMovimentos++;
	}
	
	public void decrementarQtdMovimentos() {
		qtdMovimentos--;
	}
	
	public PosicaoXadrez getPosicaoXadrez() {
		return PosicaoXadrez.converterPosicaoXadrez(posicao);
	}
	protected boolean temPecaAdversaria(Posicao posicao) {
		PecaDoXadrez p = (PecaDoXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}
}
