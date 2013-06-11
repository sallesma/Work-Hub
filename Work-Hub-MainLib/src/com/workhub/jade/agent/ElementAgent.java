package com.workhub.jade.agent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.workhub.jade.behaviour.ContentElementBehaviour;
import com.workhub.jade.behaviour.EditableElementBehaviour;
import com.workhub.jade.behaviour.EraseElementBehaviour;
import com.workhub.model.ElementModel;
import com.workhub.model.LinkElementModel;
import com.workhub.model.PictureElementModel;
import com.workhub.model.TextElementModel;
import com.workhub.utils.Constants;
import com.workhub.utils.MessageFactory;
import com.workhub.utils.Utils;
public class ElementAgent extends Agent {
	
	
	 private AID editor = null;
	 private ElementModel contentModel;
	 private int type;


	private void subscribeDFAgent(){
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.ELEMENT_AGENT);
		sd.setName(this.getAID().toString());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	public void unsubscribeDFAgent(){

		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	
	private boolean findClientAgent(AID agent){
		DFAgentDescription[] result = Utils.agentSearch(this, Constants.CLIENT_AGENT);
		boolean ok = false;

		for(DFAgentDescription df : result){
			if(df.getName().getName().equals(editor.getName())){
				ok = true;
			}
			
		}
		return ok;

	}
	
	@Override
	protected void setup() {
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			
			int color = 0xFFF6EA6F;
			int typeModel = (Integer) args[0];
			
			if(typeModel==Constants.TYPE_ELEMENT_TEXT){
					TextElementModel text = new TextElementModel(color, Utils.getAgentName(getName()), this.getAID(), "...");
					this.setContentModel((ElementModel)text); 
					this.type = Constants.TYPE_ELEMENT_TEXT;
				
			}
			else if(typeModel == Constants.TYPE_ELEMENT_LINK){
				
					LinkElementModel link = new LinkElementModel(color, Utils.getAgentName(getName()), this.getAID(), "http://www.utc.fr/");
					this.setContentModel((ElementModel)link); 
					this.type = Constants.TYPE_ELEMENT_LINK;
					
			}
			
			else if(typeModel == Constants.TYPE_ELEMENT_PICTURE){
					PictureElementModel pic = new PictureElementModel(color, Utils.getAgentName(getName()), this.getAID(), null);
					this.setContentModel((ElementModel)pic);
					this.type = Constants.TYPE_ELEMENT_PICTURE;
			}
			
			/*else if(typeModel == Constants.TYPE_ELEMENT_FILE ){
				FileElementModel file = new FileElementModel(color, "postit file", this.getAID(), null);
				this.setContentModel((ElementModel)file);
			}*/
				
			}
		
			
		subscribeDFAgent();
		this.addBehaviour(new ContentElementBehaviour());
		this.addBehaviour(new EraseElementBehaviour()); 
		this.addBehaviour(new EditableElementBehaviour());
		
		if(args.length>1)
			send(MessageFactory.createMessage((ElementAgent)this, (AID) args[1], Constants.MESSAGE_RECEIVE_ELEMENT_CONTENT));
		
	}
	 
	public void fireModelUpdate(){
		//message a tous les clients informant la mise a jour du modele.
		
		
		
		DFAgentDescription[] receivers = Utils.agentSearch(this, Constants.CLIENT_AGENT);
		for(DFAgentDescription df : receivers){
		    this.send(MessageFactory.createMessage(this, df.getName(), Constants.MESSAGE_ACTION_ELEMENT_CHANGED));
		}
	}
	
	public AID getEditor() {
		return editor;
	}
	public void setEditor(AID editor) {
		this.editor = editor;
	}
	 public ElementModel getContentModel() {
			return contentModel;
		}

	public void setContentModel(ElementModel contentModel) {
			this.contentModel = contentModel;
		}
	
	public boolean lockEdit(AID agent){
		
		 // si editor n'est pas null et est encore connecté retourner false (ne peut pas modifier)
		if(this.editor != null){
			if(!findClientAgent(editor)){
				setEditor(agent);
				return true;
			}else{
				//TODO
				//envoyer une requete a l'agent éditeur pour confirmer qu'il édite tjrs
				return false;
			}
			
			
		}
		else{
			setEditor(agent);
			return true; // tu peux éditer
		}		 
	 }
	
	public int getType(){
		return this.type;
	}
	

	
}
