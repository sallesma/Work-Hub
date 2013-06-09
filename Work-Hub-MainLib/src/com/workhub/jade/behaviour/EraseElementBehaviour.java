package com.workhub.jade.behaviour;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
			switch(msg.getPerformative()){
			case ACLMessage.INFORM: 
			case ACLMessage.REQUEST: 
			case ACLMessage.QUERY_IF: 
				break;
			default:
				return false;
			}

			JsonParser js = new JsonParser();
			JsonElement json= null;
			try{
				json = js.parse(msg.getContent());
			}catch(JsonSyntaxException e){
				return false;
			}
			int action = ((JsonObject) json).get(Constants.JSON_ACTION).getAsInt();
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
			
			
			((ElementAgent)myAgent).unsubscribeDFAgent();
			
			myAgent.doDelete();
		}
		else {
			block();
		}
	}

}
