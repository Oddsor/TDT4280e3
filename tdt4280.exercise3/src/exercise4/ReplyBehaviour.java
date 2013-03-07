/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise4;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Apparently we need to implement wakerbehaviour.
 * @author Odd
 */
public class ReplyBehaviour extends WakerBehaviour{

    SolverAgent a;
    ACLMessage msg;
    
    public ReplyBehaviour(Agent a, long timeout, ACLMessage msg) {
        super(a, timeout);
        this.a = (SolverAgent) a;
        this.msg = msg;
    }

    @Override
    protected void onWake() {
        String[] expression = msg.getContent().split(" ");
        double leftOperand = Double.parseDouble(expression[0]);
        double rightOperand = Double.parseDouble(expression[2]);
        
        a.sendMessage(msg.getSender(), a.solve(leftOperand, rightOperand), ACLMessage.INFORM);
        a.problemSolved(this);
    }
    
}
