package exercise4;


@SuppressWarnings("serial")
public class AdditionSolver extends SolverAgent {

	
	@Override
	String solve(double x, double y) {
		double result = x+y;
		return ""+result;
	}

}
