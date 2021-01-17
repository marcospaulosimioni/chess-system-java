package xadrez.pecas;

import jogotabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDoXadrez;

public class Rei extends PecaDoXadrez{

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		return mat;
	}
}
