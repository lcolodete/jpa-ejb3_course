package exercicio05;

import java.util.List;

public interface ClienteDAO
{	public long inclui(Cliente umCliente);
	public void altera(Cliente umCliente);
	public void exclui(Cliente umCliente);
	public Cliente recuperaUmCliente(long numero);
	public List<Cliente> recuperaClientes();
}