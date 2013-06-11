package com.workhub.jade.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.workhub.jade.agent.ElementAgent;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
import com.workhub.utils.Utils;

//Behaviour de ElementAgent

public class EditableElementBehaviour extends CyclicBehaviour{

	
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
			case Constants.MESSAGE_ACTION_EDIT:
			case Constants.MESSAGE_ACTION_STOP_EDIT:
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

				if (answer !=null){
					myAgent.send(answer);
				}				
				break;
			case Constants.MESSAGE_ACTION_STOP_EDIT:
				((ElementAgent)myAgent).setEditor(null);	
				break;
			}
		}
		else{
			block();
		}
		
		
	}
	
	public boolean is_editable(){
		
		if(((ElementAgent)myAgent).getEditor() != null){
			AID editor = ((ElementAgent)myAgent).getEditor();
			DFAgentDescription[] result = Utils.agentSearch(myAgent, Constants.CLIENT_AGENT);
			boolean ok = false;
			
			for(DFAgentDescription df : result){
				if(df.getName() == editor){
					ok = true;
				}
				
			}
			if(!ok){
				((ElementAgent)myAgent).setEditor(myAgent.getAID());
				return true;
			}else{
				//TODO
				//envoyer une requete a l'agent éditeur pour confirmer qu'il édite tjrs
				return false;
			}
		
		}
		else{
			((ElementAgent)myAgent).setEditor(myAgent.getAID());
			return true;
		}
	}
	
	
	

}
