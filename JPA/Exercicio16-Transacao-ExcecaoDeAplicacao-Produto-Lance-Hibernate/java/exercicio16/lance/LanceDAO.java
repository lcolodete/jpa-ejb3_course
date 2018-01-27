package exercicio16.lance;

import java.util.List;

import exercicio16.util.ObjetoNaoEncontradoException;

public interface LanceDAO
{	
	public long inclui(Lance umLance);
	public void altera(Lance umLance) 
		throws ObjetoNaoEncontradoException; 
	public void exclui(Lance umLance) 
		throws ObjetoNaoEncontradoException; 
	public Lance recuperaUmLance(long numero) 
		throws ObjetoNaoEncontradoException; 
	public List<Lance> recuperaLances(); 
}
