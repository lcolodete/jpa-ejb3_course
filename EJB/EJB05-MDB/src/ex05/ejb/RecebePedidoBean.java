package ex05.ejb;
     
import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.NamingException;

import ex05.dataObject.PedidoDO;
import ex05.excecao.AplicacaoException;
import ex05.excecao.EstadoConversacionalIncompleto;

// MDBs  s�o  identificados  pela  anota��o  @MessageDriven  ou  podem  estar
// definidos  em  um  arquivo  ejb-jar.xml.  O atributo activationConfig() da
// anota��o @MessageDriven  possui  a  forma de um array de anota��es do tipo
// @ActivationConfigProperty. Estas anota��es s�o simplesmente um conjunto de
// pares do tipo nome/valor que descrevem a configura��o do MDB. 

// Os nomes de propriedades e valores utilizados no atributo activationConfig
// para descrever o servi�o de mensagem variam do tipo de servi�o de mensagem
// que  estiver  sendo  utilizado,  mas EJB 3.0 define um conjunto de fixo de 
// propriedades    para    MDBs   JMS:    acknowledgeMode,   messageSelector,            
// destinationType e subscriptionDurability.

// MessageSelectors
// ================

// MessageSelectors  permitem  a um MDB ser mais seletivo sobre as  mensagens
// que ele pode receber de um topic ou queue.  
// 
// Exemplo:
//   @ActivationConfigProperty(propertyName="messageSelector",
//                             propertyValue="MessageFormat = 'Versao 3.4'")
//
// O String 'Versao 3.4' poder�  ser  utilizado  pelo  MDB  para  identificar         
// mensagens do tipo MapMessage. Ao adicionar um MessageFormat a  cada pedido
// nos permite criar um MDB capaz de processar diferentes tipos de  mensagens 
// de pedidos.

// J� o JMS producer deveria enviar a mensagem assim:
//   Message message = session.creteMapMessage();
//   message.setStringProperty("MessageFormat", "Versao 3.4");
//   ...
//   sender.send(message);
//

// AcknowledgeMode
// ===============

// Um  JMS  acknowledgment  significa  que o cliente JMS deve notificar o JMS            
// producer quando a mensgem � recebida.

// Exemplo:
//  @ActivationConfigProperty(propertyName="acknowledgeMode",
//                            propertyValue="Auto-acknowledge")

// Dois    valores    podem    ser    especificados   para   acknowledgeMode: 
// Auto-acknowledge e Dups-ok-acknowledge. Auto-acknowledge diz ao  container
// que ele deveria enviar um aviso de recebimento da mensagem ao provedor JMS
// logo   ap�s   a   mensagem   ser   entregue   �   inst�ncia   MDB.   J�  o 
// Dups-ok-acknowledge deve ser evitado pois faz com que uma  mensagem  possa
// ser recebida  mais de uma  vez.  Teoricamente o uso de Dups-ok-acknowledge
// pode otimizar o uso da rede. Na pr�tica o  overhead de Auto-acknowledge  �   
// t�o pequeno que deve ser evitado.

// SubscriptionDurability
// ======================

// Quando a subscri��o � dur�vel, se o servidor EJB sofrer uma falha parcial,
// shut down, ou se desconectar do provedor JMS, as  mensagens que ele  teria
// recebido n�o s�o perdidas. Os desenvolvedores  utilizam NonDurable  quando
// algumas mensagens podem ser perdidas caso ocorra uma desconex�o.

// Exemplo:
// @ActivationConfigProperty(propertyName="subscriptionDurability",
//                           propertyValue="Durable")

// Quando  o  destino  de  uma  mensagem  � uma queue, como no caso abaixo, a 
// durabilidade  n�o  �  uma  quest�o  relevante  em  fun��o  da natureza dos 
// systemas de mensagens baseados em queues. Nestes sistemas uma mensagem  s�
// sai da fila ap�s ser entregue a um dos consumidores cadastrados.

@MessageDriven(activationConfig={
   @ActivationConfigProperty(propertyName="destinationType",
                             propertyValue="javax.jms.Queue"),
   @ActivationConfigProperty(propertyName="destination", 
                             propertyValue="queue/processaPedidoQueue")})

