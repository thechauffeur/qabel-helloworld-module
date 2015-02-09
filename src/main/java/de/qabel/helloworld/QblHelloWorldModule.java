package de.qabel.helloworld;

import de.qabel.ackack.event.EventEmitter;
import de.qabel.core.drop.DropActor;
import de.qabel.core.drop.DropMessage;
import de.qabel.core.drop.DropQueueCallback;
import de.qabel.core.drop.ModelObject;
import de.qabel.core.module.Module;

public class QblHelloWorldModule extends Module {
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

        DropMessage<HelloWorldObject> dm = new DropMessage<HelloWorldObject>();
        dm.setData(data);
        dm.setModelObject(HelloWorldObject.class);

		DropActor.send(EventEmitter.getDefault(), dm, this.getModuleManager().getDropActor().getContacts().getContacts());
    }

	@Override
    protected void onDropMessage(DropMessage<?> dm) {
        System.out.println(dm.getData().as(HelloWorldObject.class).getStr());
    }
}
