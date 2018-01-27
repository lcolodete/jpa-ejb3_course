package exercicio15.lance;

import java.util.List;

import exercicio15.util.ObjetoNaoEncontradoException;

public interface LanceDAO
{	
	public long inclui(Lance umLance);
	public void altera(Lance umLance) 
		throws ObjetoNaoEncontradoException; 
	public void exclui(Lance umLance) 
		throws ObjetoNaoEncontradoException; 
	public Lance recuperaUmLance(long numero); 
	public List<Lance> recuperaLances(); 
}
