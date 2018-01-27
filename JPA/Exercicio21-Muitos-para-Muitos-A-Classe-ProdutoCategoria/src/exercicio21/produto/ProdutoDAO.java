package exercicio21.produto;

import java.util.List;
import java.util.Set;

import exercicio21.dao.DaoGenerico;
import exercicio21.lance.Lance;
import exercicio21.util.ObjetoNaoEncontradoException;

public interface ProdutoDAO extends DaoGenerico<Produto, Long>
{   Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException;

	List<Produto> recuperaListaDeProdutos();
	
	Set<Produto> recuperaConjuntoDeProdutosELances();
	
	Lance recuperaUltimoLance(Produto produto)
		throws ObjetoNaoEncontradoException;
	
	Produto recuperaUmProdutoELanceVencedor(long id)
		throws ObjetoNaoEncontradoException;

	Produto recuperaUmProdutoECategorias(long id)
		throws ObjetoNaoEncontradoException;

	List<Produto> recuperaListaDeProdutosCategorias();
}
