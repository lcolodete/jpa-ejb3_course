package exercicio08;

import java.util.List;

public interface ClienteDAO
{	public long inclui(Cliente umCliente);
	public boolean altera(Cliente umCliente); 
	public boolean exclui(long numero); 
	public Cliente recuperaUmCliente(long numero) 
		throws AplicacaoException;
	public List<Cliente> recuperaClientes();
}