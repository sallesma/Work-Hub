package com.workhub.jade.behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workhub.jade.agent.ClientAgent;
import com.workhub.utils.Constants;
import com.workhub.utils.Utils;

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


			// on recupere dans le json du message l'AID de l'element a partager
			JsonParser js = new JsonParser();
			String agent = ((JsonObject) js.parse(message.getContent())).get("element").getAsString();

			AID element_shared = findElementAgent(agent);

			((ClientAgent)myAgent).fireChanges(Constants.EVENT_TYPE_RECEIVE_ELEMENT, element_shared);
		}


		// Lorsque le clientAgent clic sur recevoir il recupere l'AID de l'ElementAgent partage et envoie un message de GET_CONTENT  a l'agent pour mettre a jour son interface avec le nouvel element

	}

	private AID findElementAgent(String agent){
		DFAgentDescription[] result = Utils.agentSearch(myAgent, Constants.ELEMENT_AGENT);

		for(DFAgentDescription df : result){
			if(df.getName().getName().equals(agent)){
				return df.getName();
			}
		}
		return null;

	}

}
