package ex10.ejb;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;

import org.jboss.annotation.ejb.RemoteBinding;

@Stateless
@RemoteBinding(jndiBinding="exercicio10.AgendaEJBBean/remote")
public class AgendaEJBBean implements AgendaEJBRemote
{	
==>

    public String agendarDesagendar(String info, Date umaData) 
    {	
        for (Object obj : timerService.getTimers()) 
        {	javax.ejb.Timer timer = (javax.ejb.Timer)obj;
            String agendado = (String)timer.getInfo();
==>         if (agendado.equals(info)) 
            {	timer.cancel();
            	return "desagendado";
            }
        }

==>    	timerService.createTimer(umaData, 5000, info);

//      Create an interval timer whose first expiration occurs at a given
//      point  in  time  and  whose  subsequent expirations occur after a 
//      specified interval.

// ==> 	timerService.createTimer(Date umaData, Serializable info);

//    	Create  a  single-action  timer  that expires at a given point in 
//      time.

// ==>  timerService.createTimer(long numero1, long numero2, Serializable info);

//      Create  an  interval  timer whose first expiration occurs after a 
//      specified duration, and whose subsequent expirations occur after 
//      a specified interval.

    	return "agendado";
    }

    @Timeout
    public void chegouAHora(javax.ejb.Timer timer) 
    {	System.out.println("Evento " + timer.getInfo() + " executado.");
    }
}
