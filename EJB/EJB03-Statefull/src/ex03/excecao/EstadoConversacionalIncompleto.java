package ex03.excecao;

public class EstadoConversacionalIncompleto 
	extends java.lang.Exception
{	
	private static final long serialVersionUID = 1L;

	public EstadoConversacionalIncompleto ()
	{	super ();
	}
   
	public EstadoConversacionalIncompleto (String msg)
	{	super (msg);
	}  
}
