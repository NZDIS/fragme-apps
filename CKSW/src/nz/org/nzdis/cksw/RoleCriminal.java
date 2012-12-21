package nz.org.nzdis.cksw;

public class RoleCriminal extends Role {

	public RoleCriminal() {
		super(Cksw.ROLE_CRIMINAL);
	}
	
	@Override
	public void perform(Agent agent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// Collect taxes
	}

	public static double value(Agent agent) {
		return 0.0;
	}

}
