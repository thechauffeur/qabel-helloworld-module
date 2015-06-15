package de.qabel.helloworld;

import de.qabel.core.drop.ModelObject;

public class HelloWorldObject extends ModelObject {
	public HelloWorldObject() { }
	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
}
