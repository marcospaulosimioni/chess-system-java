package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogotabuleiro.Peca;
import jogotabuleiro.Posicao;
import jogotabuleiro.Tabuleiro;
import jogotabuleiro.TabuleiroExcecao;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaDeXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean xeque;
	private boolean xequeMate;
	private PecaDoXadrez vulneravelEnPassant;
	private PecaDoXadrez promovido;

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

	public PecaDoXadrez getVulneravelEnPassant() {
		return vulneravelEnPassant;
	}

	public PecaDoXadrez getPromovido() {
		return promovido;
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
			throw new XadrezExcecao("Você não pode se colocar em xeque.");
		}

		PecaDoXadrez pecaMovida = (PecaDoXadrez) tabuleiro.peca(destino);

		//# movimento especial promoção
		promovido = null;
		if (pecaMovida instanceof Peao) {
			if (pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0 || pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7) {
				promovido = (PecaDoXadrez)tabuleiro.peca(destino);
				promovido = substituirPecaPromovida("Q");
				
			}
		}
		
		xeque = (EstaEmXeque(adversario(jogadorAtual)) ? true : false);

		if (verificarXequeMate(adversario(jogadorAtual))) {
			xequeMate = true;
		} else {
			proximoTurno();
		}

		if (pecaMovida instanceof Peao
				&& (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			vulneravelEnPassant = pecaMovida;
		} else {
			vulneravelEnPassant = null;
		}

		return (PecaDoXadrez) pecaCapturada;
	}

	public PecaDoXadrez substituirPecaPromovida(String tipo) {
		if (promovido == null) {
			throw new IllegalStateException("Não há peça para ser promovida.");
		}
		if (!tipo.equals("B") && !tipo.equals("N") && !tipo.equals("R") && !tipo.equals("Q")) {
			throw new InvalidParameterException("Tipo de peça inválido para promoção.");
		}
		Posicao pos = promovido.getPosicaoXadrez().posicionarCoordenadaXadrez();
		Peca p = tabuleiro.removerPeca(pos);
		pecasNoTabuleiro.remove(p);
		
		PecaDoXadrez novaPeca = novaPeca(tipo, promovido.getCor());
		tabuleiro.posicionarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);
		
		return novaPeca;
	}
	
	private PecaDoXadrez novaPeca(String tipo, Cor cor) {
		if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
		else if (tipo.equals("N")) return new Cavalo(tabuleiro, cor);
		else if (tipo.equals("Q")) return new Rainha(tabuleiro, cor);
		else return new Torre(tabuleiro, cor);
	}
	
	private Peca movimentar(Posicao origem, Posicao destino) {
		PecaDoXadrez p = (PecaDoXadrez) tabuleiro.removerPeca(origem);
		p.incrementarQtdMovimentos();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.posicionarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		// #movimento especial lançando torre do lado do rei (roque pequeno)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDoXadrez torre = (PecaDoXadrez) tabuleiro.removerPeca(origemTorre);
			tabuleiro.posicionarPeca(torre, destinoTorre);
			torre.incrementarQtdMovimentos();
		}

		// #movimento especial lançando torre do lado do rainha (roque grande)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDoXadrez torre = (PecaDoXadrez) tabuleiro.removerPeca(origemTorre);
			tabuleiro.posicionarPeca(torre, destinoTorre);
			torre.incrementarQtdMovimentos();
		}

		// #movimento especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if (p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removerPeca(posicaoPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}

		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaDoXadrez p = (PecaDoXadrez) tabuleiro.removerPeca(destino);
		p.decrementarQtdMovimentos();
		tabuleiro.posicionarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.posicionarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}

		// #movimento especial lançando torre do lado do rei (roque pequeno)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDoXadrez torre = (PecaDoXadrez) tabuleiro.removerPeca(destinoTorre);
			tabuleiro.posicionarPeca(torre, origemTorre);
			torre.decrementarQtdMovimentos();
		}

		// #movimento especial lançando torre do lado do rainha (roque grande)
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDoXadrez torre = (PecaDoXadrez) tabuleiro.removerPeca(destinoTorre);
			tabuleiro.posicionarPeca(torre, origemTorre);
			torre.decrementarQtdMovimentos();
		}
		
		// #movimento especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == vulneravelEnPassant) {
				PecaDoXadrez peao = (PecaDoXadrez)tabuleiro.removerPeca(destino);
				Posicao posicaoPeao;
				if (p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.posicionarPeca(peao, posicaoPeao);
			}
		}
	}

	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new TabuleiroExcecao("Não existe peça na posição de origem.");
		}
		if (jogadorAtual != ((PecaDoXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("A peça escolhida não é sua.");
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
		throw new IllegalStateException("Não existe o Rei " + cor + " no tabuleiro.");
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
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
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
		posicionarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		posicionarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		posicionarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

		posicionarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		posicionarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
		posicionarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));

	}
}
