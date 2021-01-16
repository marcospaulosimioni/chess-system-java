package xadrez;

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
	
	private void posicionarNovaPeca(char coluna, int linha, PecaDoXadrez peca) {
		tabuleiro.posicionarPeca(peca, new PosicaoXadrez(coluna, linha).posicionarCoordenadaXadrez());
	}
	
	private void configInicial() {		
		posicionarNovaPeca('b', 6, new Torre(tabuleiro, Cor.BRANCA));
		posicionarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETA));
		posicionarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCA));
	}
}
