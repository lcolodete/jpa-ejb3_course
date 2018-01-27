package exercicio21.dao;

import java.io.Serializable;

import exercicio21.util.ObjetoNaoEncontradoException;

/**
 * A interface GenericDao b�sica com os m�todos CRUD. Os m�todos
 * de busca s�o adicionados por heran�a de interface.
 *
 * Interfaces estendidas podem declarar m�todos que come�am  por 
 * busca...  recuperaCnjuntoDe...  ou recupera...  Estes m�todos
 * ir�o  executar buscas pr�-configuradas que s�o localizadas em 
 * fun��o do restante do nome dos m�todos.
 * 
 */
public interface DaoGenerico<T, PK extends Serializable>
{
    T inclui(T obj);

    T getPorId(PK id) throws ObjetoNaoEncontradoException;

    T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException;

    void altera(T obj);

    void exclui(T obj);
}
