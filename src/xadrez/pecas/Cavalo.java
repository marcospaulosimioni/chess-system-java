package xadrez.pecas;

import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDoXadrez;

public class Cavalo extends PecaDoXadrez{

	public Cavalo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "C";
	}

	private boolean podeMover(Posicao posicao) {		
		PecaDoXadrez p = (PecaDoXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);
		
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 2);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValor(posicao.getLinha() - 2, posicao.getColuna() - 1);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValor(posicao.getLinha() - 2, posicao.getColuna() + 1);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 2);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 2);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValor(posicao.getLinha() + 2, posicao.getColuna() + 1);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValor(posicao.getLinha() + 2, posicao.getColuna() - 1);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 2);
		if (getTabuleiro().verificarPosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		return mat;
	}
}
