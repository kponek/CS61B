public class Planet {
	private double xxPos;
	private double yyPos;
	private double xxVel;
	private double yyVel;
	private double mass;
	private String imgFileName;
	/**Constructor for the Planet class that takes individual values and creates
		a new Planet class */
	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	/**Constructor for the Planet class that creates a copy of one Planet */
	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}
	/**Calculates the distance between two Planets */
	public double calcDistance(Planet p) {
		double dX = Math.max(xxPos,p.xxPos) - Math.min(xxPos,p.xxPos);
		double dY = Math.max(yyPos,p.yyPos) - Math.min(yyPos,p.yyPos);
		return Math.sqrt(dX*dX + dY*dY);
	}
	/**Calculates the net force of two Planets on each other */
	public double calcForceExertedBy(Planet p){
		double g = 6.67 * Math.pow(10,-11);
		return g * mass * p.mass / (calcDistance(p) * calcDistance(p));
	}
	/**Calculates the force in the x direction for two Planets */
	public double calcForceExertedByX(Planet p){
		//double g = 6.67 * Math.pow(10,-11);
		//double dX = Math.max(xxPos,p.xxPos) - Math.min(xxPos,p.xxPos);
		double dX = p.xxPos - xxPos;
		return calcForceExertedBy(p) * dX / calcDistance(p);

	}
	/**Calculates the force in the y direction for two Planets */
	public double calcForceExertedByY(Planet p){
		//double g = 6.67 * Math.pow(10,-11);
		//double dY = Math.max(yyPos,p.yyPos) - Math.min(yyPos,p.yyPos);
		double dY = p.yyPos - yyPos;
		return calcForceExertedBy(p) * dY / calcDistance(p);
	}
	/** Calculates the net force in the x direction of multiple Planets on this Planet */
	public double calcNetForceExertedByX(Planet[] p){
		double netForceX = 0;
		for (int i = 0; i < p.length; i++){
			if (!p[i].equals(this)) {
				netForceX += calcForceExertedByX(p[i]);
			}
		}
		return netForceX;
	}
	/** Calculates the net force in the y direction of multiple Planets on this Planet */
	public double calcNetForceExertedByY(Planet[] p){
		double netForceY = 0;
		for (int i = 0; i < p.length; i++){
			if (!p[i].equals(this)) {
				netForceY += calcForceExertedByY(p[i]);
			}
		}
		return netForceY;
	}
	/**Updates the velocities and positions in the x and y directions with new
		forces in the x and y directions applied for a period of time */
	public void update(double dt, double fX, double fY){
		double aX = fX/mass;
		double aY = fY/mass;
		xxVel += dt * aX;
		yyVel += dt * aY;
		xxPos += dt * xxVel;
		yyPos += dt * yyVel;
	}
	public void draw(){
		StdDraw.picture(xxPos,yyPos,"images/" + imgFileName);
	}
}
