package xadrez;

import jogotabuleiro.Peca;
import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import jogotabuleiro.TabuleiroExcecao;
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
	
	public PecaDoXadrez moverPecaNoTabuleiro(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.posicionarCoordenadaXadrez();
		Posicao destino = posicaoDestino.posicionarCoordenadaXadrez();
		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem, destino);
		Peca capturarPeca = movimentar(origem, destino);
				return (PecaDoXadrez) capturarPeca;
		
	}
	
	private Peca movimentar(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.posicionarPeca(p, destino);
		return pecaCapturada;
	}
	
	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new TabuleiroExcecao("Não existe peça na posição de origem.");
		}
		if (!tabuleiro.peca(posicao).temMovimentoPossivelPeca()) {
			throw new XadrezExcecao("Não existe movimentos possíveis para a peça escolhida.");
		}
	}
	
	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezExcecao("A peça escolhida não pode mover para a posição de destino.");
		}
	}
	
	private void posicionarNovaPeca(char coluna, int linha, PecaDoXadrez peca) {
		tabuleiro.posicionarPeca(peca, new PosicaoXadrez(coluna, linha).posicionarCoordenadaXadrez());
	}
	
	private void configInicial() {		
		
		posicionarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCA));
		posicionarNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCA));
		posicionarNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCA));
		posicionarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCA));
		posicionarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCA));
		posicionarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCA));

		posicionarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETA));
		posicionarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETA));
		posicionarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETA));
		posicionarNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETA));
		posicionarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETA));
		posicionarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETA));
	}
}
