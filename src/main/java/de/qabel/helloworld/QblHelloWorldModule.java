package de.qabel.helloworld;

import java.util.Date;

import de.qabel.core.config.Contacts;
import de.qabel.core.drop.Drop;
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

	private DropQueueCallback<HelloWorldObject> mQueue;

	public QblHelloWorldModule() {
		super(QblHelloWorldModule.class.getName());
	}

	@Override
	public void init() {
		mQueue = new DropQueueCallback<HelloWorldObject>();
		getModuleManager().getDropController().register(HelloWorldObject.class, mQueue);
	}

	public void _run() throws InterruptedException {
		DropMessage<HelloWorldObject> dm = new DropMessage<HelloWorldObject>();
		HelloWorldObject data = new HelloWorldObject();
		data.setStr("Hello World");
		dm.setData(data);
		
        Date date = new Date();
        dm.setTime(date);
        dm.setVersion(1);
        dm.setModelObject(HelloWorldObject.class);
        Drop<HelloWorldObject> drop = new Drop<HelloWorldObject>();
        Contacts contacts = new Contacts();
        drop.sendAndForget(dm, contacts);

		for (DropMessage<HelloWorldObject> msg = mQueue.take(); msg != null; msg = mQueue
				.take()) {
			System.out.println(msg.getData().getStr());
		}
	}

	@Override
	public void run() {
		try {
			_run();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
