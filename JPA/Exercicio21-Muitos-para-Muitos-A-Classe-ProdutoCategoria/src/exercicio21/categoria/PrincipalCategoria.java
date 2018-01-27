package exercicio21.categoria;

import static java.lang.System.out;

import java.util.List;
import java.util.Set;

import corejava.Console;
import exercicio21.controleTransacao.FabricaDeAppService;
import exercicio21.prodcat.ProdutoCategoria;
import exercicio21.util.AplicacaoException;

public class PrincipalCategoria
{	public static void main (String[] args) 
	{	
		String nome;

		Categoria umaCategoria;

		CategoriaAppService categoriaAppService = null;
		
		try 
		{	categoriaAppService = (CategoriaAppService) FabricaDeAppService
				.getAppService(CategoriaAppService.class);
		} 
		catch (Exception e) 
		{	e.printStackTrace();
			System.exit(1);
		}

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
			System.out.println('\n' + "1. Cadastrar uma categoria");
			System.out.println("2. Remover uma categoria");
			System.out.println("3. Listar todas as categorias e produtos");
			System.out.println("4. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 4:");
					
			switch (opcao)
			{	case 1:
				{
					nome = Console.readLine('\n' + 
						"Informe o nome da categoria: ");
						
					umaCategoria = new Categoria(nome);
			
					long numero = categoriaAppService.inclui(umaCategoria);

					System.out.println('\n' + "Categoria numero " + 
						    numero + " incluida com sucesso!");	

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero da categoria que voce deseja remover: ");
									
					try
					{	umaCategoria = categoriaAppService.
											recuperaUmaCategoria(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
						
					System.out.println('\n' + 
						"Número = " + umaCategoria.getId() + 
						"    Nome = " + umaCategoria.getNome());
								
					String resp = Console.readLine('\n' + 
						"Confirma a remocao da categoria?");

					if(resp.equals("s"))
					{	try
						{	categoriaAppService.exclui (resposta);
							System.out.println('\n' + 
									"Categoria removida com sucesso!");
						}
						catch(AplicacaoException e)
						{	System.out.println('\n' + e.getMessage());
						}
					}
					else
					{	System.out.println('\n' + "Categoria nao removida.");
					}
					
					break;
				}

				case 3:
				{
					List<Categoria> categorias = categoriaAppService.
											recuperaListaDeCategoriasEProdutos();
						
					for (Categoria categoria : categorias)
					{	System.out.println('\n' + 
							"Categoria numero = "  + categoria.getId() + 
							"  Nome = "  + categoria.getNome());

						Set<ProdutoCategoria> produtoCategorias = categoria.getProdutoCategorias();
						
						for (ProdutoCategoria produtoCategoria : produtoCategorias)
						{	
							out.println(	
								'\n' + "      Produto número = "  + produtoCategoria.getProduto().getId() + 
									   "  Nome = "  + produtoCategoria.getProduto().getNome());
						}	
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
