package ionshield.project2.main;

public class Evaporator {
    private double r = 2.26 * 1000000;//J/kg
    private double kT = 5000;//W/m^2
    private double f = 10;//m^2
    private double cT = 4187;//J/kg*deg
    private double tR = 90;//deg
    
    private double sigma = 0.12;//kg^0.5 m/s
    private double s = 0.75;//m^2
    private double p0 = 7900;//kg/m^2
    private double p1 = 7600;//kg/m^2
    
    private double totalMass;
    private double dryMass;
    private double time = 0;
    
    private double mSec;
    private double mOut;
    private double cOut;
    
    public Evaporator() {
    
    }
    
    public void init(double cIn, double mIn, double tP) {
        calculate(cIn, mIn, tP);
        totalMass = s * (Math.pow((mIn - mSec) / sigma, 2) + p1 - p0);
        dryMass = totalMass * cOut;
    }
    
    private void calculate(double cIn, double mIn, double tP) {
        mSec = (kT * f * (tP - tR)) / (r - cT * tR);
        mOut = mIn - mSec;
        cOut = mIn * cIn / mOut;
    }
    
    public void tick(double seconds, double cIn, double mIn, double tP) {
        double mSec = (kT * f * (tP - tR)) / (r - cT * tR);
        double mOut = sigma * Math.sqrt(p0 + totalMass / s - p1);
        
        double dDry = mIn * cIn - mOut * (dryMass / totalMass);
        double dTotal = mIn - mOut - mSec;
    
        cOut = dryMass / totalMass;
        this.mSec = mSec;
        this.mOut = mOut;
        
        dryMass += dDry * seconds;
        totalMass += dTotal * seconds;
        
        time += seconds;
    }
    
    public double getR() {
        return r;
    }
    
    public double getkT() {
        return kT;
    }
    
    public double getF() {
        return f;
    }
    
    public double getcT() {
        return cT;
    }
    
    public double gettR() {
        return tR;
    }
    
    public double getmSec() {
        return mSec;
    }
    
    public double getmOut() {
        return mOut;
    }
    
    public double getcOut() {
        return cOut;
    }
    
    public double getTime() {
        return time;
    }
}
