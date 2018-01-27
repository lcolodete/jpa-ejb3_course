package exercicio21.lance;

import java.util.List;

import exercicio21.dao.DaoGenerico;
import exercicio21.util.ObjetoNaoEncontradoException;

public interface LanceDAO extends DaoGenerico<Lance, Long>
{
    Lance recuperaUmLanceComProduto(long numero) 
    	throws ObjetoNaoEncontradoException;

    List<Lance> recuperaListaDeLances();
}
