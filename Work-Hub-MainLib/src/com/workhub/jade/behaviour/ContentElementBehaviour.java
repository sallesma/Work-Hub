package com.workhub.jade.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workhub.jade.agent.ElementAgent;
import com.workhub.model.*;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
import com.workhub.utils.Utils;

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

			switch(action){
				case Constants.MESSAGE_ACTION_GET_CONTENT :
					answer = MessageFactory.createMessage((ElementAgent)myAgent, message.getSender(), Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT);
					myAgent.send(answer);
					break;
					
				case Constants.MESSAGE_ACTION_GET_TITLE:
					answer = MessageFactory.createMessage((ElementAgent)myAgent, message.getSender(), Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT);
					myAgent.send(answer);
					break;
					
				case Constants.MESSAGE_ACTION_SAVE_CONTENT:
					ElementModel model = MessageFactory.getModel(message);
					// mise a jour model
					int typeModel = model.getType();
					String new_title =  model.getTitle();
					int new_color = model.getColor();
					
					
					switch(typeModel){
						case Constants.TYPE_ELEMENT_TEXT :
							((TextElementModel)(((ElementAgent)myAgent).getContentModel())).setContent(((TextElementModel)model).getContent());		
							break;
						
						case Constants.TYPE_ELEMENT_LINK:
							((LinkElementModel)(((ElementAgent)myAgent).getContentModel())).setContent(((LinkElementModel)model).getContent());
							break;
						
						case Constants.TYPE_ELEMENT_PICTURE :
							((PictureElementModel)(((ElementAgent)myAgent).getContentModel())).setContent(((PictureElementModel)model).getContent());
							break;
						
						case Constants.TYPE_ELEMENT_FILE :
							((FileElementModel)(((ElementAgent)myAgent).getContentModel())).setContent(((FileElementModel)model).getContent());
							break;
						
						default :
							break;
					
					}
					
					(((ElementAgent)myAgent).getContentModel()).setTitle(new_title);
					(((ElementAgent)myAgent).getContentModel()).setColor(new_color);
					
					// envoyer a tous les clients agents un message de type MESSAGE_ACTION_CONTENT
					((ElementAgent)myAgent).fireModelUpdate();
					
					// on remet l'editeur à null
					((ElementAgent)myAgent).setEditor(null);
					break;
					
				
				default :
					break;
			}
			
		
		}
	}
	

}
