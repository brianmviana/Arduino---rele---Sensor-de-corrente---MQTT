package com.brianmviana;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String server = "tcp://localhost:1883";

		SmartPub publisher = new SmartPub(server);
		SmartSub subscribe = new SmartSub(server);
		SmartSub subscribe2 = new SmartSub(server);
		SmartSub subscribe3 = new SmartSub(server);

		List<SmartSub> subscribes = new ArrayList<>();

		Scanner scan1 = new Scanner(System.in);
		Scanner scan2 = new Scanner(System.in);


		String topicoDispositivos = "/USUARIO/DISPOSITIVOS";
		String topicoRele = "/USUARIO/SALA/1/ATUADOR/RELE/1";
		String topicoCorrente = "/USUARIO/SALA/1/SENSOR/CORRENTE/1";
		String topicoCorrenteMedicao = "/USUARIO/SALA/1/SENSOR/CORRENTE/1/MEDICAO";


		int opcao = 0;

		subscribe.subscribe("teste");

		subscribe2.subscribe(topicoDispositivos);
		subscribe3.subscribe(topicoCorrenteMedicao);


		while(true) {
			System.out.println("1 - Descobrir \n2 - Sair");
			opcao = scan2.nextInt();
			switch (opcao) {
			case 1:
				List<String> l = subscribe2.getMensagensByTopic(topicoDispositivos);
				String myT = "1";
				if(l != null) {
					System.out.println("==================================");
					for (String string : l) {
						if(myT != string) {
							System.out.println(string);							
						}
					}
					System.out.println("==================================");

					System.out.println("<================================>");
					System.out.println("1 - Publicar \n2 - Subscrever \n3 - Sair");
					int op2 = scan2.nextInt();
					System.out.println("<================================>");

					switch (op2) {
					case 1:
						System.out.println("<<==============================>>");
						System.out.println("1 - Rele \n2 - Corrente \n3 - Sair");
						int op3 = scan2.nextInt();
						System.out.println("<<==============================>>");
						if(op3 == 1){
							System.out.println("<<<============================>>>");
							System.out.println("1 - Ligar \n2 - Desligar \n3 - Sair");	
							int op4 = scan2.nextInt();
							System.out.println("<<<============================>>>");
							if(op4 == 1) {
								publisher.publicar(topicoRele, "1");
							} else if( op4 == 2){								
								publisher.publicar(topicoRele, "0");
							} else {
								break;
							}
						}else if(op3 == 2){
							System.out.println("<<<============================>>>");
							System.out.println("1 - Medida Atual \n2 - Sair");	
							int op4 = scan2.nextInt();
							System.out.println("<<<============================>>>");
							if(op4 == 1) {
								publisher.publicar(topicoCorrente, "1");
								List<String> l2 = subscribe3.getMensagensByTopic(topicoCorrenteMedicao);
								String myT2 = "1";
								System.out.println("==================================");
								if(null != l2) {
								for (String string : l2) {
									if(myT2 != string) {
										System.out.println(string);							
									}
								}
								}
								System.out.println("==================================");
							} else {
								break;
							}
						} else {
							break;
						}
						break;
					default:
						break;
					}
				}else {
					break;
				}
			default:
				return;
			}
		}

	}
}



