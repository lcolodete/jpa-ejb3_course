package exercicio19.produto;

import java.util.List;
import java.util.Set;

import exercicio19.dao.DaoGenerico;
import exercicio19.lance.Lance;
import exercicio19.util.ObjetoNaoEncontradoException;

public interface ProdutoDAO extends DaoGenerico<Produto, Long>
{   Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException;

	List<Produto> recuperaListaDeProdutos();
	
	Set<Produto> recuperaConjuntoDeProdutosELances();
	
	Lance recuperaUltimoLance(Produto produto)
		throws ObjetoNaoEncontradoException;
	
	Produto recuperaUmProdutoELanceVencedor(long idProduto)
		throws ObjetoNaoEncontradoException;
	        
	Produto recuperaUmProdutoComDeterminadoLanceVencedor(long idLance)
		throws ObjetoNaoEncontradoException;
	
}
