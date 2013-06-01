package com.workhub.jade.agent;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import com.workhub.model.ElementModel;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;

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
	}


	@Override
	protected void onGuiEvent(GuiEvent ev) {
		ElementModel elementModel = (ElementModel) ev.getSource();
		ACLMessage message = null;
		
		switch (ev.getType()) {
		case Constants.EVENT_TYPE_SAVE:
			message =  MessageFactory.createMessage(this, elementModel.getAgent(), Constants.MESSAGE_ACTION_SAVE_CONTENT, elementModel);
			break;
			
		case Constants.EVENT_TYPE_SEND://TODO
			break;
			
		case Constants.EVENT_TYPE_DELETE:
			message =  MessageFactory.createMessage(this, elementModel.getAgent(), Constants.MESSAGE_ACTION_DELETE, null);
			break;
			
		case Constants.EVENT_TYPE_GET_NEIGHBOURGS://TODO
			break;
			
		case Constants.EVENT_TYPE_GET_ELEMENTS://TODO
			break;
			
		case Constants.EVENT_TYPE_CREATE_ELEMENT://TODO
			break;
			
		case Constants.EVENT_TYPE_CHARGE://TODO
			break;
			
		default:
			break;
		}
	send(message);	
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
			Iterator list = message.getAllReplyTo();
			//MessageFactory.createMessage(this, , Constants.MESSAGE_ACTION_GET_CONTENT);
			reception_box.removeFirst();
		}
	}
}
