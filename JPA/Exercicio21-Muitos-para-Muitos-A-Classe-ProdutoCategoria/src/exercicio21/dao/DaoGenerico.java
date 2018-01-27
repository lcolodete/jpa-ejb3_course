package exercicio21.dao;

import java.io.Serializable;

import exercicio21.util.ObjetoNaoEncontradoException;

/**
 * A interface GenericDao básica com os métodos CRUD. Os métodos
 * de busca são adicionados por herança de interface.
 *
 * Interfaces estendidas podem declarar métodos que começam  por 
 * busca...  recuperaCnjuntoDe...  ou recupera...  Estes métodos
 * irão  executar buscas pré-configuradas que são localizadas em 
 * função do restante do nome dos métodos.
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
