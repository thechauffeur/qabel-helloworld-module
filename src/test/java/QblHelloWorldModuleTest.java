import de.qabel.ackack.event.EventEmitter;
import de.qabel.core.config.Persistence;
import de.qabel.core.drop.DropActor;
import org.junit.Before;
import org.junit.Test;

import de.qabel.core.module.ModuleManager;
import de.qabel.helloworld.QblHelloWorldModule;

import java.lang.reflect.InvocationTargetException;

public class QblHelloWorldModuleTest {
	@Before
	public void setUp() throws Exception {
		Persistence.setPassword("qabel".toCharArray());

		DropActor dropActor = new DropActor(EventEmitter.getDefault());
		Thread dropActorThread = new Thread(dropActor);
		dropActor.setInterval(500);
		dropActorThread.start();
	}

	@Test
	public void test() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
		ModuleManager mm = new ModuleManager();
		mm.startModule(QblHelloWorldModule.class);
		mm.shutdown();
	}

}
