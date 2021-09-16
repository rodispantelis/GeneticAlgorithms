
public class Codec {
	//encodes and decodes graph in the Edge Vector format
	//numbering starts from 0
	public Codec() {
	}
	
	public int coder(int a, int b){
		//on input of two nodes, you get the place of their edge on the vector
		int x=0;
		if(a==b){
			System.out.println("error"+a+"|"+b);
			}else{
				if(a>b){
					int t=a;
					a=b;
					b=t;
				}
				x=((b-1)*(1+(b-1)))/2;
			}
		return x+a;

	}
	
	public int[] decoder(int q){
		//on input of one place of the vector that denotes one edge of the graph
		//you get the two nodes that define the edge
		//x<y
			
		int x=0;
		int y=0;
		int result[]=new int[2];

		y=(int) Math.sqrt((2*q));
		y++;
		x=q-(((y*y)-y)/2);
		
		if(x<0){
			y--;
			x=q-(((y*y)-y)/2);
		}
		
		if(x<y) {
			result[0]=x;
			result[1]=y;
		}else {
			result[0]=y;
			result[1]=x;
		}

		return result;
	}

	public int coderOld(int a, int b){
		//on input of two nodes, you get the place of their edge on the vector
		int x=0;
		if(a==b){
			System.out.println("error"+a+"|"+b);
			}else{
				if(a>b){
					int t=a;
					a=b;
					b=t;
				}
				for(int i=1;i<b;i++){
					x+=i;
				}
			}
		return x+a;

	}
	
	public int[] decoderOld(int q){
		//on input of one place of the vector that denotes one edge of the graph
		//you get the two nodes that define the edge
		//x<y
		int x=0;
		int y=1;
		int cnt=0;
		
		while(cnt<q){
			if(x<(y-1)){
				x++;
				cnt++;
			}else{
				y++;
				x=0;
				cnt++;
			}
			 }
		
		int result[]={x,y};
		return result;
		
	}
}
