package com.workhub.main;

import com.workhub.utils.Constants;

import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {

	public static void main(String[] args) {
		//Initialisation de l'interface JADE
		Runtime rt = Runtime.instance();
		Profile p = null;		
		try {
			//si on est pas hote ,
			//p = new ProfileImpl("AdresseIP de l'hote", port (1099),null);

			p = new ProfileImpl("prop");
			//crÈation d'un conteneur
			AgentContainer mc = rt.createMainContainer(p);

			AgentController clientAgent1 = mc.createNewAgent("ClientAgent1","com.workhub.jade.agent.ClientAgent",null);
			clientAgent1.start();

			AgentController clientAgent2 = mc.createNewAgent("ClientAgent2","com.workhub.jade.agent.ClientAgent",null);
			clientAgent2.start();
		
			AgentController clientAgent3 = mc.createNewAgent("ClientAgent3","com.workhub.jade.agent.ClientAgent",null);
			clientAgent3.start();
			
			AgentController elementAgent1 = mc.createNewAgent("ElementAgent1","com.workhub.jade.agent.ElementAgent",new Object[]{Constants.TYPE_ELEMENT_LINK});
			elementAgent1.start();
		
			AgentController elementAgent2 = mc.createNewAgent("ElementAgent2","com.workhub.jade.agent.ElementAgent",new Object[]{Constants.TYPE_ELEMENT_TEXT});
			elementAgent2.start();
		}
		catch (StaleProxyException e) {
			e.printStackTrace();
		} catch (ProfileException e1) {
			e1.printStackTrace();
		}

	}

}
