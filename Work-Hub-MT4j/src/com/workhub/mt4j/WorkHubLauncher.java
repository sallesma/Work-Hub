package com.workhub.mt4j;

import org.mt4j.MTApplication;

public class WorkHubLauncher extends MTApplication {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3337824151386218881L;

	public static void main(String[] args) {
		initialize();
	}
 
	@Override
	public void startUp() {
		addScene(new WorkHubScene(this, "WorkHub"));
	}
}
