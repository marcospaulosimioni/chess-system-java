package xadrez.pecas;

import jogotabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDoXadrez;

public class Torre extends PecaDoXadrez {

	public Torre(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "T";
	}
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		return mat;
	}
}
