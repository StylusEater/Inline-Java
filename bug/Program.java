import org.perl.inline.java.*;

public class Program {

    // Grundv�rden
	PerlObject him = new PerlObject("$him");

    // Konstruktor
    public Program() throws InlineJavaException {
    }
    // Metod som g�r n�got...
    public void work () {
       // H�r skrivs skriptet i Java!
	    
	   
       String T = (String)him.method("name");
    	if (T.equals("Nisse") )
			//him.method("name","Knut-G�ran");
			him.method("name", new Object[] {"Knut-G�ran" } );
		else
			him.method("name","Kurt-Arne");
		him.method("peers", new Object[] {"Tuve", "Bernt-Arne", "Nyman"} );
	    
	   
       // H�r slutar skriptet som skrivits i Java!
    }
}
