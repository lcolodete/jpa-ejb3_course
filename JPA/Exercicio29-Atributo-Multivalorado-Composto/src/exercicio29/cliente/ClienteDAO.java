package exercicio29.cliente;

import java.util.List;

import exercicio29.dao.DaoGenerico;
import exercicio29.util.ObjetoNaoEncontradoException;

public interface ClienteDAO extends DaoGenerico<Cliente, Long>
{   Cliente recuperaUmClienteETelefones(long id)
		throws ObjetoNaoEncontradoException;
	
	List<Cliente> recuperaListaDeClientes();

	List<Cliente> recuperaListaDeClientesETelefones();
}
