package xadrez.pecas;

import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDoXadrez;

public class Peao extends PecaDoXadrez {

	public Peao(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		if (getCor() == Cor.BRANCO) {
			p.setValor(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)
					&& getTabuleiro().verificarPosicao(p2) && !getTabuleiro().temUmaPeca(p2)
					&& getQtdMovimentos() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
		} else {
			p.setValor(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p3 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)
					&& getTabuleiro().verificarPosicao(p3) && !getTabuleiro().temUmaPeca(p3)
					&& getQtdMovimentos() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
		}
		return mat;
	}

	public String toString() {
		return "P";
	}
}
