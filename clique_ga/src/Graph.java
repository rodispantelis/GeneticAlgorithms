import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
	//load and store graph
	int edges,nodes;
	int graph[],nodew[];
	Codec codec= new Codec();

	public void random(int n, double d) {
		//generate random graph
    	nodes=n;
    	edges=nodes*(nodes-1)/2;
    	
		graph= new int[edges];
		for(int i=0;i<edges;i++) {
			if(Math.random()>=d) {
				graph[i]=1;
			}else {
				graph[i]=0;
			}
		}
		System.out.println("Random graph generated");
		
		exportgraph("random.EV");
	}
	
	public void loaddimacs(String filename){
		//load graph in DIMACS format from file
		ArrayList<Integer> temp1 = new ArrayList<>();
		ArrayList<Integer> temp2 = new ArrayList<>();
		
		int max=1;

		try{
			File file = new File (filename);
	    	Scanner scanner = new Scanner(file);

	    	while(scanner.hasNext()){
	    		String[] tokens= scanner.nextLine().split(" ");
	    		if(tokens[0].equals("e")){
	    	
	    			int t1 = Integer.parseInt(tokens[1]);
	    			int t2 = Integer.parseInt(tokens[2]);
	    		
	    			if(t1>max){
	    				max=t1;
	    			}else if(t2>max){
	    				max=t2;
	    			}
	    		
	    			temp1.add(t1);
	    			temp2.add(t2);
	    		}
	    		else{}
	    	}
	    
	    	scanner.close();
		    
	    	nodes=max;
	    	edges=nodes*(nodes-1)/2;
			graph=new int[edges];		
		}
		catch (IOException e) {
		       e.printStackTrace();
		   }
		
		for(int c1=0;c1<temp1.size();c1++){
			graph[codec.coder((temp1.get(c1)-1), (temp2.get(c1)-1))]=1;
		}

		System.out.println("DIMACS graph file read");
	}
	
	public int graphsize() {
		return nodes;
	}
	
	public void loadEdgeVector(String filename){		
		//load graph from file in edge vector format
		ArrayList<Integer> tempev = new ArrayList<>();
		int max=1;
		
		try{
			File file = new File (filename);
	    	Scanner scanner = new Scanner(file);

	    	while(scanner.hasNext()){
	    		String[] tokens= scanner.nextLine().split(",");
	    		if(tokens.length==1){
	    			int tev = Integer.parseInt(tokens[0]);
	    			tempev.add(tev);
	    		}
	    		else{
	    			
	    		for(int t=0;t<tokens.length;t++){
	    			int tev = Integer.parseInt(tokens[t]);
	    			tempev.add(tev);
	    		}
	    		}
	    	}
	    	scanner.close();
		    
	    	int nd[]=codec.decoder(1+tempev.size());
	    	
			if((nd[0]+1)>max){
				max=(nd[0]);
			}
			if((nd[1]+1)>max){
				max=(nd[1]);
			}
	    	
	    	nodes=max;
	    	edges=nodes*(nodes-1)/2;
			graph=new int[edges];
		}
		catch (IOException e) {
		       e.printStackTrace();
		   }
		for(int c1=0;c1<edges;c1++){
			graph[c1]=tempev.get(c1);
		}
	}
	
	
	public void exportgraph(String name){
		//exports the graph to a file
		PrintWriter mout;
		try {
			mout = new PrintWriter(name);
			String st="";
			st=java.util.Arrays.toString(graph);
			st = st.replaceAll("\\s","");
			int sz=st.length();
			st=st.substring(1, (sz-1));
			mout.append(st);
			mout.println("");
			mout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
