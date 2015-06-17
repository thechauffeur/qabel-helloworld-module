import de.qabel.ackack.event.EventEmitter;
import de.qabel.core.config.*;
import de.qabel.core.crypto.QblECKeyPair;
import de.qabel.core.drop.DropActor;
import de.qabel.core.drop.DropURL;
import org.junit.Before;
import org.junit.Test;

import de.qabel.core.module.ModuleManager;
import de.qabel.helloworld.QblHelloWorldModule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

public class QblHelloWorldModuleTest {
	private static final String BOB_DROP_URL = "http://localhost:6000/123456789012345678901234567890123456789012b";
	private ModuleManager moduleManager;

	@Before
	public void setUp() throws Exception {
		Persistence.setPassword("qabel".toCharArray());
		moduleManager = new ModuleManager();

		Collection<DropURL> bobDropURLs = new ArrayList<>();
		bobDropURLs.add(new DropURL(BOB_DROP_URL));

		Identity alice = new Identity("Alice", null, new QblECKeyPair());
		Identity bob = new Identity("Bob", bobDropURLs, new QblECKeyPair());

		Identities identities = new Identities();
		identities.put(alice);
		moduleManager.getResourceActor().writeIdentities(identities.getIdentities().toArray(new Identity[0]));

		Contact bobAsContactForAlice = new Contact(alice, bobDropURLs, bob.getEcPublicKey());

		Contacts contacts = new Contacts();
		contacts.put(bobAsContactForAlice);
		moduleManager.getResourceActor().writeContacts(contacts.getContacts().toArray(new Contact[0]));

		DropActor dropActor = new DropActor(EventEmitter.getDefault());
		Thread dropActorThread = new Thread(dropActor);
		dropActor.setInterval(500);
		dropActorThread.start();
	}

	@Test
	public void helloWorldModuleTest() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
		moduleManager.startModule(QblHelloWorldModule.class);
		moduleManager.shutdown();
	}
}
