package com.workhub.jade.agent;
import java.beans.PropertyChangeListener;
import java.io.File;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.workhub.jade.behaviour.ContentElementBehaviour;
import com.workhub.jade.behaviour.EraseElementBehaviour;
import com.workhub.model.ElementModel;
import com.workhub.model.FileElementModel;
import com.workhub.model.LinkElementModel;
import com.workhub.model.PictureElementModel;
import com.workhub.model.TextElementModel;
import com.workhub.utils.Constants;

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
	
	
	private boolean findClientAgent(AID agent){
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.ELEMENT_AGENT);
		sd.setName(agent.toString());
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			if (result.length > 0) {
				return true;
			}
		}
		catch(FIPAException fe) {}
		return false;
	}
	
	@Override
	protected void setup() {
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			
			int typeModel = (Integer) args[0];
			
			switch (typeModel) {
				case Constants.TYPE_ELEMENT_TEXT:
					TextElementModel text = new TextElementModel(255, "postit text", this.getAID(), "init");
					this.setContentModel((ElementModel)text); 
					this.type = Constants.TYPE_ELEMENT_TEXT;
				
				/*case Constants.TYPE_ELEMENT_FILE :
					FileElementModel file = new FileElementModel(255, "postit file", this.getAID(), null);
					this.setContentModel((ElementModel)file);
				*/
				
				case Constants.TYPE_ELEMENT_LINK :
					LinkElementModel link = new LinkElementModel(255, "postit link", this.getAID(), null);
					this.setContentModel((ElementModel)link); 
					this.type = Constants.TYPE_ELEMENT_LINK;
				
				case Constants.TYPE_ELEMENT_PICTURE :
					PictureElementModel pic = new PictureElementModel(255, "postit image", this.getAID(), null);
					this.setContentModel((ElementModel)pic);
					this.type = Constants.TYPE_ELEMENT_PICTURE;
				
			}
			
			
		}
		
		subscribeDFAgent();
		this.addBehaviour(new ContentElementBehaviour());
		this.addBehaviour(new EraseElementBehaviour());
		
	}
	 
	public void fireModelUpdate(){
		//TODO 
		//message a tous les clients informant la mise a jour du modele.
	}
	public AID getEditor() {
		return editor;
	}
	private void setEditor(AID editor) {
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
