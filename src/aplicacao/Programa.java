package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaDeXadrez;
import xadrez.PecaDoXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezExcecao;

public class Programa {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		List<PecaDoXadrez> capturadas = new ArrayList<>();
		
		while (!partidaDeXadrez.getXequeMate()) {
			try {
				InterfaceUsuario.limparTela();
				InterfaceUsuario.imprimePartida(partidaDeXadrez, capturadas);
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = InterfaceUsuario.lerPosicaoXadrez(sc);
				
				boolean[][] movimentosPossiveis = partidaDeXadrez.movimentosPossiveis(origem);
				InterfaceUsuario.limparTela();
				InterfaceUsuario.imprimeTabuleiro(partidaDeXadrez.getPecas(), movimentosPossiveis);
				
				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadrez destino = InterfaceUsuario.lerPosicaoXadrez(sc);
				
				PecaDoXadrez pecaCapturada = partidaDeXadrez.moverPecaNoTabuleiro(origem, destino);
				
				if (pecaCapturada != null) {
					capturadas.add(pecaCapturada);
				}
				
				if (partidaDeXadrez.getPromovido() != null) {
					System.out.print("Digite uma pe�a para promo��o (B/N/R/Q): ");
					String tipo = sc.nextLine().toUpperCase();
					while (!tipo.equals("B") && !tipo.equals("N") && !tipo.equals("R") && !tipo.equals("Q")) {
						System.out.print("Valor inv�lido! Digite uma pe�a para promo��o (B/N/R/Q): ");
						tipo = sc.nextLine().toUpperCase();
					}
					partidaDeXadrez.substituirPecaPromovida(tipo);
				}
			}
			catch(XadrezExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		InterfaceUsuario.limparTela();
		InterfaceUsuario.imprimePartida(partidaDeXadrez, capturadas);
	}

}
