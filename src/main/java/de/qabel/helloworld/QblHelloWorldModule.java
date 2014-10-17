package de.qabel.helloworld;

import de.qabel.core.drop.DropController;
import de.qabel.core.drop.DropMessage;
import de.qabel.core.drop.DropQueueCallback;
import de.qabel.core.drop.ModelObject;
import de.qabel.core.module.Module;

public class QblHelloWorldModule extends Module {
	class HelloWorldObject extends ModelObject {
		private String str;

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}
	}
	
	DropController mDropController;
	private DropQueueCallback<HelloWorldObject> mQueue;
	public QblHelloWorldModule() {
		super(QblHelloWorldModule.class.getName());
		mDropController = new DropController();
	}

	@Override
	public void init() {
		mQueue = new DropQueueCallback<HelloWorldObject>();
		mDropController.register(HelloWorldObject.class, mQueue);
	}

	public void _run() throws InterruptedException {
		for(DropMessage<HelloWorldObject> msg = mQueue.take(); msg != null; msg = mQueue.take()) {
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
