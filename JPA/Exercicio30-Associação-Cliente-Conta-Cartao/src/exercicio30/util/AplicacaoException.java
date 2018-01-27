package exercicio30.util;

public class AplicacaoException extends Exception
{	
	private final static long serialVersionUID = 1;
	
	private int codigo;
	
	public AplicacaoException()
	{
	}

	public AplicacaoException(String msg)
	{	super(msg);
	}

	public AplicacaoException(int codigo, String msg)
	{	super(msg);
		this.codigo = codigo;
	}
	
	public AplicacaoException(Exception e)
	{	super(e);
	}
	
	public int getCodigoDeErro()
	{	return codigo;
	}
}	