package de.qabel.helloworld;

import de.qabel.ackack.event.EventEmitter;
import de.qabel.core.config.Contact;
import de.qabel.core.config.Entity;
import de.qabel.core.drop.DropActor;
import de.qabel.core.drop.DropMessage;
import de.qabel.core.drop.ModelObject;
import de.qabel.core.module.Module;

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
		HashSet<Contact> contacts = new HashSet<>(this.getModuleManager().getDropActor().getContacts().getContacts());

		DropActor.send(EventEmitter.getDefault(), dm, contacts);
    }

    protected void onDropMessage(DropMessage<?> dm) {
        System.out.println(dm.getData().as(HelloWorldObject.class).getStr());
    }
}
