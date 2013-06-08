package com.workhub.mt4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Map.Entry;

import com.workhub.jade.agent.ClientAgentInterface;
import com.workhub.model.ElementModel;
import com.workhub.utils.Constants;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public final class JadeInterface implements PropertyChangeListener {
	private static volatile JadeInterface instance = new JadeInterface();

	private AgentController agentController;
	private WorkHubScene scene;
	
	private JadeInterface() {
	}
	
	public static JadeInterface getInstance() {
		return instance;
	}
	
	public void setScene(WorkHubScene scene) {
		this.scene = scene;
	}
	
	public void startJade(String hostID, String platformID, boolean isHost, String nickname) {
		Runtime rt = Runtime.instance();
		Profile profile = isHost ? new ProfileImpl(null , 1099, null) : new ProfileImpl(hostID, 1099, platformID, false);
		//profile.setParameter(Profile.GUI, "true"); // Pour debugger
		ContainerController cc = rt.createMainContainer(profile);
		try {
			agentController = cc.createNewAgent(nickname, "com.workhub.jade.agent.ClientAgent", new Object[] { this });
			agentController.start();
			AgentController creatorAgent = cc.createNewAgent("creatorAgent","com.workhub.jade.agent.CreatorAgent",null);
			creatorAgent.start();
		} catch (StaleProxyException e) {
			System.err.println("Erreur lors de la creation de l'agent");
			e.printStackTrace();
		}
	}

	private ClientAgentInterface getAgent() throws StaleProxyException {
		return agentController.getO2AInterface(ClientAgentInterface.class);
	}
	
	private void fireOnGuiEvent(GuiEvent event) {
		try {
			getAgent().fireOnGuiEvent(event);
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

	public void sendElement(AID dest, AID elementAgent){
		Object[] param = {dest , elementAgent};
		GuiEvent event = new GuiEvent(null,Constants.EVENT_TYPE_SEND);
		event.addParameter(dest);
		event.addParameter(elementAgent);
		fireOnGuiEvent(event);
	}
	
	public void askEdition(AID elementAgent){
		GuiEvent event = new GuiEvent(null,Constants.EVENT_TYPE_ASK_EDIT);
		event.addParameter(elementAgent);
		fireOnGuiEvent(event);
	}

	public void finishEdition(AID elementAgent){
		GuiEvent event = new GuiEvent(null,Constants.EVENT_TYPE_STOP_EDIT);
		event.addParameter(elementAgent);
		fireOnGuiEvent(event);
	}

	public void createElement(int elementType){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_CREATE_ELEMENT);
		
		event.addParameter(elementType);
		fireOnGuiEvent(event);
	}

	public void deleteElement(AID elementAgent){
		GuiEvent event = new GuiEvent(null,Constants.EVENT_TYPE_DELETE);
		event.addParameter(elementAgent);
		fireOnGuiEvent(event);
	}

	public void saveElement(ElementModel model){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_SAVE);
		event.addParameter(model);
		fireOnGuiEvent(event);
	}
	
	public void saveElement(AbstractElementView element){
		saveElement(element.getModel());
	}

	public void getElement(AID agentAID ){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_CHARGE);
		event.addParameter(agentAID);
		fireOnGuiEvent(event);
	}

	public void getElementList(){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_GET_ELEMENTS);
		fireOnGuiEvent(event);
	}

	public void getNeightbourgList(){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_GET_NEIGHBOURGS);
		fireOnGuiEvent(event);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		switch ((Integer.parseInt(event.getPropertyName()))) {
		case Constants.EVENT_TYPE_CHANGE:
		{
			AID aidModel = (AID)event.getNewValue();
			AbstractElementView element = null;
			element = scene.getElement(aidModel);
			if(element!=null){
				getElement(aidModel);
			}
			break;
		}
		case Constants.EVENT_TYPE_CONTENU:
		{
			ElementModel model = (ElementModel)event.getNewValue();
			AbstractElementView element = null;
			element = scene.getElement(model.getAgent());
			if(element!=null){
				element.setModel(model);
			}else{
				try {
					scene.attachModel(model);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
		case Constants.EVENT_TYPE_DIED:
		{
			AID agent = (AID)event.getNewValue();
			AbstractElementView element = scene.getElement(agent);
			if(element!=null){
				element.destroy();
			}
			break;
		}
		case Constants.EVENT_TYPE_ELEMENTS:
		{
			Map<AID, String> map = (Map<AID, String>)event.getNewValue();
			
			for (Entry<AID, String> entry : map.entrySet()) {
				//scene.addToAdapter(entry); TODO
			}
			

			break;
		}
		case Constants.EVENT_TYPE_NEIGHBOURS:
		{
			Map<AID, String> map = (Map<AID, String>)event.getNewValue();
			for (Entry<AID, String> entry : map.entrySet()) {
				//scene.addToAdapter(entry); TODO
			}

			break;
		}
		case Constants.EVENT_TYPE_CAN_EDIT:
		{
			AID aidModel = (AID)event.getNewValue();
			AbstractElementView element = null;
			element = scene.getElement(aidModel);
			if(element!=null){
				element.setEnableKeyboard(true);
			}
			break;
		}
		case Constants.EVENT_TYPE_CANT_EDIT:
		{
			// TODO visualisation de l'accès refusé
			AID aidModel = (AID)event.getNewValue();
			AbstractElementView element = null;
			element = scene.getElement(aidModel);
			if(element!=null && element.getKeyboard() != null){
				element.getKeyboard().destroy();
			}
			break;
		}
		}

	}
}
