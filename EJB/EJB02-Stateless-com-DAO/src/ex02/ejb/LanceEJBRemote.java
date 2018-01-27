package ex02.ejb;

import java.util.List;

import javax.ejb.Remote;

import ex02.dominio.Lance;
import ex02.dominio.Produto;
import ex02.excecao.AplicacaoException;

@Remote
public interface LanceEJBRemote
{	
	public long inclui(double valor, String dtCriacao, Produto umProduto)  
		throws AplicacaoException;
	public void exclui(Lance umLance);
	public Lance recuperaUmLance(long numero)	
		throws AplicacaoException;
	public List<Lance> recuperaLances();
}