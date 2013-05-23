package com.workhub.jade.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workhub.utils.Constants;

//Behaviour de ElementAgent

public class ContentElementBehaviour extends CyclicBehaviour {

	private MessageTemplate template = new MessageTemplate(new MatchExpression() {
		
		@Override
		public boolean match(ACLMessage msg) {
			JsonParser js = new JsonParser();
			int action = ((JsonObject) js.parse(msg.getContent())).get(Constants.JSON_ACTION).getAsInt();
			switch (action) {
			case Constants.MESSAGE_ACTION_GET_CONTENT:
			case Constants.MESSAGE_ACTION_GET_TITLE:
			case Constants.MESSAGE_ACTION_SAVE_CONTENT:
				return true;
			default:
				return false;
			}
			
		}
	});
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		// si MESSAGE_ACTION_GET_CONTENT renvoie le contenu en envoyant au client un message MESSAGE_RECEIVE_ELEMENT_TITLE 
		// Si MESSAGE_ACTION_GET_TITLE en renvoyant au client un message MESSAGE_RECEIVE_ELEMENT_CONTENT
		// Va mettre a jour son ElementModel et envoyer a tous les agents un MESSAGE_ACTION_CONTENT
		
		
	}

	

}
