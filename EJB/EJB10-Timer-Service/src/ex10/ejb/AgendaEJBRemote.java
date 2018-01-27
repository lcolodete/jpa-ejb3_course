package ex10.ejb;

import javax.ejb.Remote;
import java.util.Date;

@Remote
public interface AgendaEJBRemote
{	public String agendarDesagendar(String info, Date umaData);
}
