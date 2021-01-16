package jogotabuleiro;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private Peca[][] pecas;
	
	public Tabuleiro(int linhas, int colunas) {
		if (linhas < 1 || colunas < 1) {
			throw new TabuleiroExcecao("Erro ao criar o tabuleiro: é necessário que haja pelo menos uma linha e uma coluna.");
		}
		
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
		
	public Peca peca(int linha, int coluna) {
		if (!verificarPosicao(linha, coluna)) {
			throw new TabuleiroExcecao("Posição não permitida no tabuleiro.");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Posicao posicao) {
		if (!verificarPosicao(posicao)) {
			throw new TabuleiroExcecao("Posição não permitida no tabuleiro.");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void posicionarPeca(Peca peca, Posicao posicao) {
		if (temUmaPeca(posicao)) {
			throw new TabuleiroExcecao("Já existe uma peça na posição: " + posicao);
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	private boolean verificarPosicao(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
	}
	
	public boolean verificarPosicao(Posicao posicao) {
		return verificarPosicao(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean temUmaPeca(Posicao posicao) {
		if (!verificarPosicao(posicao)) {
			throw new TabuleiroExcecao("Posição não permitida no tabuleiro.");
		}
		return peca(posicao) != null;
	}
	
}
