package com.workhub.mt4j;

import org.mt4j.components.visibleComponents.widgets.MTList;

import processing.core.PApplet;

public class ContextMenu extends MTList {
//	ArrayList<ContextButton> buttons = new ArrayList<ContextButton>();
	
	public ContextMenu(int x, int y, PApplet applet, Integer menuType) {
		super(x, y, Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, applet);
		initializeButtons(menuType, applet);
//		for(ContextButton button : buttons) {
//			addChild(button);
//		}
		setHeightXYGlobal(getChildCount() * Constants.CONTEXT_BUTTON_HEIGHT);
		setVisible(true);
	}

	private void initializeButtons(Integer menuType, PApplet applet) {
		switch (menuType) {
		case Constants.CONTEXT_MAIN_MENU:
			ContextButton item1 = new ContextButton(applet, "Créer un élément texte");
			addListElement(item1);
			ContextButton item2 = new ContextButton(applet, "Créer un élément image");
			addListElement(item2);
			ContextButton item3 = new ContextButton(applet, "Créer un élément lien");
			addListElement(item3);
			ContextButton item4 = new ContextButton(applet, "Créer un élément fichier");
			addListElement(item4);

			break;
		}
	}	
}
