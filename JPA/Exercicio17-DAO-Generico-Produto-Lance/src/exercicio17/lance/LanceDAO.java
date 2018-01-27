package exercicio17.lance;

import java.util.List;

import exercicio17.dao.DaoGenerico;

public interface LanceDAO extends DaoGenerico<Lance, Long>
{
//    Lance recuperaUmLanceComProduto(long numero) 
//    	throws ObjetoNaoEncontradoException;

    List<Lance> recuperaListaDeLances();
}
