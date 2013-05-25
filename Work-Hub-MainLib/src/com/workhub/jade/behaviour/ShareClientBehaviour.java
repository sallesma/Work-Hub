package com.workhub.jade.behaviour;

import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import com.workhub.utils.Constants;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;
import jade.util.leap.Iterator;

import com.workhub.jade.agent.ClientAgent;

//Behaviour agent client
public class ShareClientBehaviour extends CyclicBehaviour {
	
	
	private MessageTemplate template = new MessageTemplate(new MatchExpression() {
		@Override
		public boolean match(ACLMessage msg) {
			JsonParser js = new JsonParser();
			int action = ((JsonObject) js.parse(msg.getContent())).get(Constants.JSON_ACTION).getAsInt();
			switch (action) {
			case Constants.MESSAGE_ACTION_SHARE:
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
			
			ClientAgent cli = (ClientAgent)myAgent;
			// Un clientAgent recoit d'un autre clientAgent un message lui proposant de partager un Element
			// on ajoute a la boite au lettre du clientAgent le message car l'element sera recupere au clic sur le bouton "recevoir"
			
		}

		
		// Lorsque le clientAgent clic sur recevoir il recupere l'AID de l'ElementAgent partage et envoie un message de GET_CONTENT  a l'agent pour mettre a jour son interface avec le nouvel element
		
	}

}
