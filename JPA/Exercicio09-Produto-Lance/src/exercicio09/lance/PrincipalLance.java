package exercicio09.lance;

import corejava.*;
import exercicio09.produto.*;
import exercicio09.util.*;

import java.sql.Date;
import java.util.List;

public class PrincipalLance 
{	public static void main (String[] args) 
	{	
		double valor;
		Date dataCriacao;
		String umaData;

		Lance umLance;

		ProdutoDAO produtoDAO = new ProdutoDAOImpl();
		LanceDAO lanceDAO = new LanceDAOImpl();

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
/* ==> */   System.out.println('\n' + "1. Cadastrar um lance de um produto");
/* ==> */   System.out.println("2. Remover um lance");
/* ==> */   System.out.println("3. Recuperar todos os lances");
			System.out.println("4. Sair");
						
			int opcao = Console.readInt('\n' + 
				"Digite um número entre 1 e 4:");			
					
			switch (opcao)
			{	case 1:
				{
					long idProduto = Console.readInt('\n' + 
									"Informe o número do produto: ");
					valor = Console.readDouble('\n' + 
									"Informe o valor do lance: ");
					umaData = Console.readLine('\n' +
									"Informe a data de emissão do lance: ");
					dataCriacao = Util.strToDate(umaData);
								
					umLance = new Lance (valor, dataCriacao);
					
					try
					{	produtoDAO.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					// Adiciona o lance ao produto

					produtoDAO.adicionarLance(idProduto, umLance);

					System.out.println('\n' + "Lance adicionado com sucesso");						

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o número do lance que você deseja remover: ");
									
					try
					{	umLance = lanceDAO.recuperaUmLance(resposta);	
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					// Como associações single-point são recuperadas?
					// System.out.println(umLance.getProduto()
					//						  .getClass()
					//						  .getName());							

					System.out.println("Classe = " + umLance
														.getProduto()
														.getClass()
														.getName());							
					// Se tirarmos o comentário abaixo ocorrerá um  erro
					// uma vez que a  sessão que  recuperou o  lance  já
					// foi fechada.
					
					// System.out.println(umLance.getProduto().getNome());							
										
					System.out.println('\n' + 
						"Número = " + umLance.getId() + 
						"    Valor = " + umLance.getValor() +
						"    Data de Emissão = " + umLance.getDataCriacao());
																						
					String resp = Console.readLine('\n' + 
						"Confirma a remoção do lance?");

					if(resp.equals("s"))
					{	try
						{	lanceDAO.exclui(umLance);
							System.out.println('\n' + "Lance removido com sucesso!");
						}
						catch(AplicacaoException e)
						{	System.out.println('\n' + e.getMessage());
						}
					}
					else
					{	System.out.println('\n' + "Lance não removido.");
					}
					
					break;
				}

				case 3:
				{	List<Lance> arrayLances = lanceDAO.recuperaLances();
									
					if (arrayLances.size() == 0)
					{	System.out.println('\n' + 
							"Não há lances cadastrados.");
						break;
					}
										
					System.out.println("");
					for (Lance lance : arrayLances)
					{	System.out.println(	
							"Número = " + lance.getId() + 
							"  Valor = " + lance.getValor() +
							"  Data de Emissão = " + 
										lance.getDataCriacao());
					}
										
					break;
				}

				case 4:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");						
			}
		}		
	}
}
