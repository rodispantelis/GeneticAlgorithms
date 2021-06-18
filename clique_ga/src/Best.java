
public class Best {
	//store the best fitness & mapping
	private String bestmapping[];	//best mapping so far
	private int bestfitness=0;		//fitness for the above mapping
	
	public Best(int f){
		this.bestfitness=-1;
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
