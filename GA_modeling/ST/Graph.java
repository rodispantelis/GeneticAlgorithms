import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Graph {
	
	//store graph in Edge Vector format
	int edges,nodes;
	int graph[];
	int dsize=0;
	Codec codec= new Codec();

	public int graphsize() {
		return nodes;
	}
	
	public void loaddimacs(String filename){
		//load graph in DIMACS format from file
		//and store in edge vector
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
}
