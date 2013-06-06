package com.workhub.jade.agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.workhub.jade.behaviour.ContentElementBehaviour;
import com.workhub.jade.behaviour.CreateElementBehaviour;
import com.workhub.utils.Constants;

public class CreatorAgent extends Agent{

	private void subscribeDFAgent(){
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.CREATOR_AGENT);
		sd.setName(this.getAID().toString());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	protected void setup() {
		subscribeDFAgent();
		this.addBehaviour(new CreateElementBehaviour());
	}
	
}
