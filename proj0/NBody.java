public class NBody {

	/* Return a double corresponding to the radius of the universe in that file */
	public static double readRadius(String filename){

		int nums = 0;
		double radius = 0;

		/* Start reading in planets.txt */
		In in = new In(filename);

		nums = in.readInt();
		radius = in.readDouble();
		return radius;
	}

	/* Return an array of Planets corresponding to the planets in the file */
	public static Planet[] readPlanets(String filename){

		int nums = 0;
		double radius = 0;

		/* Start reading in planets.txt */
		In in = new In(filename);

        nums = in.readInt();
		radius = in.readDouble();

		Planet [] planets = new Planet [nums];
		int i = 0;

		while(!in.isEmpty()){
			double xP = in.readDouble();
			double yP = in.readDouble();
			double xV = in.readDouble();
			double yV = in.readDouble();
			double m = in.readDouble();
			String img = in.readString();
			planets[i] = new Planet(xP, yP, xV, yV, m, img);
			i ++;
		}
		return planets;
	}

	public static void main(String[] args){

		double T =  Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Planet [] planets = readPlanets(filename);

		String imgloc = "images/";
		String bgdimg = "starfield.jpg";

		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0, 0, imgloc + bgdimg, 2 * radius, 2 * radius);
		StdAudio.play("audio/2001.mid");

		for (Planet p : planets){
			p.draw();
		}

		for (double t = 0; t < T; t += dt){
			double [] xForces;
			double [] yForces;
			xForces = new double[planets.length];
			yForces = new double[planets.length];
			for (int n = 0; n < planets.length; n++){
				xForces[n] = planets[n].calcNetForceExertedByX(planets);
				yForces[n] = planets[n].calcNetForceExertedByY(planets);				
			}			

			StdDraw.setScale(-radius, radius);
			StdDraw.picture(0, 0, imgloc + bgdimg, 2 * radius, 2 * radius);

			for (int n = 0; n < planets.length; n++){
				planets[n].update(dt, xForces[n], yForces[n]);
				planets[n].draw();
			}

			StdDraw.show(10);
		}

		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (Planet p : planets){
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
		}
	}

}
