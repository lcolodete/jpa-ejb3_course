package ex09.ejb;

import java.util.List;

import javax.ejb.Remote;

import ex09.dominio.Produto;
import ex09.excecao.AplicacaoException;

@Remote
public interface ProdutoEJBRemote
{	
	public long inclui(String nome, String descricao, 
	                   double lanceMinimo, String dtCadastro) 
		throws AplicacaoException;
	public boolean altera(Produto umProduto); 
	public boolean exclui(Produto umProduto) 
		throws AplicacaoException;
	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException;
	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException;
	public List<Produto> recuperaProdutosELances();
}