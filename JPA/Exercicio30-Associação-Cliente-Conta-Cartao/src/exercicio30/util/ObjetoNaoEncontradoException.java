package exercicio30.util;

public class ObjetoNaoEncontradoException extends Exception
{	
	private final static long serialVersionUID = 1;
	
	public ObjetoNaoEncontradoException()
	{
	}

	public ObjetoNaoEncontradoException(String msg)
	{	super(msg);
	}
}	