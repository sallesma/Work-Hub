package com.workhub.jade.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workhub.jade.agent.ElementAgent;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;

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
		
		ACLMessage message = myAgent.receive(template);
		if (message!=null){
			ACLMessage answer = null;

			JsonParser js = new JsonParser();
			int action = ((JsonObject) js.parse(message.getContent())).get(Constants.JSON_ACTION).getAsInt();
		
			// si MESSAGE_ACTION_GET_CONTENT renvoie le contenu en envoyant au client un message MESSAGE_RECEIVE_ELEMENT_TITLE 

			if(action == Constants.MESSAGE_ACTION_GET_CONTENT){
				answer = MessageFactory.createMessage((ElementAgent)myAgent, message.getSender(), Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT);
			}
			
			// Si MESSAGE_ACTION_GET_TITLE en renvoyant au client un message MESSAGE_RECEIVE_ELEMENT_CONTENT

			else if(action == Constants.MESSAGE_ACTION_GET_TITLE){
				answer = MessageFactory.createMessage((ElementAgent)myAgent, message.getSender(), Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT);
			}
			
			// Si MESSAGE_ACTION_SAVE_CONTENT Va mettre a jour son ElementModel et envoyer a tous les agents un MESSAGE_ACTION_CONTENT
			// TODO
			
			myAgent.send(answer);
			
		
		
		}
	}
	

}