// MDBs  geralmente  implementam  a  interface  MessageListener  que define o 
// m�todo  onMessage().  Este  m�todo  processa a  mensagem JMS recebida pelo 
// bean.
public class RecebePedidoBean implements javax.jms.MessageListener 
{	
	@EJB private CarrinhoEJBLocal carrinho;

	// Para  enviar  uma  mensagem  JMS  precisamos de uma  conex�o com o JMS
	// provider e de um endere�o de destino para a mensagem.
   	@Resource(mappedName="java:/JmsXA")
   	private ConnectionFactory connectionFactory;

   	// Em JMS um objeto Message �  composto de duas partes: um cabe�alho e um
   	// corpo. O cabe�alho � composto de informa��o de entrega e de metadados,
   	// e o corpo  cont�m os dados da  aplica��o.  Estes dados  podem estar em
   	// v�rios  formatos:  texto puro,  objetos serializados, strems de bytes, 
   	// etc. A API JMS prov� m�todos para entregar  mensagens de  v�rios tipos
   	// (TextMessage,  MapMessage,  ObjectMessage, e  outros, e prov�  m�todos
   	// para  entregar e receber mensagens de outras aplica��es.
   	public void onMessage(Message message) 
   	{	System.out.println("Mensagem recebida");
   		
   		MapMessage pedidoMsg = null; 
   		Queue queue = null;
   		long idCliente = 0;
		long idProduto = 0;
		int qtd = 0;
		String numCartao = "";
		
		try 
   		{
   			pedidoMsg = (MapMessage)message;
   			
   			// Aqui  estamos   recuperando  o  endere�o  de  destino  para  a
   			// mensagem. O endere�o de destino � identificado por  um  objeto
   			// Topic ou Queue.

   			// Em   JMS   as   mensagens   n�o   s�o  enviadas diretamente �s
   			// aplica��es. Elas s�o enviadas a topics ou  queues. Um topic  �
   			// an�logo  a  uma  lista  de  e-mails  ou  newsgroup.   Qualquer
   			// aplica��o  com  as  credenciais  necess�rias  podem  receber e
   			// enviar  mensagens para um topic.  Um cliente  subscreve  a  um 
   			// topic. 
   			
   			// No tipo de mensagem "publish  and subscribe", um produtor pode
   			// enviar  uma  mensagem  para  muitos consumidores atrav�s de um 
   			// canal  virtual  denominado  "topic".  Os  consumidores   podem 
   			// subscrever a um topic. Qualquer mensagem endere�ada a um topic
   			// � entregue a todos os consumidores do topic.
   			
   			// J� o tipo de mensagem "point-to-point" permite a clientes  JMS
   			// enviarem e receberem mensagens s�ncronas e ass�ncronas atrav�s 
   			// de um canal virtual conhecido como queues. Uma queue pode  ter 
   			// v�rios recebedores, mas apenas um recebe cada mensagem. 
   			queue = (Queue)pedidoMsg.getJMSReplyTo();    
			
   			carrinho.criarNovoPedido();
   			
   			idCliente = pedidoMsg.getLong("ClienteID");
			carrinho.designarCliente(idCliente);
			
			int i = 1;
			while(pedidoMsg.itemExists("ProdutoID" + i))
			{	
				idProduto = pedidoMsg.getLong("ProdutoID" + i);
				qtd = pedidoMsg.getInt("Qtd" + i);
				carrinho.adicionarProduto(idProduto, qtd);
				i++;
			}
			
			numCartao = pedidoMsg.getString("NumCartao");
			PedidoDO pedidoDO = carrinho.fecharCompra(numCartao);
			comunicarCompra(queue, pedidoDO);
   		} 
   		catch(AplicacaoException e) 
   		{	try
   			{	comunicarCompra(queue, 
						converteParaString(pedidoMsg) + "\n\n" + 
						"Mensagem de erro = " + e.getMessage());
   			}
	   		catch(Exception ejb) 
	   		{	throw new EJBException(ejb);
	   		}
   		}
   		catch(EstadoConversacionalIncompleto e) 
   		{	try
			{	comunicarCompra(queue, 
						converteParaString(pedidoMsg) + "\n\n" + 
						"Mensagem de erro = " + e.getMessage());
			}
	   		catch(Exception ejb) 
	   		{	throw new EJBException(ejb);
	   		}
   		}
   		catch(Exception e) 
   		{	throw new EJBException(e);
   		}
   	}
    
