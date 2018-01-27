package exercicio30.clienteConta;

import java.util.List;

import exercicio30.dao.DaoGenerico;
import exercicio30.util.ObjetoNaoEncontradoException;

public interface ClienteContaDAO extends DaoGenerico<ClienteConta, IdClienteConta>
{	List<ClienteConta> recuperaListaDeClienteConta();

	ClienteConta recuperaUmClienteContaComCartoes(long idCliente, long idConta)
		throws ObjetoNaoEncontradoException;
}
