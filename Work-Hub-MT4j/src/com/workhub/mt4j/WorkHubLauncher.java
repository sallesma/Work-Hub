package com.workhub.mt4j;

import javax.swing.ImageIcon;

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
		try {
			this.
			addScene(new WorkHubScene(this, "WorkHub"));
			ImageIcon icon = new ImageIcon("Image/logo.png");
			frame.setIconImage(icon.getImage());
		} catch (WorkHubException e) {
			e.printStackTrace();
		}
	}
}
