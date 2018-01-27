package exercicio27.util;

import exercicio27.controleTransacao.ExcecaoDeAplicacao;

@ExcecaoDeAplicacao(rollback=true)
public class TransacaoException extends Exception
{	
	private final static long serialVersionUID = 1L;
	
	public TransacaoException()
	{
	}

	public TransacaoException(String msg)
	{	super(msg);
	}

	public TransacaoException(Exception e)
	{	super(e);
	}
}	