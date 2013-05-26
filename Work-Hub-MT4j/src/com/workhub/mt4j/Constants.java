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
	public static final int Z_POSITION_DEFAULT_ELEMENT = 0;
	
	public static final int ELEMENT_DEFAULT_HEIGHT	= 200;
	public static final int ELEMENT_DEFAULT_WIDTH	= 200;
	
	public static final int CONTEXT_BUTTON_WIDTH	= 250;
	public static final int CONTEXT_BUTTON_HEIGHT	= 40;
	
	/*
	 * ID et taille du menu à la fois
	 */
	public static final int CONTEXT_MAIN_MENU		= 6;
	public static final int CONTEXT_ELEMENT_MENU	= 8;
	public static final int CONTEXT_GROUP_MENU		= 5;
	public static final int CONTEXT_SHORTCUT_MENU	= 2;
	
	public static final String CONTEXT_BUTTON_CLOSE					= "Fermer ce menu";
	public static final String CONTEXT_BUTTON_CREATE_TEXT			= "Créer un élément texte";
	public static final String CONTEXT_BUTTON_CREATE_IMAGE			= "Créer un élément image";
	public static final String CONTEXT_BUTTON_CREATE_LINK			= "Créer un élément lien";
	public static final String CONTEXT_BUTTON_CREATE_FILE			= "Créer un élément fichier";
	public static final String CONTEXT_BUTTON_VISUALIZE_ELEMENTS	= "Visualiser tous les éléments";
	public static final String CONTEXT_BUTTON_EDIT_TITLE			= "Editer le titre";
	public static final String CONTEXT_BUTTON_EDIT_CONTENT			= "Editer le contenu";
	public static final String CONTEXT_BUTTON_SHARE					= "Partager";
	public static final String CONTEXT_BUTTON_CHANGE_COLOR			= "Modifier la couleur";
	public static final String CONTEXT_BUTTON_EXPORT_PDF			= "Exporter en pdf";
	public static final String CONTEXT_BUTTON_HIDE					= "Masquer";
	public static final String CONTEXT_BUTTON_DELETE				= "Supprimer";
	public static final String CONTEXT_BUTTON_SPLIT_GROUP			= "Séparer le groupe";
}
