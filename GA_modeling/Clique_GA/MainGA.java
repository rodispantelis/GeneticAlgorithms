import java.io.File;
import java.util.Scanner;

public class MainGA {

	File dir = new File("C:\\Files\\Graphs\\DIMACS\\DIMACS_test");
	
	public static void main(String[] args) {
		MainGA mga=new MainGA();
		
		mga.readdimacsfiles();	
		//load all edge vector files from one directory run the algorithm to all

	}

	
	public void readdimacsfiles() {
		String[] files;

		files = dir.list();

	   for (String ev : files) {
		//String ev=files[0].toString();
	    	File f=new File(dir+"\\"+ev);
	        if(!f.isDirectory()) {
	        	if(ev.endsWith(".clq")) {
	        		String[] token1= ev.split("-");
	        		String[] token2=token1[1].split(".clq");
	        		int k = Integer.parseInt(token2[0]);
	        		System.out.println(ev+", k: "+k);
	        		CliqueGA ga=new CliqueGA(dir+"\\"+ev, k);
	        		ga.graph.loaddimacs(dir+"\\"+ev);
	        		ga.init();
	        		System.out.println("-------------------");
	        	}
	        }
	      }
	}
}
