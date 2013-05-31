package com.workhub.jade.agent;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

public class ClientAgent extends GuiAgent implements ClientAgentInterface{

	
	PropertyChangeSupport changes = new PropertyChangeSupport(this);
    LinkedList<ACLMessage> reception_box = new LinkedList<ACLMessage>();
	
	@Override
	protected void setup() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			if (args[0] instanceof PropertyChangeListener) {
				changes.addPropertyChangeListener((PropertyChangeListener) args[0]);
			}
		}
		registerO2AInterface(ClientAgentInterface.class, this);
		
		/*ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		JsonObject j = new JsonObject();
		j.addProperty(Constants.JSON_ACTION, Constants.MESSAGE_ACTION_GET_CONTENT);
		String content = j.toString();
		message.setContent(content);
		message.setSender(this.getAID());
		message.addReceiver(new AID("ElementAgent1", true));
		
		this.send(message);*/
		
	}


	@Override
	protected void onGuiEvent(GuiEvent ev) {
		switch (ev.getType()) {
		case 1://TODO
			
			
			break;

		default:
			break;
		}
		
	}




	@Override
	public void fireOnGuiEvent(GuiEvent ev) {
		onGuiEvent(ev);		
	}
	
	public void add_element_reception_box(ACLMessage message){
		reception_box.add (message);
	}
	
	public void get_first_element_reception_box(){
		//TODO
		// quand on clique sur la boite de reception, on retire le dernier message 
		if(!reception_box.isEmpty()){
			ACLMessage message = reception_box.getFirst();
			// on recupere element et on envoie a element un GET_CONTENT
			//MessageFactory.createMessage(this, , Constants.MESSAGE_ACTION_GET_CONTENT);
			reception_box.removeFirst();
		}
	}




}
