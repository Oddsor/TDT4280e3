package other;

import java.util.ArrayList;

public class TournamentLogger {
	
	ArrayList<String> tournamentLog = new ArrayList<String>();
	private static TournamentLogger singletonInstance;
	
	private TournamentLogger() {
		// TODO Auto-generated constructor stub
	}
	
	//Vet ikke om du har noen sterke meninger om singletons som design patterns
	//gjør det nå sånn i hvert fall =)
	public static TournamentLogger getTournamentLogger(){
		if( singletonInstance == null){
			singletonInstance = new TournamentLogger();
		}
		return singletonInstance;
	}
	
	public void log(String string){
		tournamentLog.add(string);
	}
	
	public void printLog(){
		for(String s : tournamentLog){
			System.out.println(s);
		}
	}
	
}
