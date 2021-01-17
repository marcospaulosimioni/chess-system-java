package aplicacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.PartidaDeXadrez;
import xadrez.PecaDoXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezExcecao;

public class Programa {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		
		while (true) {
			try {
				InterfaceUsuario.limparTela();
				InterfaceUsuario.imprimeTabuleiro(partidaDeXadrez.getPecas());
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

	}

}
