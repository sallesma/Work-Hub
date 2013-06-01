package com.workhub.utils;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Random;

public class Utils {
	
	public static DFAgentDescription[] agentSearch(Agent agent, String agentType) {

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(agentType);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(agent, template);
			if (result.length > 0) {
				return result;
			}
		}
		catch(FIPAException fe) {}
		return null;
	}
}
