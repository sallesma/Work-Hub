package com.workhub.jade.behaviour;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workhub.jade.agent.ElementAgent;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
import com.workhub.utils.Utils;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

public class EraseElementBehaviour extends CyclicBehaviour{
	
	private MessageTemplate template = new MessageTemplate(new MatchExpression() {
		@Override
		public boolean match(ACLMessage msg) {
			//TODO: prévoir sis c'est un message systeme. Dans ce cas ce n'est pas forcement un JSON
			//MalformedJsonException
			JsonParser js = new JsonParser();
			int action = 0;
			try {
				action = ((JsonObject) js.parse(msg.getContent())).get(Constants.JSON_ACTION).getAsInt();
			} catch (Exception e) {
				return false;
			}
			
			switch (action) {
			case Constants.MESSAGE_ACTION_DELETE:
				return true;
			default:
				return false;
			}
		}
	});
	
	@Override
	public void action() {
		ACLMessage message = myAgent.receive(template);
		if (message!=null){
			//Il reçoit des messages du type :
			//{"action" : "15004"}
			
			DFAgentDescription[] receivers = Utils.agentSearch(myAgent, Constants.CLIENT_AGENT);
			for(DFAgentDescription df : receivers)
				myAgent.send(MessageFactory.createMessage((ElementAgent) myAgent, df.getName(), Constants.MESSAGE_ACTION_IS_DYING));
			
			myAgent.doDelete();
		}
		else {
			block();
		}
	}

}
