package com.workhub.mt4j;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.widgets.MTList;

import processing.core.PApplet;

public class ContextMenu extends MTList {
	
	public ContextMenu(MTComponent source, int x, int y, PApplet applet, WorkHubScene scene, Integer menuType) {
		super(x, y, Constants.CONTEXT_BUTTON_WIDTH, sizeOf(menuType) * (Constants.CONTEXT_BUTTON_HEIGHT + 2), applet);
		initializeButtons(menuType, source, applet, scene);
		setAnchor(PositionAnchor.UPPER_LEFT);
		Utils.fixPosition(this, x, y, applet, PositionAnchor.UPPER_LEFT);
		
		setVisible(true);
	}

	private void initializeButtons(Integer menuType, MTComponent source, PApplet applet, WorkHubScene scene) {
		switch (menuType) {
		case Constants.CONTEXT_MAIN_MENU:
			ContextButton item01 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_TEXT);
			addListElement(item01);
			ContextButton item02 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_IMAGE);
			addListElement(item02);
			ContextButton item03 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_LINK);
			addListElement(item03);
			ContextButton item04 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_FILE);
			addListElement(item04);
			ContextButton item05 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_VISUALIZE_ELEMENTS);
			addListElement(item05);
			ContextButton item06 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_EXIT);
			addListElement(item06);
			ContextButton item07 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CLOSE);
			addListElement(item07);
			break;
		case Constants.CONTEXT_BACKGROUND_MENU:
			ContextButton item1 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_TEXT);
			addListElement(item1);
			ContextButton item2 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_IMAGE);
			addListElement(item2);
			ContextButton item3 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_LINK);
			addListElement(item3);
			ContextButton item4 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CREATE_FILE);
			addListElement(item4);
			ContextButton item5 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_VISUALIZE_ELEMENTS);
			addListElement(item5);
			ContextButton item6 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CLOSE);
			addListElement(item6);
			break;
		case Constants.CONTEXT_ELEMENT_MENU:
			ContextButton item11 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_EDIT_TITLE);
			addListElement(item11);
			ContextButton item12 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_EDIT_CONTENT);
			addListElement(item12);
			ContextButton item13 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_SHARE);
			addListElement(item13);
			ContextButton item14 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CHANGE_COLOR);
			addListElement(item14);
			ContextButton item15 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_EXPORT_PDF);
			addListElement(item15);
			ContextButton item16 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_HIDE);
			addListElement(item16);
			ContextButton item17 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_DELETE);
			addListElement(item17);
			ContextButton item18 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CLOSE);
			addListElement(item18);
			break;
		case Constants.CONTEXT_GROUP_MENU:
			ContextButton item21 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_SPLIT_GROUP);
			addListElement(item21);
			ContextButton item22 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_EXPORT_PDF);
			addListElement(item22);
			ContextButton item23 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_SHARE);
			addListElement(item23);
			ContextButton item24 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_HIDE);
			addListElement(item24);
			ContextButton item25 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_DELETE);
			addListElement(item25);
			ContextButton item26 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CLOSE);
			addListElement(item26);
			break;
		case Constants.CONTEXT_SHORTCUT_MENU:
			ContextButton item31 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_DELETE);
			addListElement(item31);
			ContextButton item32 = new ContextButton(applet, scene, source, Constants.CONTEXT_BUTTON_CLOSE);
			addListElement(item32);
			break;
		}
	}
	
	public static int sizeOf(int menuType) {
		switch(menuType) {
		case Constants.CONTEXT_MAIN_MENU :
			return 7;
		case Constants.CONTEXT_BACKGROUND_MENU :
			return 6;
		case Constants.CONTEXT_ELEMENT_MENU :
			return 8;
		case Constants.CONTEXT_GROUP_MENU :
			return 6;
		case Constants.CONTEXT_SHORTCUT_MENU :
			return 2;
		default :
			return -1;
		}
	}
}
