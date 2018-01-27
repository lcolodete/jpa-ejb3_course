package exercicio28.util;

import java.util.List;

import exercicio28.controleTransacao.ExcecaoDeAplicacao;

@ExcecaoDeAplicacao
public class AplicacaoException extends Exception
{	
	private final static long serialVersionUID = 1L;
	
	private List<String> mensagens;
	
	public AplicacaoException(String msg)
	{	super(msg);
	}

	public AplicacaoException(List<String> mensagens)
	{	this.mensagens = mensagens;
	}

	public List<String> getMensagens()
	{	return mensagens;
	}
}	