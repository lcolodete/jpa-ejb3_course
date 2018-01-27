package exercicio28.cliente;

import java.util.List;

import exercicio28.dao.DaoGenerico;
import exercicio28.util.ObjetoNaoEncontradoException;

public interface ClienteDAO extends DaoGenerico<Cliente, Long>
{   Cliente recuperaUmClienteETelefones(long id)
		throws ObjetoNaoEncontradoException;
	
	List<Cliente> recuperaListaDeClientes();

	List<Cliente> recuperaListaDeClientesETelefones();
}
