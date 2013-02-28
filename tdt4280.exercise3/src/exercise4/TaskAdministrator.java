package exercise4;

import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class TaskAdministrator extends AdministratorAgent {

	

	@Override
	void handleMessage(ACLMessage msg) {
		switch (msg.getPerformative()) {
		case ACLMessage.QUERY_REF:
				sendMessage(msg.getSender(), handleExpression(msg.getContent()));
			break;

		default:
			break;
		}

	}
	
	private String handleExpression(String expr){
		
		return expr;
		
	}

}
