package ex09.excecao;

public class InfraestruturaException extends RuntimeException
{	
	private final static long serialVersionUID = 1;
	
	public InfraestruturaException(Exception e)
	{	super(e);
	}
}	