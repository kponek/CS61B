public class NBody {
	/**Reads the text file to find the radius of the universe (second value in the file) */
	public static double readRadius(String file){
		In in = new In(file);
		in.readDouble();//to skip this value
		return in.readDouble();
	}
	/**Reads the number of planets in the text file and initializes all of them
		and returns an array */
	public static Planet[] readPlanets(String file){
		In in = new In(file);
		Planet[] p = new Planet[in.readInt()];
		in.readDouble();//to skip this value
		for (int i = 0; i < p.length; i++){
			p[i] = new Planet(in.readDouble(),in.readDouble(),
				in.readDouble(),in.readDouble(),in.readDouble(),in.readString());
		}
		return p;
	}
	/**main */
	public static void main(String[] args){
		/**Collecting all needed input */
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = (args[2]);
		double radius = readRadius(filename);
		//System.out.println(radius);//test
		Planet[] planets = readPlanets(filename);
		/**Drawing the background */
		StdDraw.setScale(-1 * radius, radius);
		StdDraw.picture(0,0,"images/starfield.jpg");
		/**Drawing all of the planets */
		for (int i = 0; i < planets.length; i++){
			planets[i].draw();
			//System.out.print(planets[i].xxPos + "," + planets[i].yyPos + "\n");//test
		}
		/**Creating an animation */
		double time = 0;
		double[] xForces = new double[planets.length];
		double[] yForces = new double[planets.length];
		//StdAudio.loop("audio/2001.mid");
		while (time < T) {
			for (int i = 0; i < planets.length; i++){
				xForces[i] =  planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}
			for (int i = 0; i < planets.length; i++){
				planets[i].update(dt,xForces[i],yForces[i]);
			}
			StdDraw.picture(0,0,"images/starfield.jpg");
			for (int i = 0; i < planets.length; i++){
				planets[i].draw();
			}
			StdDraw.show(10);
			time += dt;
		}
		/**Printing the universe*/
		/* Wrong way
		System.out.println(planets.length);
		System.out.println(radius);
		for (int i = 0; i < planets.length; i++){
			System.out.println(planets[i].xxPos + " " + planets[i].yyPos + " " + planets[i].xxVel
				+ " " + planets[i].yyVel + " " + planets[i].mass + " " + planets[i].imgFileName);*/
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		   		planets[i].xxPos, planets[i].yyPos, planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);	
		}	
		
	}
}