   	public void comunicarCompra(Queue queue, Serializable resultado) 
   		throws JMSException, NamingException
   	{	
   		// Uma  vez  criada uma conex�o ela pode ser utilizada para criar uma
   		// sess�o.  Uma  sess�o  permite a voc�  agrupar as a��es de enviar e 
   		// receber  mensagens.  Utilizar  multiplas  sess�es  �  �til se voc� 
   		// gostaria de produzir e consumir  mensagens em threads  diferentes. 
   		// Um  objeto  session  n�o  pode  ser  acessado  por  v�rias threads 
   		// simultaneamente. 
   		
   		// A  thread que cria a session � geralmente a  thread que utiliza os 
   		// objetos MessageProducer e MessageConsumer. 
   		
   		Connection connect = connectionFactory.createConnection();

   		// createSession(boolean transacted, int acknowledgeMode)
   		
   		// De acordo com a especifica��o EJB, estes argumentos s�o  ignorados
   		// em tempo de execu��o pois o container EJB gerencia a transa��o e o
   		// acknowledgeMode. A especifica��o recomenda o uso de true para para
   		// transacted  e  0  para  acknowledgeMode.  Alguns  fabricantes   de 
   		// containers EJB ignoram estes valores, outros n�o.
   		
   		// Um acknowledgment JMS significa que o cliente  JMS notifica o  JMS 
   		// provider quando uma mensgem � recebida. Em EJB, � responsabilidade 
   		// do container do MDB enviar o acknowledgment quando ele recebe  uma
   		// mensagem. Ao acusar o recebimento de  uma mensagem o  container de 
   		// um MDB est� dizendo ao JMS provider  que a mensagem foi recebida e
   		// processada. Se o container n�o acusar  o recebimento  da mensagem,
   		// o JMS provider poder� receber a mensagem novamente o que ir� fazer
   		// com que um pedido seja cadastrado duas ou mais vezes. 
   		
   		// O comportamento default de  um container  EJB � enviar o  aviso de 
   		// recebimento de uma mensagem  ao JMS  provider  no contexto  de uma 
   		// transa��o do MDB. Se a transa��o no MDB for bem sucedida, o  aviso
   		// de  recebimento  da  mensagem  ser�  imediatamente  enviado ao JMS 
   		// provider.
   		
   		Session session = connect.createSession(true,0);
   		
   		// A  sess�o  �  utilizada  para criar um objeto MessageProducer, que
   		// envia mensagens para o  destino especificado  pelo objeto queue ou
   		// topic. 
   		MessageProducer sender = session.createProducer(queue);

   		ObjectMessage message = session.createObjectMessage();
   		message.setObject(resultado);
   		sender.send(message);
   		connect.close();
   	}

   	private String converteParaString(MapMessage pedidoMsg) throws JMSException
   	{
		StringBuffer resultado = new StringBuffer();
   		
   		long idCliente = pedidoMsg.getLong("ClienteID");
		resultado.append("Cliente = " + idCliente + '\n');
		
		int i = 1;
		while(pedidoMsg.itemExists("ProdutoID" + i))
		{	
			long idProduto = pedidoMsg.getLong("ProdutoID" + i);
			int qtd = pedidoMsg.getInt("Qtd" + i);
			resultado.append("   Produto = " + idProduto + '\n');
			resultado.append("   Qtd = " + qtd + "\n\n");
			i++;
		}
		
		String numCartao = pedidoMsg.getString("NumCartao");
		resultado.append("N�mero do cart�o = " + numCartao);

		return resultado.toString();
   	}
}
