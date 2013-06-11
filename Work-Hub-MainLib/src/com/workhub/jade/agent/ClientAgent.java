package com.workhub.jade.agent;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.workhub.jade.behaviour.ContentClientBehaviour;
import com.workhub.jade.behaviour.GetAllElementTitlesBehaviour;
import com.workhub.jade.behaviour.ShareClientBehaviour;
import com.workhub.model.ElementModel;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
import com.workhub.utils.Utils;

public class ClientAgent extends GuiAgent implements ClientAgentInterface{

	PropertyChangeSupport changes = new PropertyChangeSupport(this);
    LinkedList<ACLMessage> reception_box = new LinkedList<ACLMessage>();
    String nickname;
	
    
    private void subscribeDFAgent(){
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.CLIENT_AGENT);
		sd.setName(this.getAID().toString());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	@Override
	protected void setup() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			if (args[0] instanceof PropertyChangeListener) {
				changes.addPropertyChangeListener((PropertyChangeListener) args[0]);
			}
		}
		

		registerO2AInterface(ClientAgentInterface.class, this);
		
		subscribeDFAgent();
		
		this.addBehaviour(new ContentClientBehaviour());
		this.addBehaviour(new ShareClientBehaviour());
	}


	@Override
	protected void onGuiEvent(GuiEvent ev) {
		ElementModel elementModel = null;
		ACLMessage message = null;
		
		switch (ev.getType()) {
		case Constants.EVENT_TYPE_SAVE:
			elementModel = (ElementModel)ev.getParameter(0);
			message =  MessageFactory.createMessage(this, elementModel.getAgent(), Constants.MESSAGE_ACTION_SAVE_CONTENT, elementModel);
			break;
			
		case Constants.EVENT_TYPE_SEND:
			List<AID> dests= new ArrayList<AID>(); 
			if(ev.getParameter(0)!=null){
				dests.add((AID)ev.getParameter(0));
			}else{
				DFAgentDescription[] receivers = Utils.agentSearch(this, Constants.CLIENT_AGENT);
				for(DFAgentDescription df : receivers){
					if(!df.getName().equals(getAID()))
						dests.add(df.getName());
				}
			}
			for (int i = 0; i < dests.size(); i++) {
				AID elementAgent = (AID)ev.getParameter(1);
				send( MessageFactory.createMessage(this, dests.get(i), Constants.MESSAGE_ACTION_SHARE, elementAgent));
			}
			
			break;
			
		case Constants.EVENT_TYPE_DELETE:
		{
			AID agent = (AID) ev.getParameter(0);
			message =  MessageFactory.createMessage(this, agent, Constants.MESSAGE_ACTION_DELETE, null);
			break;
		}			
		case Constants.EVENT_TYPE_GET_NEIGHBOURGS:
			DFAgentDescription[] listClientAgent = Utils.agentSearch(this, Constants.CLIENT_AGENT);
			//create Hashmap
			Map<AID, String> listToFire = new HashMap<AID, String>();
			for(DFAgentDescription df : listClientAgent){
				listToFire.put(df.getName(), Utils.getAgentName(df.getName()));
			}
			changes.firePropertyChange(String.valueOf(Constants.EVENT_TYPE_NEIGHBOURS), null, listToFire);
			break;
			
		case Constants.EVENT_TYPE_GET_ELEMENTS:
			DFAgentDescription[] listElementAgent = Utils.agentSearch(this, Constants.ELEMENT_AGENT);
			if(listElementAgent==null)
				break;
			for(DFAgentDescription df : listElementAgent){
				send(MessageFactory.createMessage(this, df.getName(), Constants.MESSAGE_ACTION_GET_TITLE, null));
			}
			break;
			
		case Constants.EVENT_TYPE_GET_ALL_ELEMENTS:
			DFAgentDescription[] listElementAgentToFire = Utils.agentSearch(this, Constants.ELEMENT_AGENT);

			if(listElementAgentToFire != null) {
				this.addBehaviour(new GetAllElementTitlesBehaviour(listElementAgentToFire.length));
				for(DFAgentDescription df : listElementAgentToFire){
					message = MessageFactory.createMessage(this, df.getName(), Constants.MESSAGE_ACTION_GET_ALL_TITLES, null);
					send(message);
					message = null;
				}
			}
			else {
				fireChanges(Constants.EVENT_TYPE_ELEMENTS_MAP, null);
			}

			break;
			
		case Constants.EVENT_TYPE_CREATE_ELEMENT:
			DFAgentDescription creatorAgent = Utils.agentSearch(this, Constants.CREATOR_AGENT)[0];
			message = MessageFactory.createMessage(this, creatorAgent.getName(), Constants.MESSAGE_ACTION_CREATE_ELEMENT, ev.getParameter(0)); 
			break;

		case Constants.EVENT_TYPE_CHARGE:
		{
			AID agent = (AID) ev.getParameter(0);
			message = MessageFactory.createMessage(this, agent, Constants.MESSAGE_ACTION_GET_CONTENT, null);
			break;
		}
		case Constants.EVENT_TYPE_ASK_EDIT:
		{
			AID agent = (AID) ev.getParameter(0);
			message = MessageFactory.createMessage(this, agent, Constants.MESSAGE_ACTION_EDIT, null);
			break;
		}
		case Constants.EVENT_TYPE_STOP_EDIT:
		{
			AID agent = (AID) ev.getParameter(0);
			message = MessageFactory.createMessage(this, agent, Constants.MESSAGE_ACTION_STOP_EDIT, null);
			break;
		}
		default:
			break;
		}
		
		if(message!=null){
			
			send(message);	
		}
	}




	public String getNickname() {
		return nickname;
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
	
	public void fireChanges (Integer typeMessage, Object params){
		changes.firePropertyChange(String.valueOf(typeMessage), null, params);
	}


}
