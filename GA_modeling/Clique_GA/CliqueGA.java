import java.util.ArrayList;
import java.util.Arrays;

public class CliqueGA {
	int chromes=1000;								//even numbers only
	int generations=100;							//even numbers only
	int supergen=1;									//even numbers only
	Chromosome pop[], pop2[]=new Chromosome[0];		//stores population
	Chromosome pops[], pops2[];
	String filename;
	Graph graph=new Graph();
	int gencount=0;
	int k, gsize, hf;								//problem parameters
	Best best=new Best(k);							//current best solution
	int min,max,dif=10;
	double crossprop=0.8;							//crossover probability
	double mutprop=0.1;								//mutation probability
	Codec cod=new Codec();							//edge vector representation codec
	
	boolean heuristic=false;						//set to true when a heuristic is used
	boolean success=false;							//set to true when a clique is found

	ArrayList<ArrayList<Integer>> elist;			//used in population generation
	int[] exresult= {1};							//used in heuristic search
	boolean loadex=false;							//used in heuristic search
	boolean verbose=true;							//when false only the best fitness is printed
	
	public CliqueGA(int chroms, int gnrs, int sgnrs, String filename, int k){
		this.filename=filename;
		this.generations=gnrs;
		this.supergen=sgnrs;
		this.chromes=chroms;
		this.pop=new Chromosome[chromes];
		this.k=k;
	}
	
	public CliqueGA(String filename, int k, int chromes, int generations, int supergen, double  crossprop, double mutprop) {
		this.filename=filename;
		this.k=k;
		this.chromes=chromes;
		this.generations=generations;
		this.supergen=supergen;
		this.crossprop=crossprop;
		this.mutprop=mutprop;
		this.pop=new Chromosome[chromes];
	}
	
	public CliqueGA(String filename, int k) {
		this.filename=filename;
		this.k=k;
		this.pop=new Chromosome[chromes];
		
	}
	
	public CliqueGA(int k) {
		this.pop=new Chromosome[chromes];
		this.k=k;
	}
	
	public void setheur(boolean b) {
		heuristic=b;
	}
	
	public void init() {
		//sets and groups structure
		gsize=graph.graphsize();
		hf=k*(k-1)/2;
		System.out.println("Target fitness: "+hf);

		pops2=new Chromosome[supergen];
		for(int t=0;t<supergen;t++){
			pops=new Chromosome[supergen];

			for(int s=0;s<supergen && dif>0;s++){
				best=new Best(k);
				genpop();
				init2();
				superbest(s);
				
				if(success) {
					break;
				}
			}

			if(supergen>1 && !success) {
				best=new Best(k);
				pop=pops;
				init2();
				superbest2(t);
			}
		}
		
		if(supergen>1 && !success) {
			crossprop=1.0;
			best=new Best(k);
			pop=pops2;
			init2();
		}

		if(verbose) {
			if(success) {
				System.out.println("A clique is found!!!");
			}
			
			System.out.println("Best fitness: "+best.getbestfitness()+" in generation: "+best.getgencount());
			System.out.println("Output: "+Arrays.toString(best.getbestmapping()));
		}
		
		if(!verbose) {
			System.out.println(best.getbestfitness());
		}
	}
	
	public void init2(){
		//initialize genetic procedures
		genfit();
		for(int gs=0;gs<generations && dif>0;gs++){
			if(dif>0){
				crossover();
				mutation();
				selection();
				gencount++;
			}else {
				break;
			}
			
			if(success) {
				break;
			}
		}
	}
	
	private void selection(){
		//Selection
			ArrayList<Chromosome> newpop = new ArrayList<Chromosome>();//temp new pop storage
			newpop.clear();

		while(newpop.size()<chromes){

			double p = Math.random();
			double c=(double)min+(p*(double)dif);
			for(int sel=0;sel<pop.length;sel++){
				if(newpop.size()>(chromes-1)){
				break;
				}else if(pop[sel].getfitness()>c){
					newpop.add(pop[sel]);
				}
			}
			
			for(int sel=0;sel<pop2.length;sel++){
				if(newpop.size()>(chromes-1)){
				break;
				}else if(pop2[sel].getfitness()>c){
					newpop.add(pop2[sel]);
				}
			}	
		}
		
		pop=newpop.toArray(new Chromosome[chromes]);
		pop2=new Chromosome[0];
		//}
	}
	
	private void crossover(){
		//Crossover
		ArrayList<Chromosome> crosspop = new ArrayList<Chromosome>();
		for(int c=0;c<pop.length;c=c+2){
			double prop = Math.random();
			if(prop<=crossprop){
				double rplace = Math.random()*(pop[0].getmapping().length);
				int place=(int) (Math.round(rplace));
				String s1[]=pop[c].getstringmap();
				String s2[]=pop[c+1].getstringmap();
				Chromosome temp1= new Chromosome();
				Chromosome temp2= new Chromosome();
				temp1.strtomap(s1);
				temp2.strtomap(s2);

				for(int t1=0;t1<place;t1++){
					
					int temp=temp1.getmapping()[t1];
					temp1.change(t1,temp2.getmapping()[t1]);
					temp2.change(t1,temp);
				}
				
				if(checkcross(temp1.getmapping())){
					crosspop.add(temp1);
				}
				if(checkcross(temp2.getmapping())){
					crosspop.add(temp2);
				}
			}
		}	
		pop2=crosspop.toArray(new Chromosome[crosspop.size()]);
		genfit1(pop2);
		//}
	}
	
