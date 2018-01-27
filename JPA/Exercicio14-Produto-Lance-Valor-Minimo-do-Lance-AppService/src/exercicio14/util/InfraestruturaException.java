package exercicio14.util;

public class InfraestruturaException extends RuntimeException
{	
	private final static long serialVersionUID = 1;
	
	public InfraestruturaException(Exception e)
	{	super(e);
	}
}	