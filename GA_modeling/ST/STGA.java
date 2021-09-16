import java.util.ArrayList;
import java.util.Arrays;

public class STGA {
	int chromes=20 ;
	int generations=1000;
	Graph graph=new Graph();
	Best best;
	Best best2;
	int gsize;
	int gencount=0;
	Chromosome pop[], pop2[]=new Chromosome[0], pops[],pops2[];
	int min,max,dif=10, target;
	double crossprop=0.8;
	double mutprop=0.1;	
	Codec cod= new Codec();
	boolean success=false;
	
	public STGA(Graph g) {
		this.graph=g;
		this.pop=new Chromosome[chromes];
	}
	
	public void init() {
		//initialize the genetic algorithm
		gsize=graph.nodes;
		target=graph.nodes-1;
		System.out.println("Target: "+(graph.nodes-1));
		
		pops2=new Chromosome[1];

		pops=new Chromosome[1];

		best=new Best(gsize);
		genpop();
		init2();
		superbest(0);

		System.out.println("GA output:"+Arrays.toString(best.getbestmapping()));
		System.out.println("Fitness: "+best.getbestfitness());

	}
	
	public void init2(){
		//genetic procedures
		genfit();

		for(int gs=0;gs<generations;gs++){
				crossover();
				mutation();
				selection();
				gencount++;
			if(success) {
				break;
			}
		}
	}

	public void selection() {
		//selection
		if(dif>0){
			ArrayList<Chromosome> newpop = new ArrayList<Chromosome>();//temp new pop storage
			newpop.clear();

		while(newpop.size()<chromes){

			double p = Math.random();
			double c=(double)min+(0.8*(double)dif);
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
	public void crossover() {
		//crossover
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
				}
			}
			pop2=crosspop.toArray(new Chromosome[crosspop.size()]);
			genfit1(pop2);
	}

	public void mutation() {
		//mutation
		ArrayList<Chromosome> newpop = new ArrayList<Chromosome>();
		newpop.clear();
		
		boolean m=false;
		for(int c=0;c<pop.length;c++){
			double prop0 = Math.random();
			String d[]=pop[c].getstringmap();
			if(prop0<mutprop){		
				Chromosome tc=new Chromosome(d);
				double prop1 = Math.random();
				double prop2 = Math.random();
				int p1=(int)((gsize-1)*prop1);
				int p2=(int)((gsize-1)*prop2);
				if(p1>0 && p1!=p2 && cod.coder(p1, p2)>0) {
					tc.change(p1, p2);
						newpop.add(tc);
						m=true;
				}
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

	public void superbest(int in){
		pops[in]=new Chromosome(best.getbestmapping());
	}
	
	public void superbest2(int in){
		pops2[in]=new Chromosome(best.getbestmapping());
	}
	
	public void genpop() {
		pop=new Chromosome[chromes];
		for(int g=0; g<pop.length;g++){
			Chromosome map = new Chromosome(randommapping());
			pop[g]=map;
		}
	}
	
	
	public int[] randommapping(){
		//generate random chromosome
		int mapping[]=new int[graph.nodes-1];
		
		for(int v=0;v<mapping.length;v++){
			boolean f=false;
			do {
				double randomm = Math.random()*(graph.nodes);
				int rand=(int) Math.round(randomm);
				
				if(rand==graph.nodes) {
					rand--;
				}
				
				if(v!=rand) {
					if(graph.graph[cod.coder(v, rand)]>0) {
						mapping[v]=rand;
						f=true;
					}
				}
			}while(!f);
		}
		return mapping;
	}
	
	public void printchromes() {
		for(int p=0;p<pop.length;p++) {
			System.out.println(Arrays.toString(pop[p].getmapping()));
		}
	}
	
	public void genfit(){
		genfit1(pop);
		genfit1(pop2);
		
	}
	public void genfit1(Chromosome somepop[]){
		//compute fitness
		
		//delete fitness from pop array
		for(int i00=0;i00<somepop.length;i00++){
			somepop[i00].setfitness(0);
		}
		
		//compute fitness for each chromosome of the population
		for(int i1=0;i1<somepop.length;i1++){
			genefitness(somepop[i1]);
		}
		range();
		updatebest();
	}
	
	public int genefitness(Chromosome h) {
		int tt=0;
		int[] m=h.getmapping();
		
		int[] res=new int[graph.edges];
		for(int r=0;r<m.length;r++) {
			res[cod.coder(r, m[r])]=1;
		}
		
		Connectivity con=new Connectivity(res);

		tt=con.check0();
		
		h.setfitness(tt);
		return tt;
	}
	
	public int genefitnessOld(Chromosome h) {
		int[] c=new int[h.getmapping().length+1];
		for(int i0=0;i0<(c.length-1);i0++) {
			c[i0]=h.getmapping()[i0];
		}
		c[c.length-1]=-1;

		int tt=0;
		boolean notconti=false;
	
		do {
			for(int i1=0;i1<c.length;i1++) {
				if(c[i1]>(-1)) {
					boolean found=false;
					for(int i2=0;i2<c.length;i2++) {
						if(c[i2]==i1) {
							found=true;
						}
					}
					if(!found) {
						c[i1]=-1;
						notconti=true;
					}
				}
			}
			
		}while(!notconti);
		
		for(int i3=1;i3<c.length;i3++) {
			if(c[i3]<0) {
				tt++;
			}
		}
		
		if(!checkchrome(h)) {
			tt=tt/2;
		}
		h.setfitness(tt);
		return tt;
	}
	
	
	public boolean checkchrome(Chromosome chr) {
		boolean r=true;
		int[] c=chr.getmapping();
		for(int i1=0;i1<c.length;i1++) {
			if(c[i1]<c.length) {
				if(c[i1]==c[c[i1]]) {
					r=false;
					break;
				}
			}
		}
		
		return r;
	}
	
	public void printfit(int[] d) {
		int[] c=new int[d.length];
		for(int i0=0;i0<c.length;i0++) {
			c[i0]=d[i0];
		}
		int tt=0;
		boolean notconti=false;
		
		do {
			c[0]=-1;
			for(int i1=1;i1<c.length;i1++) {
				if(c[i1]>(-1)) {
					boolean found=false;
					for(int i2=1;i2<c.length;i2++) {
						if(c[i2]==i1) {
							found=true;
						}
					}
					if(!found) {
						c[i1]=-1;
						notconti=true;
					}
				}
			}
		}while(!notconti);
		
		for(int i3=1;i3<c.length;i3++) {
			if(c[i3]<0) {
				tt++;
			}else {
			}
		}
		
		System.out.println(">"+Arrays.toString(c)+">"+tt);
	}
	
	public void range() {
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
	public void updatebest() {
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
		
		if(target==best.getbestfitness()) {
		success=true;
		}
	}
	
	public void printlastbest() {
		//print current overall best solution
		System.out.println("best fitness "+best.getbestfitness()+" in generation "+(gencount+1));
		}
}
