package de.qabel.helloworld;

import de.qabel.ackack.event.EventEmitter;
import de.qabel.core.config.Contact;
import de.qabel.core.config.Entity;
import de.qabel.core.config.Identity;
import de.qabel.core.crypto.QblECKeyPair;
import de.qabel.core.drop.DropActor;
import de.qabel.core.drop.DropMessage;
import de.qabel.core.drop.DropURL;
import de.qabel.core.drop.ModelObject;
import de.qabel.core.exceptions.QblDropInvalidURL;
import de.qabel.core.module.Module;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class QblHelloWorldModule extends Module {
	protected QblHelloWorldModule() {
		super();
	}

	class HelloWorldObject extends ModelObject {
		public HelloWorldObject() { }
		private String str;

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}
	}

	@Override
	public void init() {
	}

    /**
     * Send "Hello World" to all contacts.
     */
    private void sendMessages() {
        HelloWorldObject data = new HelloWorldObject();
        data.setStr("Hello World");

		Entity sender = null; // TODO
        DropMessage<HelloWorldObject> dm = new DropMessage<>(sender, data);

		/*
		 TODO: This is taken from MultiPartCryptoTest just to compile this example again.
		 TODO: A working example will be created after a refactorization of the ModuleManager
		 */
		try {
			QblECKeyPair alicesKey = new QblECKeyPair();
			Collection<DropURL> alicesDrops = new ArrayList<DropURL>();
			alicesDrops.add(new DropURL("http://localhost:6000/12345678901234567890123456789012345678alice"));
			Identity alice = new Identity("Alice", alicesDrops, alicesKey);

			QblECKeyPair bobsKey = new QblECKeyPair();
			Identity bob = new Identity("Bob", new ArrayList<DropURL>(), bobsKey);
			bob.addDrop(new DropURL("http://localhost:6000/1234567890123456789012345678901234567890bob"));

			Contact alicesContact = new Contact(alice, null, bobsKey.getPub());
			alicesContact.addDrop(new DropURL("http://localhost:6000/1234567890123456789012345678901234567890bob"));

			Contact bobsContact = new Contact(bob, null, alicesKey.getPub());
			alicesContact.addDrop(new DropURL("http://localhost:6000/12345678901234567890123456789012345678alice"));

			DropActor.send(EventEmitter.getDefault(), dm, bobsContact);
		} catch (MalformedURLException | QblDropInvalidURL e) {
			e.printStackTrace();
		}
	}

    protected void onDropMessage(DropMessage<?> dm) {
        System.out.println(dm.getData().as(HelloWorldObject.class).getStr());
    }
}
