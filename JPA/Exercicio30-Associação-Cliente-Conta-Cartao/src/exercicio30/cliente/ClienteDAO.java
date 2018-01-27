package exercicio30.cliente;

import java.util.List;

import exercicio30.dao.DaoGenerico;

public interface ClienteDAO extends DaoGenerico<Cliente, Long>
{   List<Cliente> recuperaListaDeClientes();
}