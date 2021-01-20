package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogotabuleiro.Peca;
import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import jogotabuleiro.TabuleiroExcecao;
import xadrez.pecas.Bispo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean xeque;
	private boolean xequeMate;

	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRANCO;

		configInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getXeque() {
		return xeque;
	}

	public boolean getXequeMate() {
		return xequeMate;
	}

	public PecaDoXadrez[][] getPecas() {

		PecaDoXadrez[][] mat = new PecaDoXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];

		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaDoXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] movimentosPossiveis(PosicaoXadrez posicaoOrigem) {
		Posicao posicao = posicaoOrigem.posicionarCoordenadaXadrez();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}

	public PecaDoXadrez moverPecaNoTabuleiro(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.posicionarCoordenadaXadrez();
		Posicao destino = posicaoDestino.posicionarCoordenadaXadrez();
		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem, destino);
		Peca pecaCapturada = movimentar(origem, destino);

		if (EstaEmXeque(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadrezExcecao("Voc� n�o pode se colocar em xeque.");
		}

		xeque = (EstaEmXeque(adversario(jogadorAtual)) ? true : false);

		if (verificarXequeMate(adversario(jogadorAtual))) {
			xequeMate = true;
		} else {
			proximoTurno();
		}
		
		return (PecaDoXadrez) pecaCapturada;
	}

	private Peca movimentar(Posicao origem, Posicao destino) {
		PecaDoXadrez p = (PecaDoXadrez)tabuleiro.removerPeca(origem);
		p.incrementarQtdMovimentos();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.posicionarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaDoXadrez p = (PecaDoXadrez)tabuleiro.removerPeca(destino);
		p.decrementarQtdMovimentos();
		tabuleiro.posicionarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.posicionarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}
	}

	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new TabuleiroExcecao("N�o existe pe�a na posi��o de origem.");
		}
		if (jogadorAtual != ((PecaDoXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("A pe�a escolhida n�o � sua.");
		}
		if (!tabuleiro.peca(posicao).temMovimentoPossivelPeca()) {
			throw new XadrezExcecao("N�o existe movimentos poss�veis para a pe�a escolhida.");
		}
	}

	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezExcecao("A pe�a escolhida n�o pode mover para a posi��o de destino.");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO ? Cor.PRETO : Cor.BRANCO);
	}

	private Cor adversario(Cor cor) {
		return (cor == Cor.BRANCO ? Cor.PRETO : Cor.BRANCO);
	}

	private PecaDoXadrez Rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDoXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (PecaDoXadrez) p;
			}
		}
		throw new IllegalStateException("N�o existe o Rei " + cor + " no tabuleiro.");
	}

	private boolean EstaEmXeque(Cor cor) {

		Posicao posicaoRei = Rei(cor).getPosicaoXadrez().posicionarCoordenadaXadrez();
		List<Peca> pecasAdversario = pecasNoTabuleiro.stream()
				.filter(x -> ((PecaDoXadrez) x).getCor() == adversario(cor)).collect(Collectors.toList());

		for (Peca pc : pecasAdversario) {
			boolean[][] mat = pc.movimentosPossiveis();

			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean verificarXequeMate(Cor cor) {
		if (!EstaEmXeque(cor)) {
			return false;
		}

		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDoXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i<tabuleiro.getLinhas(); i++) {
				for (int j = 0; j<tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaDoXadrez) p).getPosicaoXadrez().posicionarCoordenadaXadrez();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = movimentar(origem, destino);
						boolean testarXeque = EstaEmXeque(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testarXeque) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void posicionarNovaPeca(char coluna, int linha, PecaDoXadrez peca) {
		tabuleiro.posicionarPeca(peca, new PosicaoXadrez(coluna, linha).posicionarCoordenadaXadrez());
		pecasNoTabuleiro.add(peca);
	}

	private void configInicial() {

		posicionarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));

		posicionarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));

	}
}
