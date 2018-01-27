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
		{	System.out.println('\n' + "O que voc� deseja fazer?");
/* ==> */   System.out.println('\n' + "1. Cadastrar um lance de um produto");
/* ==> */   System.out.println("2. Remover um lance");
/* ==> */   System.out.println("3. Recuperar todos os lances");
			System.out.println("4. Sair");
						
			int opcao = Console.readInt('\n' + 
				"Digite um n�mero entre 1 e 4:");			
					
			switch (opcao)
			{	case 1:
				{
					long idProduto = Console.readInt('\n' + 
									"Informe o n�mero do produto: ");
					valor = Console.readDouble('\n' + 
									"Informe o valor do lance: ");
					umaData = Console.readLine('\n' +
									"Informe a data de emiss�o do lance: ");
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
						"Digite o n�mero do lance que voc� deseja remover: ");
									
					try
					{	umLance = lanceDAO.recuperaUmLance(resposta);	
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					// Como associa��es single-point s�o recuperadas?
					// System.out.println(umLance.getProduto()
					//						  .getClass()
					//						  .getName());							

					System.out.println("Classe = " + umLance
														.getProduto()
														.getClass()
														.getName());							
					// Se tirarmos o coment�rio abaixo ocorrer� um  erro
					// uma vez que a  sess�o que  recuperou o  lance  j�
					// foi fechada.
					
					// System.out.println(umLance.getProduto().getNome());							
										
					System.out.println('\n' + 
						"N�mero = " + umLance.getId() + 
						"    Valor = " + umLance.getValor() +
						"    Data de Emiss�o = " + umLance.getDataCriacao());
																						
					String resp = Console.readLine('\n' + 
						"Confirma a remo��o do lance?");

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
					{	System.out.println('\n' + "Lance n�o removido.");
					}
					
					break;
				}

				case 3:
				{	List<Lance> arrayLances = lanceDAO.recuperaLances();
									
					if (arrayLances.size() == 0)
					{	System.out.println('\n' + 
							"N�o h� lances cadastrados.");
						break;
					}
										
					System.out.println("");
					for (Lance lance : arrayLances)
					{	System.out.println(	
							"N�mero = " + lance.getId() + 
							"  Valor = " + lance.getValor() +
							"  Data de Emiss�o = " + 
										lance.getDataCriacao());
					}
										
					break;
				}

				case 4:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Op��o inv�lida!");						
			}
		}		
	}
}
