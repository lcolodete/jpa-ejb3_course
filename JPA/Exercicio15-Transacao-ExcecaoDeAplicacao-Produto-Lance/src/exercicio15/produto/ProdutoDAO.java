package exercicio15.produto;

import java.util.List;

import exercicio15.lance.Lance;
import exercicio15.util.ObjetoNaoEncontradoException;

public interface ProdutoDAO
{	
	public long inclui(Produto umProduto); 
	public void altera(Produto umProduto) 
		throws ObjetoNaoEncontradoException; 
	public void exclui(Produto umProduto) 
		throws ObjetoNaoEncontradoException; 
	public Produto recuperaUmProduto(long numero); 
	public List<Produto> recuperaProdutos(); 
	public Produto recuperaUmProdutoELances(long numero); 
	public List<Produto> recuperaProdutosELances();
	public Lance recuperaUltimoLance(Produto produto); 
}