package com.workhub.utils;

import jade.core.AID;

public class Constants {
	public final static int TYPE_ELEMENT_TEXT = 1000;
	public final static int TYPE_ELEMENT_PICTURE = 1001;
	public final static int TYPE_ELEMENT_LINK = 1002;
	public final static int TYPE_ELEMENT_FILE = 1003;
	public static final String ELEMENT_AGENT = "ELEMENT";
	public static final String CLIENT_AGENT = "CLIENT";
	public static final String CREATOR_AGENT = "CREATOR";

	public static final int MESSAGE_ACTION_EDIT = 15000;
	public static final int MESSAGE_ACTION_GET_CONTENT = 15001;
	public static final int MESSAGE_ACTION_GET_TITLE = 15002;
	public static final int MESSAGE_ACTION_SAVE_CONTENT = 15003;
	public static final int MESSAGE_ACTION_DELETE = 15004;
	public static final int MESSAGE_ACTION_SHARE = 15005;
	public static final int MESSAGE_ACTION_CONTENT= 15006;
	public static final int MESSAGE_RECEIVE_ELEMENT_CONTENT= 15007;
	public static final int MESSAGE_RECEIVE_ELEMENT_TITLE= 15008;
	public static final int MESSAGE_ACTION_IS_DYING = 15009;
	public static final int MESSAGE_ACTION_CREATE_ELEMENT = 15010;
	public static final int MESSAGE_ACTION_STOP_EDIT = 150011; 
	
	public static final int MESSAGE_ACTION_ELEMENT_CHANGED= 15012; //TODO element venant d'être édité -> tous les clients (client -> interface : cf EVENT_TYPE_CHANGE)
	public static final int MESSAGE_ACTION_GET_ALL_TITLES = 15013;
	public static final int MESSAGE_RECEIVE_ALL_TITLES = 15014;



	public static final String JSON_ACTION = "action";
	public static final String JSON_AGENT_TYPE = "agent_type";
	public static final String JSON_AID = "aid";
	public static final String JSON_CAN_EDIT = "can_edit";
	public static final String JSON_ELEMENT = "element";
	public static final String JSON_CONTENT = "content";
	public static final String JSON_TITLE = "title";

	
	
	public static final int EVENT_TYPE_SAVE = 16000; //Interface -> agent : Sauve l'element : ok
	
	//param : AID de l'element
	public static final int EVENT_TYPE_CHANGE = 16001; //agent -> Interface : l'element a changé : ok
	public static final int EVENT_TYPE_CHARGE = 16002; //Interface -> agent : demande de contenu d'element

	public static final int EVENT_TYPE_RECEIVE_ELEMENT = 16004; //agent -> interface : il y a un nouveau message
	
	// param : elementmodel
	public static final int EVENT_TYPE_CONTENU = 16003; //agent -> interface : contenu de l'element


	//		Object[] params = {AID dest, AID elementAgent};
	public static final int EVENT_TYPE_SEND = 16005; //interface -> agent : envoi le message
	public static final int EVENT_TYPE_DELETE = 16006; //interface -> agent : Supprime l'agent element : ok
	public static final int EVENT_TYPE_DIED = 16007; //agent -> interface: L'element est mort, ne l'affiche plus : ok
	public static final int EVENT_TYPE_GET_NEIGHBOURGS= 16008; // interface -> agent : quels sont les voisins a qui je peux envoyer ?

	// param: Map<AID, String> = HashMap<AID agentID, String name>()
	public static final int EVENT_TYPE_NEIGHBOURS= 16009; // agent -> interface : liste des voisins
	public static final int EVENT_TYPE_GET_ELEMENTS= 16010; // interface -> agent : quels sont les elements disponibles
	public static final int EVENT_TYPE_GET_ALL_ELEMENTS = 16017; // interface -> agent : quels sont les elements disponibles
	public static final int EVENT_TYPE_ELEMENTS_MAP = 16017; //agent -> interface : map des agents element dispo et de leur titre

	//param : pour chaque element dont l'agent recois un json, l'agent renvois un model à l'interface
	public static final int EVENT_TYPE_ELEMENTS= 16011; // agent -> interface : liste des elements
	
	
	// param: Map<AID, String> = HashMap<AID agentID, String name>()
	public static final int EVENT_TYPE_CREATE_ELEMENT= 16012; // interface -> agent : créer l'element
	
	//param : AID de l'agent editable
	public static final int EVENT_TYPE_CAN_EDIT = 16013; // agent -> interface : edition possible : ok
	//param : AID de l'agent non editable
	public static final int EVENT_TYPE_CANT_EDIT = 16014;// agent -> interface : edition impossible : ok
	public static final int EVENT_TYPE_ASK_EDIT = 16015; // interface -> agent : puis je editer : ok
	public static final int EVENT_TYPE_STOP_EDIT = 16016; // interface -> agent : je n'édite plus (en cas d'annulation)

	//public static final String JSON_ACTION = "action";
	//public static final String JSON_ACTION = "action";

}
