package exercicio17.produto;

import java.util.List;
import java.util.Set;

import exercicio17.dao.DaoGenerico;
import exercicio17.lance.Lance;
import exercicio17.util.ObjetoNaoEncontradoException;

public interface ProdutoDAO extends DaoGenerico<Produto, Long>
{   Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException;

	List<Produto> recuperaListaDeProdutos();
	
	List<Produto> recuperaListaDeProdutosELances();

	Set<Produto> recuperaConjuntoDeProdutosELances();
	
	Lance recuperaUltimoLance(Produto produto)
		throws ObjetoNaoEncontradoException;
}
