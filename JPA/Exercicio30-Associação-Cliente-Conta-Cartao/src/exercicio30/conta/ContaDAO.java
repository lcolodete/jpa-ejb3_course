package exercicio30.conta;

import java.util.List;

import exercicio30.dao.DaoGenerico;

public interface ContaDAO extends DaoGenerico<Conta, Long>
{   List<Conta> recuperaListaDeContas();
}
