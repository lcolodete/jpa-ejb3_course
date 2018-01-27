package exercicio16.produto;

import java.util.List;

import exercicio16.lance.Lance;
import exercicio16.util.ObjetoNaoEncontradoException;

public interface ProdutoDAO
{	
	public long inclui(Produto umProduto); 
	public void altera(Produto umProduto) 
		throws ObjetoNaoEncontradoException; 
	public void exclui(Produto umProduto) 
		throws ObjetoNaoEncontradoException; 
	public Produto recuperaUmProduto(long numero)
		throws ObjetoNaoEncontradoException; 
	public List<Produto> recuperaProdutos(); 
	public Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException; 
	public List<Produto> recuperaProdutosELances();
	public Lance recuperaUltimoLance(Produto produto); 
}