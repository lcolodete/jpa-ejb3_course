package ex06.excecao;

import java.util.List;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class AplicacaoException extends Exception
{	
	private final static long serialVersionUID = 1;
	
	private int codigo;
	private List<String> mensagens;
	
	public AplicacaoException()
	{
	}

	public AplicacaoException(String msg)
	{	super(msg);
	}

	public AplicacaoException(List<String> mensagens)
	{	this.mensagens = mensagens;
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

	public List<String> getMensagens()
	{	return mensagens;
	}
}	