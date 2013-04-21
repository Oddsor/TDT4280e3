import java.util.Arrays;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.domain.*;
import java.util.List;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class ControllerAgent extends Agent {
    
    public HashMap<AID, double[]> agentInfo;
    
    protected void setup() {
	registerService(this);
        agentInfo = new HashMap<AID, double[]>();
        List<AID> others = getOtherAgents(this);
        for(AID other: others){
            agentInfo.put(other, new double[3]);
        }
        addBehaviour(new ObstacleAvoidanceBehaviour(this));
	//addBehaviour(new MoveToNearestBehaviour(this));
    }

    protected void takeDown() {}
	
	public static void registerService(Agent myself) {
        DFAgentDescription dfa = new DFAgentDescription();
        dfa.addLanguages("TRADE");
        dfa.addOntologies("trade");
        try {
            DFService.register(myself, dfa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	public static List<AID> getOtherAgents(Agent myself){
        List<AID> agents = new ArrayList<AID>();
        
        DFAgentDescription dfa = new DFAgentDescription();
        dfa.addLanguages("TRADE");
        dfa.addOntologies("trade");
        
        DFAgentDescription[] results = null;
        try {
            results = DFService.search(myself, dfa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < results.length; i++){
            if(!results[i].getName().equals(myself.getAID())){
                agents.add(results[i].getName());
            }
        }
        return agents;
    }
	
	public static int[] objectRecognition(int[][] image){
	int[] objects = {0, 0, 0, 0, 0, 0};
	for(int x = 0; x < image.length; x++){
		for(int y = 0; y < image[x].length; y++){
			if(image[x][y] == 0) objects[0]++;
			else if(image[x][y] == 1) objects[1]++;
			else if(image[x][y] == 2) objects[2]++;
			else if(image[x][y] == 3) objects[3]++;
			else if(image[x][y] == 4) objects[4]++;
			else if(image[x][y] == 5) objects[5]++;
		}
	}
	
	return objects;
	}
	
	public static double boundSpeed(double speed){
		return Math.max(-100.0, Math.min(100.0, speed));
	}
        
        public boolean sameBearingAsNearFriend(double[] position, double bearing){
            for(double[] agentVals: agentInfo.values()){
                double normal = Math.abs(agentVals[2] - bearing);
                if(normal >= 345 || normal <= 10){
                    //Similar heading, check proximity.
                    if(agentVals[0] - position[0] < Math.abs(0.1) && agentVals[1] - position[1] < Math.abs(0.1)){
                        return true;
                    }
                }
            }
            return false;
        }
        public boolean nearFriend(double[] position){
            for(double[] agentVals: agentInfo.values()){
                if(Math.abs(agentVals[0] - position[0]) < 0.1 && Math.abs(agentVals[1] - position[1]) < 0.1){
                    return true;
                }
            }
            return false;
        }
}

class ObstacleAvoidanceBehaviour extends CyclicBehaviour {
    private ControllerAgent myAgent;
    private boolean turnRight = false;
    private int counter;

    public ObstacleAvoidanceBehaviour(Agent a) {
	this.myAgent = (ControllerAgent) a;
    }

    public void action() {
	ACLMessage msg = myAgent.receive(); 
        
	if (msg != null) { 
            Set<AID> otherAgents = myAgent.agentInfo.keySet();
            for(AID other: otherAgents){
                if(other.getLocalName().equals(msg.getSender().getLocalName())){
                    String[] valsString = msg.getContent().split(", ");
                    double[] vals = new double[3];
                    vals[0] = new Double(valsString[0]).doubleValue();
                    vals[1] = new Double(valsString[1]).doubleValue();
                    vals[2] = new Double(valsString[2]).doubleValue();
                    myAgent.agentInfo.put(other, vals);
                    return;
                }
            }
	    String[] sensors = msg.getContent().split(", ");

	    double[] distance_sensors = { new Double(sensors[0]).doubleValue(), 
					  new Double(sensors[1]).doubleValue() };

	    double[] position = { new Double(sensors[2]).doubleValue(), 
				  new Double(sensors[3]).doubleValue() };

	    double bearing = new Double(sensors[4]).doubleValue();

            ACLMessage broadcast = new ACLMessage(ACLMessage.PROPOSE);
            for(AID other: otherAgents){
                broadcast.addReceiver(other);
            }
            broadcast.setContent(position[0] + ", " + position[1] + ", " + bearing);
            myAgent.send(broadcast);
            
	    int camera_width = Integer.parseInt(sensors[5]);
	    int camera_height = Integer.parseInt(sensors[6]);
	    
	    int[][] image = new int[camera_width][camera_height];
	    for(int x = 0; x < camera_width; x++)
		for(int y = 0; y < camera_height; y++)
		    image[x][y] = Integer.parseInt(sensors[camera_width*y + x + 7]);
		
	    /*
	     * Implement your behaviour here, by setting the motor speeds. Below is a simple obstacle
	     * avoidance behaviour. The maximum speed of the robot motors is [-100,100], this is limited
	     * on the Webots side.
	     */
		
	    double distance_delta = distance_sensors[0] - distance_sensors[1];
//		if (distance_delta != 0.0){
//			//AVOID OBJECTS
//			double[] motor_speeds = {0.0, 0.0};
//			motor_speeds[0] = 100/2+distance_delta;
//			motor_speeds[1] = 100/2-distance_delta;
//
//			ACLMessage reply = msg.createReply(); 
//			reply.setContent(motor_speeds[0] + ", " + motor_speeds[1]);
//			myAgent.send(reply);
//			return;
//		}
                int[][] leftImg = Arrays.copyOfRange(image, 0, (int) camera_width/2);
                int[][] rightImg = Arrays.copyOfRange(image, (int) camera_width/2, camera_width);
		int[] left = ControllerAgent.objectRecognition(leftImg);
                int[] right = ControllerAgent.objectRecognition(rightImg);
                double gold_delta = (500.0 * ((left[1] + left[3]) - (right[1] + right[3]))) / (camera_height*camera_width);
                
                //System.out.println((double) (left[5] + right[5]) / (double) (camera_height*camera_width));
                if ((double) (left[5] + right[5]) / (double) (camera_height*camera_width) > 0.3){
                    System.out.println("Friendly in front!");
                    if(counter < 0){
                        counter = 10;
                        turnRight = !turnRight;
                    }
                }
                if(gold_delta != 0.0 && distance_delta == 0.0){
                    double[] motor_speeds = {0.0, 0.0};
                    motor_speeds[0] = ControllerAgent.boundSpeed(100.0 - gold_delta);
                    motor_speeds[1] = ControllerAgent.boundSpeed(100.0 + gold_delta);
                    
                    ACLMessage reply = msg.createReply(); 
                    reply.setContent(motor_speeds[0] + ", " + motor_speeds[1]);
                    myAgent.send(reply);
                    counter--;
                    return;
                }
                
                double[] motor_speeds = new double[2];
                if (turnRight){ 
                    motor_speeds[0] = -35.0;
                    motor_speeds[1] = 35.0;
                }else{
                    motor_speeds[0] = 35.0;
                    motor_speeds[1] = -35.0;
                }
                ACLMessage reply = msg.createReply(); 
                reply.setContent(motor_speeds[0] + ", " + motor_speeds[1]);
                myAgent.send(reply);
                counter--;
                return;
	}
	else
	    block();
    }
}