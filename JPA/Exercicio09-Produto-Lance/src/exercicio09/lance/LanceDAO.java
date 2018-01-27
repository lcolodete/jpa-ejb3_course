package exercicio09.lance;

import java.util.List;

import exercicio09.util.AplicacaoException;

public interface LanceDAO
{	
	public long inclui(Lance umLance); 
	public void altera(Lance umLance) 
		throws AplicacaoException; 
	public void exclui(Lance umLance) 
		throws AplicacaoException; 
	public Lance recuperaUmLance(long numero) 
		throws AplicacaoException;
	public List<Lance> recuperaLances();
}
