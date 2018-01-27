package exercicio26.produto;

import java.util.List;
import java.util.Set;

import exercicio26.dao.DaoGenerico;
import exercicio26.lance.Lance;
import exercicio26.util.ObjetoNaoEncontradoException;

public interface ProdutoDAO extends DaoGenerico<Produto, Long>
{   Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException;

	List<Produto> recuperaListaDeProdutos();
	
	Set<Produto> recuperaConjuntoDeProdutosELances();
	
	Lance recuperaUltimoLance(Produto produto)
		throws ObjetoNaoEncontradoException;
}
