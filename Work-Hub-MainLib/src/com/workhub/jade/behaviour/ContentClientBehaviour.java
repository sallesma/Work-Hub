package com.workhub.jade.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workhub.jade.agent.ClientAgent;
import com.workhub.jade.agent.ElementAgent;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
// Behaviour agent client
public class ContentClientBehaviour extends CyclicBehaviour{

	
	private MessageTemplate template = new MessageTemplate(new MatchExpression() {
		@Override
		public boolean match(ACLMessage msg) {
			JsonParser js = new JsonParser();
			int action = ((JsonObject) js.parse(msg.getContent())).get(Constants.JSON_ACTION).getAsInt();
			switch (action) {
			case Constants.MESSAGE_ACTION_CONTENT:
			case Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT:
			case Constants.MESSAGE_RECEIVE_ELEMENT_TITLE:
			case Constants.MESSAGE_ACTION_EDIT:
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
			
			if(action == Constants.MESSAGE_ACTION_CONTENT){
				// si il recoit un MESSAGE_ACTION_CONTENT : demande contenu pour la mise a jour, envoie un MESSAGE_GET_CONTENT

				answer = MessageFactory.createMessage((ClientAgent)myAgent, message.getSender(), Constants.MESSAGE_ACTION_GET_CONTENT);
				myAgent.send(answer);
			}
			else if(action == Constants.MESSAGE_ACTION_EDIT){
				// Si action_edit : sait s'il peut etre editeur ou non
				boolean editor = ((JsonObject) js.parse(message.getContent())).get("can_edit").getAsBoolean();
				
				if(editor == true){
					// l'agent peut editer !
				}

			}
			
			//TODO
			// Si receive_element_content ou title : mettre a jour l'interface
		
		}
		
	
		
		
	}

}
