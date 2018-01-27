package exercicio21.util;

public class InfraestruturaException extends RuntimeException
{	
	private final static long serialVersionUID = 1L;
	
	public InfraestruturaException(Exception e)
	{	super(e);
	}
}	