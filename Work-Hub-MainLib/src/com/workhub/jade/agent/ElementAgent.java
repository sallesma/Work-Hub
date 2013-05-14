package com.workhub.jade.agent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.workhub.jade.behaviour.ContentElementBehaviour;
import com.workhub.model.ElementModel;
import com.workhub.utils.Constants;

public class ElementAgent extends Agent {
	
	
	 private AID editor = null;
	 private ElementModel contentModel;


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
		subscribeDFAgent();
		this.addBehaviour(new ContentElementBehaviour());
	}
	 
	public void fireModelUpdate(){
		//TODO 
		//message a tous les clients informant la mise a jour du modele.
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
	

	
}
