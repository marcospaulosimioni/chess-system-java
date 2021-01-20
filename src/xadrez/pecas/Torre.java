package xadrez.pecas;

import jogotabuleiro.Posicao;
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
		
		Posicao p = new Posicao(0, 0);
		
		//para cima
		p.setValor(posicao.getLinha() - 1, posicao.getColuna());
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() - 1);			
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//para esquerda
		p.setValor(posicao.getLinha(), posicao.getColuna() - 1);
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() - 1);			
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//para direita
		p.setValor(posicao.getLinha(), posicao.getColuna() + 1);
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() + 1);			
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		//para baixo
		p.setValor(posicao.getLinha() + 1, posicao.getColuna());
		
		while (getTabuleiro().verificarPosicao(p) && !getTabuleiro().temUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() + 1);			
		}
		
		if (getTabuleiro().verificarPosicao(p) && temPecaAdversaria(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		return mat;
	}
}
