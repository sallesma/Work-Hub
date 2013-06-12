package com.workhub.jade.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.workhub.jade.agent.ClientAgent;
import com.workhub.model.ElementModel;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
// Behaviour agent client
public class ContentClientBehaviour extends CyclicBehaviour{


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
			case Constants.MESSAGE_ACTION_CONTENT:
			case Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT:
			case Constants.MESSAGE_RECEIVE_ELEMENT_TITLE:
			case Constants.MESSAGE_ACTION_EDIT:
			case Constants.MESSAGE_ACTION_IS_DYING:
			case Constants.MESSAGE_ACTION_ELEMENT_CHANGED:
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
			
			switch(action){
				case Constants.MESSAGE_ACTION_CONTENT :
					// si il recoit un MESSAGE_ACTION_CONTENT : demande contenu pour la mise a jour, envoie un MESSAGE_GET_CONTENT
					answer = MessageFactory.createMessage((ClientAgent)myAgent, message.getSender(), Constants.MESSAGE_ACTION_GET_CONTENT);
					if (answer !=null){
						myAgent.send(answer);
					}
					break;
					
				case Constants.MESSAGE_ACTION_EDIT:
					// Si action_edit : sait s'il peut etre editeur ou non

					boolean editor = ((JsonObject) js.parse(message.getContent())).get(Constants.JSON_CAN_EDIT).getAsBoolean();

					if(editor == true){
						// envoyer a l'interface un evenement de type EVENT_CAN_EDIT
						((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_CAN_EDIT, message.getSender());
					}
					else{
						// envoyer a l'interface un evenement de type EVENT_CANT_EDIT
						((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_CANT_EDIT, message.getSender());
					}
					break;
				
				case Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT :
					//mettre a jour l'interface
					// renvoie un elementModel
					ElementModel model = MessageFactory.getModel(message);
					((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_CONTENU, model);
					break;
					
				case Constants.MESSAGE_RECEIVE_ELEMENT_TITLE :
					((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_ELEMENTS, MessageFactory.getTitle(message));
					break;

				case Constants.MESSAGE_ACTION_IS_DYING:
					((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_DIED, message.getSender());
					break;
				
				case Constants.MESSAGE_ACTION_ELEMENT_CHANGED :
					((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_CHANGE, message.getSender());
					break;
					
				default:
					break;
					
			}

			
		}else{
			block();
		}




	}

}
