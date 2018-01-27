package ex05.cliente;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class ClienteJms_PagamentoConsumer
   implements javax.jms.MessageListener
{
   
	public static void main(String [] args) throws Exception
	{	new ClienteJms_PagamentoConsumer();
      	while(true) { Thread.sleep (10000); }      
	}
   
	public ClienteJms_PagamentoConsumer () throws Exception
	{	Context jndiContext = new InitialContext ();
      
		// O  código  abaixo  recupera   uma   referência   para   um   objeto 
		// ConnectionFactory após consulta ao naming service do JBoss.

		// JNDI também permite que todas as propriedades  tenham  seus valores
		// designados  em  um   arquivo  denominado   jndi.properties  que   é
		// encontrado dinamicamente em tempo de execução.
      	ConnectionFactory factory = (ConnectionFactory)
      		jndiContext.lookup ("ConnectionFactory");
      
      	Connection connect = factory.createConnection();      

      	Session session = connect.createSession(false,Session.AUTO_ACKNOWLEDGE);      

      	Queue queue = (Queue)
  			jndiContext.lookup ("queue/processaPagamentoQueue");
  
      	MessageConsumer receiver = session.createConsumer(queue);      
      	receiver.setMessageListener(this);
      
      	System.out.println ("Escutando por mensagens de processaPagamentoQueue...");
      	
      	// Inicia a entrega de mensagens de entrada desta conexão.
      	connect.start ();
	}
   
	public void onMessage (Message message)
	{	try
      	{	ObjectMessage objMsg = (ObjectMessage)message;
      		Serializable obj = (Serializable)objMsg.getObject ();
      		System.out.println ("********************************");
      		System.out.println (obj);
      		System.out.println ("********************************");
      	}
      	catch (JMSException e)
      	{	e.printStackTrace ();
      	}
	}
}
