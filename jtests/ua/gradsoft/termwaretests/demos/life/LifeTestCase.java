/*
 * Life1Main.java
 *
 */

package ua.gradsoft.termwaretests.demos.life;

import junit.framework.TestCase;
import ua.gradsoft.termware.*;


/**
 *Main class for test life game.
 * @author  Ruslan Shevchenko
 */
public class LifeTestCase extends TestCase
{
    
    /** Creates a new instance of Life1Main */
    public LifeTestCase(String name) {
        super(name);
    }
    
    public void setUp() throws TermWareException
    {
        TermWare.getInstance().init();
        life1System_=TermWare.getInstance().getOrCreateDomain("examples").resolveSystem("Life1");   
        life2System_=TermWare.getInstance().getDomain("examples").resolveSystem("Life2");
        FieldModel fieldModel1=new FieldModel(60,70);
        Life1TestFacts facts1=new Life1TestFacts();           
        facts1.setFieldModel(fieldModel1);
        life1System_.setFacts(facts1);
        FieldModel fieldModel2=new FieldModel(60,70);
        Life2TestFacts facts2=new Life2TestFacts();           
        facts2.setFieldModel(fieldModel2);
        life2System_.setFacts(facts2);
    }

 
    
    /**
     * main function
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LifeTestCase app = new LifeTestCase(LifeTestCase.class.getName());
        try {
            app.run(args);
        }catch(TermWareException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
    public void run(String[] args) throws TermWareException
    {
      setUp();     
      testConfiguration1system1();
      
      
    }
    
    public void initConfiguration1(AbstractTestLifeFacts facts)
    {
      FieldModel fieldModel=facts.getFieldModel();
      fieldModel.setCell(10,10,true);
      fieldModel.setCell(10,11, true);
      fieldModel.setCell(10,12, true);
      facts.setMaxNGenerations(1);
    }
    
    public void checkConfiguration1(AbstractTestLifeFacts facts)
    {
      FieldModel fieldModel=facts.getFieldModel();
      assertTrue(fieldModel.getCell(10,10)==false);
      assertTrue(fieldModel.getCell(10,11)==true);
      assertTrue(fieldModel.getCell(10,12)==false);
    }
    
    public void testConfiguration1system1() throws TermWareException
    {
        AbstractTestLifeFacts life1Facts=(AbstractTestLifeFacts)life1System_.getFacts();
       // life1System_.setLoggingMode(true);
       // life1System_.setLoggedEntity("Rules");
        initConfiguration1(life1Facts);
        Term t = life1Facts.generateStateTermFromNextFieldModel();
        life1System_.reduce(t);
        checkConfiguration1(life1Facts);
    }
    
    public void testConfiguration1system2() throws TermWareException
    {
        AbstractTestLifeFacts life2Facts=(AbstractTestLifeFacts)life2System_.getFacts();
        initConfiguration1(life2Facts);
        Term t = life2Facts.generateStateTermFromNextFieldModel();
        life2System_.reduce(t);
        checkConfiguration1(life2Facts);
    }
    
    
    private TermSystem life1System_;
    private TermSystem life2System_;

    private static final int MAX_NGENERATIONS=10000;
    
}
