package com.workhub.mt4j;

public class Constants {
	public static final int ELEMENT_TEXT 		= 0;
	public static final int ELEMENT_LINK 		= 1;
	public static final int ELEMENT_IMAGE		= 2;
	public static final int ELEMENT_FILE 		= 3;
	
	public static final String BUTTON_ID_MENU		= "Menu";
	public static final String BUTTON_ID_ENVOYER	= "Envoyer";
	public static final String BUTTON_ID_RECEVOIR	= "Recevoir";
	public static final String BUTTON_ID_MASQUER	= "Masquer";
	
	/*
	 * Premier bit : 0 = gauche, 1 = droite
	 * Deuxième bit : 0 = haut, 1 = bas
	 */
	public static final int CORNER_TOP_LEFT		= 0x00;
	public static final int CORNER_TOP_RIGHT	= 0x01;
	public static final int CORNER_BOTTOM_LEFT	= 0x10;
	public static final int CORNER_BOTTOM_RIGHT	= 0x11;

	public static final int Z_POSITION_DEFAULT_BUTTON = 0;
	public static final int Z_POSITION_DEFAULT_ELEMENT = 1;
	
	
}
