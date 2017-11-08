public class Planet {

	/* Initilize planet properties */
	double xxPos;
	double yyPos;
	double xxVel;
	double yyVel;
	double mass;
	String imgFileName;

    /* Planet constructor 1 */
	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

    /* Planet constructor 2 -- copy */
	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

    /* Calculate the distance between two planets */
	public double calcDistance(Planet p){
		double dx;
		double dy;
		double r;
		dx = p.xxPos - this.xxPos;
		dy = p.yyPos - this.yyPos;
		r = Math.sqrt(dx * dx + dy * dy);
		return r;
	}

	/* Calculate the force exerted on this planet by the given planet */
	public double calcForceExertedBy(Planet p){
		double g = 6.67 * Math.pow(10, -11); 
		double r;
		double f;
		r = this.calcDistance(p);
		f = g * this.mass * p.mass / (r * r);
		return f;
	}

    /* Calculate the force exerted in the X directions */
    public double calcForceExertedByX(Planet p){
    	double r;
    	double f;
    	double dx;
    	double fx;    	
    	r = this.calcDistance(p);
    	f = this.calcForceExertedBy(p);
    	dx = p.xxPos - this.xxPos;
    	fx = f * dx / r;
    	return fx;
    }

    /* Calculate the force exerted in the Y directions */
    public double calcForceExertedByY(Planet p){
    	double r;
    	double f;
    	double dy;
    	double fy;    	
    	r = this.calcDistance(p);
    	f = this.calcForceExertedBy(p);
    	dy = p.yyPos - this.yyPos;
    	fy = f * dy / r;
    	return fy;
    }

    /* Calculate the net X force exerted by all planets upon the current Planet */
    public double calcNetForceExertedByX(Planet[] planets){
    	double fXnet = 0;
    	for (Planet p : planets){
    		if (this.equals(p)){
    			continue;
    		}
    		fXnet += this.calcForceExertedByX(p);
    	}
    	return fXnet;
    }

    /* Calculate the net Y force exerted by all planets upon the current Planet */
    public double calcNetForceExertedByY(Planet[] planets){
    	double fYnet = 0;
    	for (Planet p : planets){
    		if (this.equals(p)){
    			continue;
    		}
    		fYnet += this.calcForceExertedByY(p);
    	}
    	return fYnet;
    }

    /*  Update the planet's position and velocity instance variables */
    public void update(double dt, double fX, double fY){
    	double aXnet, aYnet;
    	double vXnew, vYnew;
    	double pXnew, pYnew;
    	aXnet = fX / this.mass;
    	aYnet = fY / this.mass;
    	vXnew = this.xxVel + dt * aXnet;
    	vYnew = this.yyVel + dt * aYnet;
    	pXnew = this.xxPos + dt * vXnew;
    	pYnew = this.yyPos + dt * vYnew;
    	this.xxVel = vXnew;
    	this.yyVel = vYnew;
    	this.xxPos = pXnew;
    	this.yyPos = pYnew;	
    }

    public void draw(){
		StdDraw.picture(this.xxPos, this.yyPos, "images/"+this.imgFileName);
    }
}
