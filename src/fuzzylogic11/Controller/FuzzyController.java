package fuzzylogic11.Controller;

import fuzzylogic11.Model.Function;
import fuzzylogic11.Model.FuzzyVariable;
import fuzzylogic11.Model.TriangleFuzzySet;
import fuzzylogic11.View.FuzzyFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FuzzyController {
    
    private FuzzyVariable permintaan;
    private FuzzyVariable persediaan;
    private FuzzyFrame frame;
    private double demand;
    private double stock;
    private double[] permintaanEvaluate;
    private double[] persediaanEvaluate;
    
    public static void main(String[] args) {
        FuzzyController t = new FuzzyController();
      
    }

    public FuzzyController() {
        frame = new FuzzyFrame();
        initialiazeVariable();
        initializeListener();
        frame.setVisible(true);
    }
    
    
    
    private void initialiazeVariable(){
        permintaan = new FuzzyVariable();
        persediaan = new FuzzyVariable();
        
        permintaan.add(new TriangleFuzzySet(Double.NEGATIVE_INFINITY, 5000, 1000));
        permintaan.add(new TriangleFuzzySet(1000, Double.POSITIVE_INFINITY, 5000));
        
        persediaan.add(new TriangleFuzzySet(Double.NEGATIVE_INFINITY, 600, 100));
        persediaan.add(new TriangleFuzzySet(100, Double.POSITIVE_INFINITY, 600));
    }
    
    public void initializeListener(){
        frame.addTsukamotoButtonListener(new TsukamotoClick());
        frame.addSugenodButtonListener(new SugenoClick());
        frame.addMamdaniButtonListener(new MamdaniClick());
    }  
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    public void getUserInput(){
        demand = frame.getDemandValue();
        stock = frame.getStockValue();
    }
    
    public void evaluate(){
        permintaanEvaluate = permintaan.evaluate(demand);
        persediaanEvaluate = persediaan.evaluate(stock);
    }
    
    public void tsukamotoEvaluate(){
        System.out.println("fuzzylogic11.Controller.FuzzyController.tsukamotoEvaluate()");
        double[] rule = new double[4];
        double result = 0;
        
        rule[0] = 7000 - (Math.min(permintaanEvaluate[0], persediaanEvaluate[1])*5000);
        rule[1] = 7000 - (Math.min(permintaanEvaluate[0], persediaanEvaluate[0])*5000);
        rule[2] = (Math.min(permintaanEvaluate[1], persediaanEvaluate[1])*5000) + 2000;
        rule[3] = (Math.min(permintaanEvaluate[1], persediaanEvaluate[0])*5000) + 2000;
        
        result = (Math.min(permintaanEvaluate[0], persediaanEvaluate[1])*rule[0]) + (Math.min(permintaanEvaluate[0], persediaanEvaluate[0])*rule[1]) +
                (Math.min(permintaanEvaluate[1], persediaanEvaluate[1])*rule[2]) + (Math.min(permintaanEvaluate[1], persediaanEvaluate[0])*rule[3]);
        
        result = result / (Math.min(permintaanEvaluate[0], persediaanEvaluate[1]) + Math.min(permintaanEvaluate[0], persediaanEvaluate[0]) +
                Math.min(permintaanEvaluate[1], persediaanEvaluate[1]) + Math.min(permintaanEvaluate[1], persediaanEvaluate[0]));
        
        frame.appendCustomText("tsukamoto = "+ result +"kemasan\n");
        
    }
    
    public void sugenoEvaluate(){
        double[] rule = new double[4];
        double result = 0;
        
        if(Math.min(permintaanEvaluate[0], persediaanEvaluate[1]) > 0){
            rule[0] = demand - stock;
        }
        
        if(Math.min(permintaanEvaluate[0], persediaanEvaluate[0]) > 0){
            rule[1] = demand;
        }
        
        if(Math.min(permintaanEvaluate[1], persediaanEvaluate[0]) > 0){
            rule[2] = demand;
        }
        
        if(Math.min(permintaanEvaluate[1], persediaanEvaluate[1]) > 0){
            rule[3] = 1.25*demand - stock;
        }
        
        
        result = (Math.min(permintaanEvaluate[0], persediaanEvaluate[1])*rule[0]) + (Math.min(permintaanEvaluate[0], persediaanEvaluate[0])*rule[1]) +
                (Math.min(permintaanEvaluate[1], persediaanEvaluate[1])*rule[2]) + (Math.min(permintaanEvaluate[1], persediaanEvaluate[0])*rule[3]);
        
        result = result / (Math.min(permintaanEvaluate[0], persediaanEvaluate[1]) + Math.min(permintaanEvaluate[0], persediaanEvaluate[0]) +
                Math.min(permintaanEvaluate[1], persediaanEvaluate[1]) + Math.min(permintaanEvaluate[1], persediaanEvaluate[0]));
        
        frame.appendCustomText("Sugeno = "+ result +"kemasan\n");
    }
    
    public double trapezoidalArea(double a, double b , Function y ) 
    { 
        float n = 100;
        // Grid spacing 
        double h =  ((b - a) / n); 
      
        // Computing sum of first and last terms 
        // in above formula 
        double s = y.getAreaFunction(a) + y.getAreaFunction(b); 
      
        // Adding middle terms in above formula 
        for (int i = 1; i < n; i++) 
            s += 2 * y.getAreaFunction(a + i * h); 
      
        // h/2 indicates (b-a)/2n. Multiplying h/2 
        // with s. 
        return (h / 2) * s; 
    }
    
    public double trapezoidalMoment(double a, double b , Function y ) 
    { 
        float n = 100;
        // Grid spacing 
        double h =  ((b - a) / n); 
      
        // Computing sum of first and last terms 
        // in above formula 
        double s = y.getMomentFunction(a) + y.getMomentFunction(b); 
      
        // Adding middle terms in above formula 
        for (int i = 1; i < n; i++) 
            s += 2 * y.getMomentFunction(a + i * h); 
      
        // h/2 indicates (b-a)/2n. Multiplying h/2 
        // with s. 
        return (h / 2) * s; 
    }
    
    public void mamdaniEvaluate(){
        double[] rule = new double[4];
        double[] moment = new double[4];
        double[] area = new double[4];
        double result = 0;
        
        if(Math.min(permintaanEvaluate[0], persediaanEvaluate[1]) > 0){
            rule[0] = Math.min(permintaanEvaluate[0], persediaanEvaluate[1]);
        }
        
        if(Math.min(permintaanEvaluate[0], persediaanEvaluate[0]) > 0){
            rule[1] = Math.min(permintaanEvaluate[0], persediaanEvaluate[0]);   
        }
        
        if(Math.min(permintaanEvaluate[1], persediaanEvaluate[0]) > 0){
            rule[2] = Math.min(permintaanEvaluate[1], persediaanEvaluate[0]);
        }
        
        if(Math.min(permintaanEvaluate[1], persediaanEvaluate[1]) > 0){
            rule[3] = Math.min(permintaanEvaluate[1], persediaanEvaluate[1]);
        }
        
        
        double a1 = Math.max(rule[0], rule[1])*5000+2000;
        double a2 = Math.max(rule[2], rule[3])*5000+2000;
        
        Function f1 = new Function(Math.max(rule[0], rule[1]));
        Function f2 = new Function(2000, 5000);
        Function f3 = new Function(Math.max(rule[2], rule[3]));
        
        double moment1 = trapezoidalMoment(0, a1, f1);
        double moment2 = trapezoidalMoment(a1, a2, f2);
        double moment3 = trapezoidalMoment(a2, 7000, f3);
        
        double area1 = trapezoidalArea(0, a1, f1);
        double area2 = trapezoidalArea(a1, a2, f2);
        double area3 = trapezoidalArea(a2, 7000, f3);
        
        
        result = (moment1 + moment2 + moment3)/(area1 + area2 + area3);
        
        frame.appendCustomText("Mamdani centroid = "+ result +"kemasan\n");
    }
    
    
    
    private class TsukamotoClick implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            getUserInput();
            evaluate();
            tsukamotoEvaluate();
        }
        
    }
    
    private class SugenoClick implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            getUserInput();
            evaluate();
            sugenoEvaluate();
        }
        
    }
    
    private class MamdaniClick implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            getUserInput();
            evaluate(); 
            mamdaniEvaluate();
        }
        
    }
}