	private boolean checkcross(int[] m){
		//check if crossover produces valid mappings
		boolean r=true;
		for(int m1=0;m1<m.length;m1++){
			for(int m2=(m1+1);m2<m.length;m2++){
				if(m[m1]==m[m2]){
					r=false;
					break;
				}
			}
		}
		return r;
	}
	
	private void mutation(){
		//Mutation
		ArrayList<Chromosome> newpop = new ArrayList<Chromosome>();
		newpop.clear();
		
		boolean m=false;
		for(int c=0;c<pop.length;c++){
			double prop1 = Math.random();
			if(prop1<=mutprop){
				double prop2 = Math.random();
				int p1=(int)(gsize*prop2);
				int p2=(int)(k*prop2);
				newpop.add(new Chromosome(pop[c].changem(p2, p1)));
				m=true;
			}
		}
		if(m){
			for(int in=0;in<pop2.length;in++){
				newpop.add(new Chromosome(pop2[in].getstringmap()));
			}
			pop2=newpop.toArray(new Chromosome[newpop.size()]);
			genfit1(pop2);
		}
	}

	
	public void genpop(){
		//generates population
		int d=0;
		
		if(d>(chromes/2)){
			d=(chromes/2);
		}
		
		//the population is randomly generated
		for(int g=d; g<chromes;g++){
			Chromosome map = new Chromosome(k, gsize);
			if(g<pop.length){
				pop[g]=map;
			}
		}
		
		if(exresult.length>1 && loadex) {
			pop[chromes-1].setmapping(exresult);

		}
	}
	
	public void superbest(int in){
		pops[in]=new Chromosome(best.getbestmapping());
	}
	
	public void superbest2(int in){
		pops2[in]=new Chromosome(best.getbestmapping());

	}
	
	public void genfit(){
		genfit1(pop);
		genfit1(pop2);
	}
	
	public void genfit1(Chromosome somepop[]){
		//compute fitness for each chromosome
		
		//delete fitness from pop array
		for(int i00=0;i00<somepop.length;i00++){
			somepop[i00].setfitness(0);
		}
		
		//compute fitness for pop
		for(int i1=0;i1<somepop.length;i1++){
			int[] c=somepop[i1].getmapping();
			int tt=0;
			for(int ind1=0;ind1<k;ind1++) {
				for(int ind2=(ind1+1);ind2<k;ind2++) {			
					if(graph.graph[cod.coder(c[ind1],c[ind2])]>0) {
						tt++;
					}
				}	
			}
			somepop[i1].setfitness(tt);
		}
		range();
		updatebest();
	}

	public int genchromefit(Chromosome h) {
		//compute fitness for a single chromosome
		int[] c=h.getmapping();
		int tt=0;
		for(int ind1=0;ind1<k;ind1++) {
			for(int ind2=(ind1+1);ind2<k;ind2++) {			
				if(graph.graph[cod.coder(c[ind1],c[ind2])]==1) {
					tt++;
				}
			}	
		}
		return tt;
	}
	
	private void range(){
		//compute the range in the fitness of the population
		min=pop[0].getfitness();
		max=pop[0].getfitness();
		for(int ip=0;ip<pop.length;ip++){
			if(pop[ip].getfitness()<min){
				min=pop[ip].getfitness();
			}
			if(pop[ip].getfitness()>max){
				max=pop[ip].getfitness();
			}
		}
		
		for(int ip2=0;ip2<pop2.length;ip2++){
			if(pop2[ip2].getfitness()<min){
				min=pop2[ip2].getfitness();
			}
			if(pop2[ip2].getfitness()>max){
				max=pop2[ip2].getfitness();
			}
		}
		dif=max-min;
		
	}
	
	public void updatebest(){
		//update best solution
		for(int b=0;b<pop.length;b++){
			if(best.getbestfitness()<pop[b].getfitness()){
				best.setbestmapping(new int[]{0,0});
				best.setbestfitness(pop[b].getfitness());
				best.setbestmapping(pop[b].getmapping());
				best.setgencount(gencount);
				printlastbest();
			}else if(best.getbestfitness()<0){
				best.setbestmapping(new int[]{0,0});
				best.setbestfitness(pop[b].getfitness());
				best.setbestmapping(pop[b].getmapping());
				best.setgencount(gencount);
				printlastbest();
			}
		}
		
		for(int b=0;b<pop2.length;b++){
			if(best.getbestfitness()<pop2[b].getfitness()){
				best.setbestmapping(new int[]{0,0});
				best.setbestfitness(pop2[b].getfitness());
				best.setbestmapping(pop2[b].getmapping());
				best.setgencount(gencount);
				printlastbest();
			}else if(best.getbestfitness()<0){
				best.setbestmapping(new int[]{0,0});
				best.setbestfitness(pop2[b].getfitness());
				best.setbestmapping(pop2[b].getmapping());
				best.setgencount(gencount);
				printlastbest();
			}
		}
		
		if(best.getbestfitness()==hf) {
			success=true;
		}
	}
	
	public void printlastbest() {
		//print current best solution
		//System.out.println("|"+best.getbestfitness()+"|"+gencount);//+"| "+Arrays.toString(lastbest.getbestphenotype()));
	}
}
