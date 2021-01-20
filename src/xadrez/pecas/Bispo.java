package xadrez.pecas;

import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDoXadrez;

public class Bispo extends PecaDoXadrez {

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "B";
	}
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0);
		
		//noroeste
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(p.getLinha() - 1, p.getColuna() - 1);			
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//nordeste
		p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(p.getLinha() - 1, p.getColuna() + 1);			
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//sudeste
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(p.getLinha() + 1, p.getColuna() + 1);		
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//sudoeste
		p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValor(p.getLinha() + 1, p.getColuna() - 1);;			
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		return mat;
	}
}
