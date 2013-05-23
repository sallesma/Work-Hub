package com.workhub.jade.behaviour;

import jade.core.AID;
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

public class EditableElementBehaviour extends CyclicBehaviour{

	
	private MessageTemplate template = new MessageTemplate(new MatchExpression() {
	
		@Override
		public boolean match(ACLMessage msg) {
			JsonParser js = new JsonParser();
			int action = ((JsonObject) js.parse(msg.getContent())).get(Constants.JSON_ACTION).getAsInt();
			switch (action) {
			case Constants.MESSAGE_ACTION_EDIT:
				return true;
			default:
				return false;
			}
			
		}
	});
	
	
	@Override
	public void action() {
		
		ACLMessage message = myAgent.receive(template);
		if(message!=null){
			JsonParser js = new JsonParser();
			int action = ((JsonObject) js.parse(message.getContent())).get(Constants.JSON_ACTION).getAsInt();
		
			switch (action) {
			case Constants.MESSAGE_ACTION_EDIT:
				AID receiver = message.getSender();				
				ACLMessage answer = MessageFactory.createMessage((ElementAgent)myAgent, receiver, action);
				myAgent.send(answer);				
				break;
			}
		}
		else{
			block();
		}
		
		
	}
	
	
	

}
