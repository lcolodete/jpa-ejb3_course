package ex04.excecao;

public class EstadoConversacionalIncompleto 
	extends Exception
{	
	private static final long serialVersionUID = 1L;

	public EstadoConversacionalIncompleto ()
	{	super ();
	}
   
	public EstadoConversacionalIncompleto (String msg)
	{	super (msg);
	}  
}
