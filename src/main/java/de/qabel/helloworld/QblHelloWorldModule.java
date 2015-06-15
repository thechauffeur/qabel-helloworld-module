package de.qabel.helloworld;

import de.qabel.ackack.MessageInfo;
import de.qabel.ackack.Responsible;
import de.qabel.ackack.event.EventEmitter;
import de.qabel.core.EventNameConstants;
import de.qabel.core.config.*;
import de.qabel.core.drop.DropActor;
import de.qabel.core.drop.DropMessage;
import de.qabel.core.module.Module;
import de.qabel.core.module.ModuleManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is an example Module which periodically sends a DropMessage with a HelloWorldObject
 * to all known Contacts. The Contacts have to be added somewhere else, e.g.
 * in from qabel-desktop. The content of received HelloWorldObjects is printed out
 * on the command line.
 *
 * Attention: moduleMain() and onEvent() are running in two different threads!
 */
public class QblHelloWorldModule extends Module {

	private ContactsActor contactsActor;
	// mContacts is a thread safe EntityMap and thus can be used by both threads
	private Contacts mContacts;

	public QblHelloWorldModule(ModuleManager moduleManager) {
		super(moduleManager);
	}

	@Override
	public void init() {
		this.mContacts = new Contacts();
		this.contactsActor = getModuleManager().getContactsActor();

		// Register to Contact changed events
		on(EventNameConstants.EVENT_CONTACT_ADDED, this);
		on(EventNameConstants.EVENT_CONTACT_REMOVED, this);

		// Register to HelloWorldObject DropMessages
		on(DropActor.EVENT_DROP_MESSAGE_RECEIVED_PREFIX + HelloWorldObject.class.getCanonicalName(), this);

		// Retrieve contacts from ContactsActor
		contactsActor.retrieveContacts(this, new Responsible() {
			@Override
			public void onResponse(Serializable... data) {
				ArrayList<Contact> receivedContacts = new ArrayList<>(Arrays.asList((Contact[]) data));
				for (Contact c : receivedContacts) {
					mContacts.put(c);
				}
			}
		});
	}

	@Override
	public void moduleMain() {
		// Periodically send messages
		while (!this.shouldStop) {
			sendMessages();
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send "Hello" to all Contacts
	 */
	private void sendMessages() {
		for (Contact contact : mContacts.getContacts()) {
			HelloWorldObject data = new HelloWorldObject();
			data.setStr("Hello this is " + contact.getContactOwner().getAlias());

			DropMessage<HelloWorldObject> dm = new DropMessage<>(contact.getContactOwner(), data);
			DropActor.send(EventEmitter.getDefault(), dm, contact);
		}
	}

	@Override
	public void onEvent(String event, MessageInfo info, Object... data) {
		// Handle Contact changed and DropMessage received events
		switch (event) {
			case EventNameConstants.EVENT_CONTACT_ADDED:
				if(data[0] instanceof Contact) {
					mContacts.put((Contact) data[0]);
				}
				break;
			case EventNameConstants.EVENT_CONTACT_REMOVED:
				if(data[0] instanceof String) {
					mContacts.remove((String) data[0]);
				}
				break;
			default:
				if(data[0] instanceof DropMessage) {
					DropMessage<HelloWorldObject> dropMessage = (DropMessage<HelloWorldObject>) data[0];
					System.out.println(dropMessage.getSender().getKeyIdentifier() + ": " +
							dropMessage.getData().getStr());
				}
				break;
		}
	}

	/**
	 * This method can be used to do cleanup work in your module before it gets stopped.
	 */
	@Override
	public synchronized void stopModule() {
		super.stopModule();
	}
}
