import java.util.ArrayList;
import java.util.Arrays;

public class CliqueGA {
	int chromes=200;								//even numbers only
	int generations=200;							//even numbers only
	int supergen=10;								//even numbers only
	Chromosome pop[], pop2[]=new Chromosome[0];		//stores population
	Chromosome pops[], pops2[];
	String filename;
	Graph graph= new Graph();
	int k, gsize, hf;								//problem parameters
	Best best=new Best(k);							//current best solution
	int min,max,dif=10;
	double crossprop=1;								//crossover probability
	double mutprop=1;								//mutation probability
	Codec cod= new Codec();							//edge vector representation codec
	
	boolean heuristic=false;						//set to true when a heuristic is used

	ArrayList<ArrayList<Integer>> elist;			//used in population generation
	int[] exresult= {1};							//used in heuristc search
	boolean loadex=false;							//used in heuristc search
	boolean verbose=false;							//when false only the best fitness is printed
	
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
	
	private void heuristic(){
		if(heuristic) {
			//add a heuristic for population generation
		}
	}
	
	public void init_exp() {//experimental

		gsize=graph.graphsize();
		long startTime = System.nanoTime();
		
		hf=k*(k-1)/2;

		pops2=new Chromosome[generations];
		pops=new Chromosome[generations];
		genpop();
		for(int s=0;s<generations;s++){
			best=new Best(gsize);
			genpop();
			init2_alt();
			superbest(s);
		}

		pop=pops;
		genfit();
		
		System.out.println("Target fitness: "+hf);
		System.out.println("Last best fitness: "+best.getbestfitness());

		//Print running time
		long endTime   = System.nanoTime();
		long totalTime = (endTime - startTime)/1000000000;
		System.out.println("Running time: "+totalTime+" seconds");
	}
	
	public void init2_alt(){//experimental
		//genetic procedures
		genfit();
		crossover();
		mutation();
		selection();
	}
	
	public void init() {
		//teams and groups structure
		
		gsize=graph.graphsize();

		long startTime = System.nanoTime();
		
		hf=k*(k-1)/2;

		pops2=new Chromosome[supergen];
		for(int t=0;t<supergen;t++){
				pops=new Chromosome[supergen];
				//Stage#1
				for(int s=0;s<supergen;s++){
				
					best=new Best(k);
					genpop();
					init2();
					superbest(s);
				}
					//Stage#2
					best=new Best(k);
					pop=pops;
					init2();
					superbest2(t);
		}
		
		//Stage#3
		best=new Best(k);
		pop=pops2;
		init2();

		long endTime   = System.nanoTime();
		long totalTime = (endTime - startTime)/1000000000;
		
		if(verbose) {
			if(best.getbestfitness()==hf) {
				System.out.println("A clique is found!!!");
			}
			System.out.println("Target fitness: "+hf);
			System.out.println("Last best fitness: "+best.getbestfitness());
			System.out.println("Last best mapping: "+Arrays.toString(best.getbestmapping()));
			System.out.println("Running time: "+totalTime+" seconds");
		}
		
		if(!verbose) {
			System.out.println(best.getbestfitness());
		}
	}
	
	public void init2(){
		//initialize genetic procedures
		genfit();

		for(int gs=0;gs<generations;gs++){	
			if(dif<1){
				//Do nothing the population has converged
			}else{
				crossover();
				mutation();
				selection();
			}
		}
	}
	
	private void selection(){
		//Selection
		if(dif>0){
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
		}
	}
	
	private void crossover(){
		//Crossover
		if(dif>0){
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
		genfit2();
		}
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
			genfit2();
		}
	}

	
	public void genpop(){
		//generates population
		//the first chromes are generated using heuristics, if one is defined
		int d;
		
		if(!heuristic) {
			d=0;
		}
		else if((chromes/2) <elist.size()) {
			d=chromes/2;
		}else {
			d=elist.size();
		}
		
		if(d>(chromes/2)){
			d=(chromes/2);
		}
		//System.out.println(d);
		int t0=0;
		if(heuristic){
			for(int in1=0;in1<elist.size() && t0<d;in1++) {
			
				if(elist.get(in1).size()>(k-1)) {
					for(int in2=0;in2<elist.get(in1).size() && t0<d;in2++) {
						int[] m=new int[k];
						for(int in3=0;in3<k;in3++) {
							m[in3]=elist.get(in2).get(in3);
						}
						pop[t0]= new Chromosome(m);
						t0++;
					}
						
				}
			}
		}
			
		//the rest of the population is randomly generated
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
		genfit1();
		genfit2();
	}
	public void genfit1(){
		//compute fitness for each chromosome
		
		//delete fitness from pop array
		for(int i00=0;i00<pop.length;i00++){
			pop[i00].setfitness(0);
		}
		
		//compute fitness for pop
		for(int i1=0;i1<pop.length;i1++){
			int[] c=pop[i1].getmapping();
			int tt=0;
			for(int ind1=0;ind1<k;ind1++) {
				for(int ind2=(ind1+1);ind2<k;ind2++) {			
					if(graph.graph[cod.coder(c[ind1],c[ind2])]==1) {
						tt++;
					}
				}	
			}
			pop[i1].setfitness(tt);
		}

		range();
		updatebest();
	}
	
	public void genfit2(){
		//compute and fitness for each chromosome
		
		//delete fitness from pop array
		for(int i01=0;i01<pop2.length;i01++){
			pop2[i01].setfitness(0);
		}
		
		//compute fitness for pop2
		for(int i3=0;i3<pop2.length;i3++){

				int[] c=pop2[i3].getmapping();
				int tt=0;
				for(int ind1=0;ind1<k;ind1++) {
					for(int ind2=(ind1+1);ind2<k;ind2++) {			
						if(graph.graph[cod.coder(c[ind1],c[ind2])]>0) {
							tt++;
						}
					}	
				}
				pop2[i3].setfitness(tt);
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
			}else if(best.getbestfitness()<0){
				best.setbestmapping(new int[]{0,0});
				best.setbestfitness(pop[b].getfitness());
				best.setbestmapping(pop[b].getmapping());
			}
		}
		
		for(int b=0;b<pop2.length;b++){
			if(best.getbestfitness()<pop2[b].getfitness()){
				best.setbestmapping(new int[]{0,0});
				best.setbestfitness(pop2[b].getfitness());
				best.setbestmapping(pop2[b].getmapping());
			}else if(best.getbestfitness()<0){
				best.setbestmapping(new int[]{0,0});
				best.setbestfitness(pop2[b].getfitness());
				best.setbestmapping(pop2[b].getmapping());
			}
		}
	}
}
