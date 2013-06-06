package com.workhub.jade.agent;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.workhub.jade.behaviour.ContentClientBehaviour;
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
			AID dest = (AID)ev.getParameter(0);
			AID elementAgent = (AID)ev.getParameter(1);
			message =  MessageFactory.createMessage(this, dest, Constants.MESSAGE_ACTION_SHARE, elementAgent);
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
			for(DFAgentDescription df : listElementAgent){
				send(MessageFactory.createMessage(this, df.getName(), Constants.MESSAGE_ACTION_GET_TITLE, null));
			}
			break;
			
		case Constants.EVENT_TYPE_CREATE_ELEMENT:
			AgentController newElement;
			try {
				AgentContainer controller = getContainerController();
				int type = (Integer) ev.getParameter(0);
				 Date dNow = new Date( );
			      SimpleDateFormat ft = 
			      new SimpleDateFormat ("Nouvel element : hh:mm:ss");
			      
				newElement = controller.createNewAgent(ft.format(dNow),"com.workhub.jade.agent.ElementAgent",new Object[]{type, this.getAID()});
				newElement.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	@Override
	public AID getAgentAID() {
		return getAID();
	}
	
}
