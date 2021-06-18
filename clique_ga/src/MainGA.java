import java.io.File;
import java.util.Scanner;

public class MainGA {

	int k=12;
	int chromes=10;
	int generations=10;
	int supergen=10;
	double crossprop=1;
	double mutprop=1;
	
	public static void main(String[] args) {
		MainGA mga=new MainGA();
		
		mga.k=Integer.parseInt(args[0]);
		mga.chromes=Integer.parseInt(args[1]);
		mga.generations=Integer.parseInt(args[2]);
		mga.supergen=Integer.parseInt(args[3]);
		mga.crossprop=Integer.parseInt(args[4])/100;
		mga.mutprop=Integer.parseInt(args[5])/100;
		
		mga.readevfile();
		/*
		mga.readfiles();	//load all edge vector files from one directory run the algorithm to all
		mga.readfile();		//load file in DIMACS format and rin the algorithm
		mga.random();		//generate random graph
		*/
	}

	public void random() {
		int k=15;
		int n=10000;
		double d=0.5;

		int hf=k*(k-1)/2;
		System.out.println("Higher fitness: "+hf);
		
		CliqueGA ga=new CliqueGA(k);
		//create random EV graph with n nodes
		ga.graph.random(n, d);
		ga.setheur(false);
		ga.init();
	}
	
	public void readevfile() {
		File dir = new File("");
		String ev="graph.ev";
		
		if ( chromes % 2 == 1 ) {
			chromes++;
		}
		if ( generations % 2 == 1 ) {
			generations++;
		}
		if ( supergen % 2 == 1 ) {
			supergen++;
		}

		CliqueGA ga=new CliqueGA(dir+"\\"+ev, k,  chromes, generations, supergen, crossprop, mutprop);
		ga.graph.loadEdgeVector(dir+ev);
		ga.init();
	}
	
	public void readfiles() {
		String[] files;
		File dir = new File("C:\\path_dir");
		Graph g=new Graph();
		
		
		files = dir.list();

	    for (String ev : files) {	        	
	    	File f=new File(dir+"\\"+ev);
	        if(!f.isDirectory()) {
	        	if(ev.endsWith(".ev")) {
	        				
	        		String[] token1= ev.split("-");
	        		String[] token2=token1[1].split(".ev");
	        		int k = Integer.parseInt(token2[0]);
	        				
	        		System.out.println(ev+", k: "+k);

	        		CliqueGA ga=new CliqueGA(dir+"\\"+ev, k);
	        		ga.graph.loadEdgeVector(dir+"\\"+ev);
	        		
	        		ga.init();
	        		System.out.println("-------------------");
	        	}
	        }
	      }
	}
	
	public void readfile() {
		String filename;
		int k;

		System.out.println("Enter file name: ");
		Scanner input1 = new Scanner(System.in);

		filename="C:\\path\\"+"graph.clq";

		String[] s=filename.split("\\\\");
		System.out.println(s[s.length-1]);
		System.out.println("Enter number of k nodes in clique, k>1 and k<nodes: ");
		k = input1.nextInt();
		input1.close();
		
		int hf=k*(k-1)/2;
		System.out.println("Higher fitness: "+hf);
		
		CliqueGA ga=new CliqueGA(filename, k);
		ga.graph.loaddimacs(filename);
		ga.setheur(false);
		ga.init();
	}
}
