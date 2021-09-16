
public class Best {
	//Store the best fitness & mapping
	private String bestmapping[];		//Best mapping so far
	private int bestfitness=-1;			//The fitness for the above mapping
	private int gencount=0;
	
	public Best(int f){
		this.bestfitness=0;
		this.bestmapping=new String[f];
		for(int i=0;i<f;i++){
			bestmapping[i]=Integer.toString(i);
		}
	}
	
	public int getbestfitness(){
		return bestfitness;
	}
	
	public void setbestfitness(int newfit){
		bestfitness=newfit;
	}
	
	public void setgencount(int g){
		gencount=g;
	}
	
	public int getgencount(){
		return gencount;
	}
	
	public int[] getbestmapping(){
		int[] bmap=new int[bestmapping.length];
		for(int k=0;k<bestmapping.length;k++){
			bmap[k]=Integer.parseInt(bestmapping[k]);
		}
		return bmap;
	}
	
	public void setbestmapping(int[] ma){
		bestmapping=new String[ma.length];
		for(int i=0;i<ma.length;i++){
			bestmapping[i]=Integer.toString(ma[i]);
		}
	}
}
