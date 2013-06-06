package com.workhub.utils;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

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
	
	public static String getAgentName(AID aid){
		return getAgentName(aid.getName());
	}
	public static String getAgentName(String string){
		return string.split("@")[0];
	}
}
