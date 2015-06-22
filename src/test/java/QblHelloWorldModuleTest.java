import de.qabel.ackack.event.EventEmitter;
import de.qabel.core.config.*;
import de.qabel.core.crypto.QblECKeyPair;
import de.qabel.core.drop.DropActor;
import de.qabel.core.drop.DropURL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.qabel.core.module.ModuleManager;
import de.qabel.helloworld.QblHelloWorldModule;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

public class QblHelloWorldModuleTest {
	private static final String DB_NAME = "QblHelloWorldModuleTest.sqlite";
	private static final String BOB_DROP_URL = "http://localhost:6000/123456789012345678901234567890123456789012b";
	private ModuleManager moduleManager;

	@Before
	public void setUp() throws Exception {
		Persistence<String> persistence = new SQLitePersistence(DB_NAME, "qabel".toCharArray());
		ResourceActor resourceActor = new ResourceActor(persistence, EventEmitter.getDefault());
		moduleManager = new ModuleManager(EventEmitter.getDefault(), resourceActor);

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

		DropActor dropActor = new DropActor(resourceActor, EventEmitter.getDefault());
		Thread dropActorThread = new Thread(dropActor);
		dropActor.setInterval(500);
		dropActorThread.start();
	}

	@After
	public void tearDown() throws InterruptedException {
		File persistenceTestDB = new File(DB_NAME);
		if(persistenceTestDB.exists()) {
			persistenceTestDB.delete();
		}
	}

	@Test
	public void helloWorldModuleTest() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
		moduleManager.startModule(QblHelloWorldModule.class);
		moduleManager.shutdown();
	}
}
