package fuzzylogic11.Model;
public class Function {
    private double a;
    private double minimal;
    private double interval;

    public Function(double a) {
        this.a = a;
    }

    public Function(double a1, double a2) {
        this.a = -2;
        this.minimal = a1;
        this.interval = a2;
    }
    
    public double getMomentFunction(double x){
        if(a == -2)
            return (x-minimal)*x/interval;
        
        return a*x;
    }
    
    public double getAreaFunction(double x){
        if(a == -2)
            return (x-minimal)/interval;
        
        return a;
    }
}
