package ex04.cliente;

import javax.naming.Context;
import javax.naming.NamingException;

import corejava.Console;
import ex04.dataObject.PedidoDO;
import ex04.dominio.ItemDePedido;
import ex04.dominio.Pedido;
import ex04.ejb.CarrinhoEJBRemote;
import ex04.excecao.AplicacaoException;
import ex04.excecao.EstadoConversacionalIncompleto;

public class PrincipalCarrinhoDeCompras
{	public static void main (String[] args) 
	{	
	    Context jndiContext;

		CarrinhoEJBRemote carrinhoEJBRemote = null;

		try 
		{	jndiContext = new javax.naming.InitialContext();
		    Object ref = jndiContext.lookup("exercicio04.CarrinhoEJBBean/remote");
		    carrinhoEJBRemote = (CarrinhoEJBRemote)ref;
		} 
		catch (NamingException ex) 
		{	ex.printStackTrace();
			System.exit(1);
		}

		boolean continua = true;

		do
		{	long idCliente = Console.
				readInt('\n' + "Informe o n�mero do cliente(1, 2 ou 3): ");

			try
			{	carrinhoEJBRemote.designarCliente(idCliente);
				System.out.println('\n' + "Cliente designado com sucesso.");
				continua = false;
			}
			catch(AplicacaoException e)
			{	System.out.println('\n' + e.getMessage());
			}
		}
		while(continua);

		continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voc� deseja fazer?");
			System.out.println('\n' + "1. Acescentar um produto ao carrinho de compras");
			System.out.println("2. Calcular pre�o total do carrinho");
			System.out.println("3. Exibir o carrinho de compras");
			System.out.println("4. Fechar compra");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um n�mero entre 1 e 5:");			

			
			switch (opcao)
			{	case 1:

					int idProduto = Console.readInt('\n' + 
						"Informe o n�mero do produto (1, 2 ou 3): ");
									
					int qtd = Console.readInt('\n' + 
						"Informe a quantidade desejada: ");

					try
					{	carrinhoEJBRemote.adicionarProduto(idProduto, qtd);
						System.out.println('\n' + 
							"Produto adicionado ao carrinho com sucesso");
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
					}
										
					break;

				case 2:
					double precoTotal = carrinhoEJBRemote.calcularPrecoTotal();
									
					System.out.println('\n' + "Pre�o total: " + precoTotal);

					break;

				case 3:
					try 
					{	Pedido umPedido = carrinhoEJBRemote.recuperarCarrinho();

						System.out.println('\n' + "Nome do cliente: " + umPedido.getCliente().getNome());
						System.out.println("Data de emiss�o do pedido: " + umPedido.getDataEmissao());					
						
						for (ItemDePedido ip : umPedido.getItensDePedidos())
						{	
							System.out.println('\n' + "      Produto: " + ip.getProduto().getNome());
							System.out.println("      Pre�o: " + ip.getProduto().getPreco());
							System.out.println("      Qtd: " + ip.getQtd());
						}
						System.out.println('\n' + "Pre�o total: " + umPedido.getPrecoTotal());					
					} 
					catch (EstadoConversacionalIncompleto e) 
					{	System.out.println('\n' + "Seu carrinho de compras n�o est� completo.");
						System.out.println("N�o � poss�vel fechar a compra.");
					}
									
					break;

				case 4:

					String numCartao = Console.readLine('\n' + 
						"Informe o n�mero de seu cart�o de cr�dito: ");
					
					try 
					{	PedidoDO pedidoDO = carrinhoEJBRemote.fecharCompra(numCartao);
					
						System.out.println('\n' + "N�mero do pedido: " + pedidoDO.getIdPedido());
						System.out.println("Nome do cliente: " + pedidoDO.getNomeCliente());
						System.out.println("Pre�o total do carrinho: " + pedidoDO.getPrecoTotal());
						
						continua = false;
					} 
					catch (EstadoConversacionalIncompleto e) 
					{	System.out.println('\n' + "Seu carrinho de compras n�o est� completo.");
						System.out.println("N�o � poss�vel fechar a compra.");
					}

					break;

				case 5:

					continua = false;
					break;

				default:
					System.out.println('\n' + "Op��o inv�lida!");						
			}
		}		
	}
}
