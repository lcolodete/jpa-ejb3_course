package exercicio14.produto;

import java.util.List;

import exercicio14.lance.Lance;
import exercicio14.util.ObjetoNaoEncontradoException;

public interface ProdutoDAO
{	
	public long inclui(Produto umProduto); 
	public void altera(Produto umProduto)
		throws ObjetoNaoEncontradoException; 
	public void exclui(Produto umProduto) 
		throws ObjetoNaoEncontradoException; 
	public Produto recuperaUmProduto(long numero) 
		throws ObjetoNaoEncontradoException; 
	public Produto recuperaUmProdutoComLock(long numero) 
		throws ObjetoNaoEncontradoException; 
	public Lance recuperaUltimoLance(Produto produto); 
	public Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException; 
	public List<Produto> recuperaProdutosELances();
}