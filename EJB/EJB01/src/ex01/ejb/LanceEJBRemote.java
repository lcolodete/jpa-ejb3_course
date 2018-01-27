package ex01.ejb;

import java.util.List;

import javax.ejb.Remote;

import ex01.dominio.Lance;
import ex01.excecao.AplicacaoException;

@Remote
public interface LanceEJBRemote
{	public long inclui(Lance umLance); 
	public boolean altera(Lance umLance);
	public boolean exclui(Lance umLance);
	public Lance recuperaUmLance(long numero) 
		throws AplicacaoException;
	public List<Lance> recuperaLances();
}
