package com.workhub.jade.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workhub.jade.agent.ElementAgent;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
import com.workhub.utils.Utils;

//Behaviour de ElementAgent

public class EditableElementBehaviour extends CyclicBehaviour{

	
	private MessageTemplate template = new MessageTemplate(new MatchExpression() {
	
		@Override
		public boolean match(ACLMessage msg) {
			//TODO: prévoir sis c'est un message systeme. Dans ce cas ce n'est pas forcement un JSON
			//MalformedJsonException
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
				/*ACLMessage answer = new ACLMessage(ACLMessage.INFORM);
				JsonObject j = new JsonObject();

				System.out.println("editor : "+((ElementAgent)myAgent).getEditor());
				boolean autorization = is_editable();
				System.out.println("editor : "+((ElementAgent)myAgent).getEditor());
				j.addProperty("can_edit", autorization);
				j.addProperty(Constants.JSON_ACTION, Constants.MESSAGE_ACTION_EDIT);
				String content = j.toString();
				answer.setContent(content);
				answer.setSender(myAgent.getAID());
				answer.addReceiver(message.getSender());
				System.out.println("je vais envoyer : "+answer);*/

				myAgent.send(answer);				
				break;
			}
		}
		else{
			block();
		}
		
		
	}
	
	public boolean is_editable(){
		
		if(((ElementAgent)myAgent).getEditor() != null){
			System.out.println("je sais qu'il y a un editeur : "+((ElementAgent)myAgent).getEditor());
			AID editor = ((ElementAgent)myAgent).getEditor();
			DFAgentDescription[] result = Utils.agentSearch(myAgent, Constants.CLIENT_AGENT);
			boolean ok = false;
			System.out.println("liste agent client"+result);
			
			for(DFAgentDescription df : result){
				if(df.getName() == editor){
					ok = true;
				}
				
			}
			if(!ok){
				System.out.println("je n'ai pas trouve l'editure : "+((ElementAgent)myAgent).getEditor());
				((ElementAgent)myAgent).setEditor(myAgent.getAID());
				return true;
			}else{
				//TODO
				System.out.println("what the fuck");
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
