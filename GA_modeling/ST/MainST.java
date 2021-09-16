import java.io.File;
import java.util.Arrays;

public class MainST {

	//directory containing the DIMACS files
	File dir = new File("C:\\Files\\Graphs\\DIMACS\\DIMACS_test");
	
	public static void main(String[] args) {
		MainST st=new MainST();
		st.readdimacsfiles();
	}
	
	public void readdimacsfiles() {
		System.out.println("-------------------");
		String[] files;
		
		files = dir.list();

	        for (String ev : files) {	

	        		File f=new File(dir+"\\"+ev);
	        		if(!f.isDirectory()) {
	        			if(ev.endsWith(".clq")) {
	        				System.out.println(ev);
	        				Graph g= new Graph();
	        				g.loaddimacs(dir+"\\"+ev);
	        				STGA ga=new STGA(g);
	        				ga.init();
	        				System.out.println("-------------------");
	        		}
	        	}
	        }
	}
}
