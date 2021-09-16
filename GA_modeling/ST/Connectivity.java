public class Connectivity {
	
	//check whether a graph is connected
	
	Graph graph=new Graph();
	int[] ev,map;
	int node0=0;
	int res=0;
	Codec codec=new Codec();
	
	public Connectivity(Graph g) {
		this.graph=g;
		ev=graph.graph;
	}
	
	public Connectivity(int[] g) {
		this.ev=g;
	}
	
	
	public int check0() {
		int max=1;
    	int nd[]=codec.decoder(1+ev.length);
		if((nd[0]+1)>max){
			max=(nd[0]);
		}
		if((nd[1]+1)>max){
			max=(nd[1]);
		}
	
		graph.nodes=max;
		graph.edges=graph.nodes*(graph.nodes-1)/2;
		graph.graph=ev;
		node0=ev[0];
		return check();
	}
	
	public int check() {
		map=new int[graph.nodes];
		for(int m=0;m<map.length;m++) {
			map[m]=m;
		}

		map[node0]=-1;

		adj(node0);

		int con=-1;
		for(int m=0;m<map.length;m++) {
			if(map[m]<0) {
				con++;
			}
		}
		
		return con;
	}
	
	public void adj(int f) {
		//node adjacency
		for(int i1=0;i1<map.length;i1++) {
			if(map[i1]>(-1)) {
			if(i1!=f) {
				if(ev[codec.coder(i1, f)]>0) {
					map[i1]=-1;
					adj(i1);
				}
			}
			}
		}
		
	}

}

