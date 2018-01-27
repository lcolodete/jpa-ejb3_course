package exercicio30.cartao;

import java.util.List;

import exercicio30.dao.DaoGenerico;

public interface CartaoDAO extends DaoGenerico<Cartao, Long>
{   List<Cartao> recuperaListaDeCartoes();
}