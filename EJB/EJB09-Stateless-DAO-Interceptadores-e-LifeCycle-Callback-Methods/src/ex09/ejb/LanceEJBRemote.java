package ex09.ejb;

import java.util.List;

import javax.ejb.Remote;

import ex09.dominio.Lance;
import ex09.dominio.Produto;
import ex09.excecao.AplicacaoException;

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