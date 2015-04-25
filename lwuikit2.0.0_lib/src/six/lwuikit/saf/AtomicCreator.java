package six.lwuikit.saf;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCreator {
	
	private static AtomicInteger creator = new AtomicInteger(0);
	
	public static int getAndIncrement(){
		return creator.getAndIncrement();
	}
}
