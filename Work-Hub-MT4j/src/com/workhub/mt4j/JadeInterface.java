package com.workhub.mt4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Stack;

import org.mt4j.util.MTColor;

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
	private Stack<AID> inbox;
	private String nickname;

	private JadeInterface() {
		inbox = new Stack<>();
	}

	public static JadeInterface getInstance() {
		return instance;
	}

	public void setScene(WorkHubScene scene) {
		this.scene = scene;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public AID getLastMessageAID() {
		return inbox.lastElement();
	}

	public void removeLastMessage() {
		inbox.pop();
		if(inbox.isEmpty()) {
			scene.getShortcut(MT4JConstants.BUTTON_ID_RECEVOIR).setFillColor(new MTColor(110, 200, 240, 255));
		}
	}

	public void startJade(String hostID, String platformID, boolean isHost, String nickname) {
		Runtime rt = Runtime.instance();
		Profile profile = isHost ? new ProfileImpl(null , 1099, null) : new ProfileImpl(hostID, 1099, platformID, false);
		//profile.setParameter(Profile.GUI, "true"); // Pour debugger
		ContainerController cc = isHost ? rt.createMainContainer(profile) : rt.createAgentContainer(profile);
		try {
			agentController = cc.createNewAgent(nickname, "com.workhub.jade.agent.ClientAgent", new Object[] { this });
			agentController.start();
			if(isHost) {
				AgentController creatorAgent = cc.createNewAgent("creatorAgent","com.workhub.jade.agent.CreatorAgent",null);
				creatorAgent.start();
			}
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
		GuiEvent event = new GuiEvent(null,Constants.EVENT_TYPE_SEND);
		event.addParameter(dest);
		event.addParameter(elementAgent);
		fireOnGuiEvent(event);
	}

	public void receiveElement() {
		AID aidModel = inbox.pop();
		getElement(aidModel);
		if(inbox.isEmpty()) {
			scene.getShortcut(MT4JConstants.BUTTON_ID_RECEVOIR).setFillColor(new MTColor(110, 200, 240, 255));
		}
	}

	public boolean hasMessages() {
		return !inbox.isEmpty();
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
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_GET_ALL_ELEMENTS);
		fireOnGuiEvent(event);
	}

	public void getNeightbourgList(){
		GuiEvent event = new GuiEvent(null, Constants.EVENT_TYPE_GET_NEIGHBOURGS);
		fireOnGuiEvent(event);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
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
					}
					else{
						if(!ContextMenu.importLocation.isEmpty()) {
							AbstractElementView.createEmptyElement(model.getType(), MT4JUtils.removeBeginning(ContextMenu.importLocation), scene.getMTApplication(), scene);
						}
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
				case Constants.EVENT_TYPE_ELEMENTS_MAP:
				{
					@SuppressWarnings("unchecked")
					Map<AID, String> map = (Map<AID, String>)event.getNewValue();
					if(map != null) {
						scene.openContextualMenu(map, MT4JConstants.CONTEXT_IMPORT_MENU);
					}
					else {
						MT4JUtils.removeBeginning(ContextMenu.elementViewLocation);
					}
					break;
				}
				case Constants.EVENT_TYPE_NEIGHBOURS:
				{
					@SuppressWarnings("unchecked")
					Map<AID, String> map = (Map<AID, String>)event.getNewValue();
					scene.openContextualMenu(map, MT4JConstants.CONTEXT_EXPORT_MENU);
					break;
				}
				case Constants.EVENT_TYPE_CAN_EDIT:
				{
					AID aidModel = (AID)event.getNewValue();
					AbstractElementView element = null;
					element = scene.getElement(aidModel);
					if(element!=null){
						if(element instanceof TextElementView || element instanceof LinkElementView) {
							element.setEnableKeyboard(true);
						}
						else {
							element.editElementContent();
						}
					}
					break;
				}
				case Constants.EVENT_TYPE_CANT_EDIT:
				{
					// TODO visualisation de l'acc�s refus�
					AID aidModel = (AID)event.getNewValue();
					AbstractElementView element = null;
					element = scene.getElement(aidModel);
					if(element!=null && element.getKeyboard() != null){
						element.getKeyboard().destroy();
					}
					break;
				}
				case Constants.EVENT_TYPE_RECEIVE_ELEMENT:
				{
					AID aidModel = (AID)event.getNewValue();
					inbox.add(aidModel);
					scene.getShortcut(MT4JConstants.BUTTON_ID_RECEVOIR).setFillColor(MTColor.RED);
				}
				}
			}
		};
		scene.runnableStack.add(r);
	}
}
