public class Chromosome {
	private int mapping[];
	int cnodes, gnodes;		//clique nodes, graph nodes
	private int fitness=0;	//fitness for this chromosome

	public Chromosome(){
		this.fitness=0;
	}
	
	public Chromosome(int cnodes, int gnodes) {
		//generates random chromosomes
		this.cnodes=cnodes;
		this.gnodes=gnodes;
		mapping=new int[cnodes];
		randommapping();
	}
		
	public Chromosome(int[] c){
		//initialize the object on given configuration
		mapping=c;
	}
	
	public Chromosome(String[] c){
		//initialize object on given configuration
		mapping= new int[c.length];
		for(int s=0;s<c.length;s++){
			mapping[s]=Integer.parseInt(c[s]);
		}
	}
	
	public Chromosome(int f, int[] m){
		//initialize the object on given configuration and fitness
		this.fitness=f;
		this.mapping=m;
	}
	
	public void strtomap(String[] c){
		//string to chromosome
		mapping= new int[c.length];
		for(int s=0;s<c.length;s++){
			mapping[s]=Integer.parseInt(c[s]);
		}
	}

	public void randommapping(){
		//generate random chromosome
		for(int ii=0;ii<mapping.length;ii++){
			mapping[ii]=-1;
		}
		for(int v=0;v<mapping.length;v++){
			int rand;
			do{
				double randomm = Math.random()*(gnodes);
				rand=(int) (Math.round(randomm)-1);
			}while(!chk(rand));
			mapping[v]=rand;
		}
	}
	
	private boolean chk(int rand){
		//check for duplicated values
		boolean result = true;
		for(int i=0;i<mapping.length;i++){
			if(mapping[i]==rand){
				result=false;
			}
		}
		return result;
	}
	
	public void setfitness(int newfit){
		fitness=newfit;
	}
	public int[] getmapping(){
		int[] t=mapping;
		return t;
	}
	public String[] getstringmap(){
		//mapping to string
		String[] t= new String[mapping.length];
		for(int k=0;k<mapping.length;k++){
			t[k]=Integer.toString(mapping[k]);
		}
		return t;
	}
	
	public void change(int index, int value){
		//change one value of the chromosome
		mapping[index]=value;
	}
	
	public String[] changem(int index, int value){
		//check for validity then change one value of the chromosome
		String[] tempm=getstringmap();
		if(check(value)){
			tempm[index]=Integer.toString(value);
		}
		return tempm;
	}
	
	public boolean check(int value){//check for duplicated values on the chromosome
		boolean result=true;
		for(int m=0;m<mapping.length;m++){
			if(value==mapping[m]){
				result=false;
				break;
			}
		}
		return result;
	}
	
	public void setmapping(int[] ma){
		mapping=ma;
	}
	
	public int getfitness(){
		return fitness;
	}
}
