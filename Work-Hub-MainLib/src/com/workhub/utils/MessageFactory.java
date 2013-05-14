package com.workhub.utils;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class MessageFactory {
	public final static int IS_EDITING = 1000;
	
	
	public static ACLMessage createMessage(Agent sender, AID receiver, int MessageType){
		
		
		int performatif;
		String content = "message vide";
		int action;
		
		switch (MessageType) {
		case IS_EDITING:
			performatif = ACLMessage.QUERY_IF;
			//TODO
			
			break;
		default:
			System.err.println("Type de message invalide");
			return null;
		}
		
		ACLMessage message = new ACLMessage(performatif);
		message.setContent(content);
		message.setSender(sender.getAID());
		message.addReceiver(receiver);
		return message;
		
	}
	
	
	
	
}
