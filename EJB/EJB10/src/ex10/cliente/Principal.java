package ex10.cliente;

import java.util.Date;

import javax.naming.Context;
import javax.naming.NamingException;

import corejava.Console;
import ex10.ejb.AgendaEJBRemote;

public class Principal
{	public static void main (String[] args) 
	{	
	    Context jndiContext;
	    AgendaEJBRemote agendaEJBRemote = null;
		try 
		{	jndiContext = new javax.naming.InitialContext();
		    Object ref = jndiContext.lookup("exercicio10.AgendaEJBBean/remote");
		    agendaEJBRemote = (AgendaEJBRemote)ref;
		} 
		catch (NamingException ex) 
		{	ex.printStackTrace();
			System.exit(1);
		}

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
			System.out.println('\n' + "1. Agendar / Desagendar um evento");
			System.out.println("2. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um número entre 1 ou 2:");
					
			switch (opcao)
			{	
				case 1:
				{
					String info = Console.readLine('\n' + "Informe o evento: ");
						
					String resultado = agendaEJBRemote.agendarDesagendar(info, new Date(System.currentTimeMillis() + 5000));
					
					System.out.println('\n' + "Evento " + resultado + " com sucesso!");	

					break;
				}

				case 2:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");
			}
		}		
	}
}
