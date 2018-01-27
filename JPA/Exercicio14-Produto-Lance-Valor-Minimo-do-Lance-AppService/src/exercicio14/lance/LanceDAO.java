package exercicio14.lance;

import java.util.List;

import exercicio14.util.ObjetoNaoEncontradoException;

public interface LanceDAO
{	
	public long inclui(Lance umLance);
	public void exclui(Lance umLance) 
		throws ObjetoNaoEncontradoException; 
	public Lance recuperaUmLance(long numero) 
		throws ObjetoNaoEncontradoException; 
	public List<Lance> recuperaLances();
}
