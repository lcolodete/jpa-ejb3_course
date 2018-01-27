package exercicio22.cliente;

import java.util.List;

import exercicio22.dao.DaoGenerico;

public interface ClienteDAO extends DaoGenerico<Cliente, Long>
{   List<Cliente> recuperaListaDeClientes();
}
