package com.workhub.jade.behaviour;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.workhub.jade.agent.ClientAgent;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
import com.workhub.utils.Utils;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

public class GetAllElementTitlesBehaviour extends SimpleBehaviour {

	private int compteur = 0;
	private int total;
	Map<AID, String> listToFire = new HashMap<AID, String>();
	private boolean isCompleted;

	public GetAllElementTitlesBehaviour(int tot){
		this.total = tot;
		isCompleted = false;
	}

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
			case Constants.MESSAGE_RECEIVE_ALL_TITLES:
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
			ACLMessage answer = null;

			JsonParser js = new JsonParser();
			int action = ((JsonObject) js.parse(message.getContent())).get(Constants.JSON_ACTION).getAsInt();
			String title = ((JsonObject) js.parse(message.getContent())).get(Constants.JSON_TITLE).getAsString();

			switch(action){
			case Constants.MESSAGE_RECEIVE_ALL_TITLES :
				listToFire.put(message.getSender(),title);
				compteur++;
				
				if (compteur==total){
					((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_ELEMENTS_MAP, listToFire);
					isCompleted = true;
				}
				
				break;
			}
		}else{
			block();
		}
	}

	@Override
	public boolean done() {
		return isCompleted;
	}
}
