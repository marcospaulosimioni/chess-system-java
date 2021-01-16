package xadrez;

import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private Tabuleiro tabuleiro;
	
	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		configInicial();
	}
	
	public PecaDoXadrez[][] getPecas(){
		
		PecaDoXadrez[][] mat = new PecaDoXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		
		for (int i=0; i<tabuleiro.getLinhas(); i++) {
			for (int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaDoXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	private void configInicial() {		
		tabuleiro.posicionarPeca(new Torre(tabuleiro, Cor.BRANCA), new Posicao(2, 1));
		tabuleiro.posicionarPeca(new Rei(tabuleiro, Cor.PRETA), new Posicao(0, 4));
		tabuleiro.posicionarPeca(new Rei(tabuleiro, Cor.BRANCA), new Posicao(7, 4));
	}
}
