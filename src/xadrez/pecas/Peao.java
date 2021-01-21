package xadrez.pecas;

import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDoXadrez;

public class Peao extends PecaDoXadrez {

	private PartidaDeXadrez partidaDeXadrez;
	
	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partidaDeXadrez) {
		super(tabuleiro, cor);
		this.partidaDeXadrez = partidaDeXadrez;
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
			
			// #movimento especial en passant peças brancas
			if (posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().verificarPosicao(esquerda) && temPecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == partidaDeXadrez.getVulneravelEnPassant()) {
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().verificarPosicao(direita) && temPecaAdversaria(direita) && getTabuleiro().peca(direita) == partidaDeXadrez.getVulneravelEnPassant()) {
					mat[direita.getLinha() - 1][direita.getColuna()] = true;
				}
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
			// #movimento especial en passant peças pretas
			if (posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().verificarPosicao(esquerda) && temPecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == partidaDeXadrez.getVulneravelEnPassant()) {
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().verificarPosicao(direita) && temPecaAdversaria(direita) && getTabuleiro().peca(direita) == partidaDeXadrez.getVulneravelEnPassant()) {
					mat[direita.getLinha() + 1][direita.getColuna()] = true;
				}
			}
		}
		return mat;
	}

	public String toString() {
		return "P";
	}
}
