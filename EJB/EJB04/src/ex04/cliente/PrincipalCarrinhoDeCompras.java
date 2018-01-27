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
				readInt('\n' + "Informe o número do cliente(1, 2 ou 3): ");

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
		{	System.out.println('\n' + "O que você deseja fazer?");
			System.out.println('\n' + "1. Acescentar um produto ao carrinho de compras");
			System.out.println("2. Calcular preço total do carrinho");
			System.out.println("3. Exibir o carrinho de compras");
			System.out.println("4. Fechar compra");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um número entre 1 e 5:");			

			
			switch (opcao)
			{	case 1:

					int idProduto = Console.readInt('\n' + 
						"Informe o número do produto (1, 2 ou 3): ");
									
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
									
					System.out.println('\n' + "Preço total: " + precoTotal);

					break;

				case 3:
					try 
					{	Pedido umPedido = carrinhoEJBRemote.recuperarCarrinho();

						System.out.println('\n' + "Nome do cliente: " + umPedido.getCliente().getNome());
						System.out.println("Data de emissão do pedido: " + umPedido.getDataEmissao());					
						
						for (ItemDePedido ip : umPedido.getItensDePedidos())
						{	
							System.out.println('\n' + "      Produto: " + ip.getProduto().getNome());
							System.out.println("      Preço: " + ip.getProduto().getPreco());
							System.out.println("      Qtd: " + ip.getQtd());
						}
						System.out.println('\n' + "Preço total: " + umPedido.getPrecoTotal());					
					} 
					catch (EstadoConversacionalIncompleto e) 
					{	System.out.println('\n' + "Seu carrinho de compras não está completo.");
						System.out.println("Não é possível fechar a compra.");
					}
									
					break;

				case 4:

					String numCartao = Console.readLine('\n' + 
						"Informe o número de seu cartão de crédito: ");
					
					try 
					{	PedidoDO pedidoDO = carrinhoEJBRemote.fecharCompra(numCartao);
					
						System.out.println('\n' + "Número do pedido: " + pedidoDO.getIdPedido());
						System.out.println("Nome do cliente: " + pedidoDO.getNomeCliente());
						System.out.println("Preço total do carrinho: " + pedidoDO.getPrecoTotal());
						
						continua = false;
					} 
					catch (EstadoConversacionalIncompleto e) 
					{	System.out.println('\n' + "Seu carrinho de compras não está completo.");
						System.out.println("Não é possível fechar a compra.");
					}

					break;

				case 5:

					continua = false;
					break;

				default:
					System.out.println('\n' + "Opção inválida!");						
			}
		}		
	}
}
