public class Chromosome {
	private int mapping[];
	int edges,nodes;		//graph nodes and edges
	int root=0;
	private int fitness=-1;	//fitness for this mapping
	Codec cod= new Codec();

	public Chromosome(){
		this.fitness=0;
	}
		
	public Chromosome(int[] c){
		//initialize the object on given mapping
		mapping=c;
	}
	
	public Chromosome(String[] c){
		//initialize object on given mapping
		mapping= new int[c.length];
		for(int s=0;s<c.length;s++){
			mapping[s]=Integer.parseInt(c[s]);
		}
	}
	
	public Chromosome(int f, int[] m){
		//initialize the object on given mapping and fitness
		this.fitness=f;
		this.mapping=m;
	}
	
	public void strtomap(String[] c){
		//string to mapping
		mapping= new int[c.length];
		for(int s=0;s<c.length;s++){
			mapping[s]=Integer.parseInt(c[s]);
		}
	}
	
	public void setfitness(int newfit){
		fitness=newfit;
	}
	
	public void setroot(int r){
		root=r;
	}
	
	public int getroot(){
		return root;
	}
	
	public int[] getmapping(){
		int[] t=mapping;
		return t;
	}
	
	public String[] getstringmap(){
		//mapping to string
		String[] t= new String[mapping.length];
		for(int j=0;j<mapping.length;j++){
		t[j]=Integer.toString(mapping[j]);
		}
		return t;
	}
	
	public void change(int index, int value){
		//change one value of the mapping
		mapping[index]=value;
	}

	public void setmapping(int[] ma){
		mapping=ma;
	}
	
	public int getfitness(){
		return fitness;
	}
}
