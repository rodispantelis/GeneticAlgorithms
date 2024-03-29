import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

/*
    This is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
	https://www.gnu.org/licenses/agpl-3.0.txt
	
	Published by Pantelis Rodis, 2021
*/	

public class Main {
    
	int population=-1, generations=-1, supergenerations=-1;
	double crossprob=-1.0, mutprob=-1.0;
	boolean checked=false;
	String command[]= {"0"}, tokens[]= {"0"}, target="", variables[][]= {{"0"},{"0"}};
	int parameter_dom[][];
	
	public static void main(String[] args) {
		Main m = new Main();
		
		String p="profile", s="settings";
		
		//load command line arguments
	    if(args.length>0) {
	    	p=args[0];
		    if(args.length>1) {
		    	s=args[1];
		    }
	    }
	    
	    m.checkfiles(p, s);
	    
	    if(m.checked) {//if files are valid continue
	    	System.out.println("Profile and settings files loaded.");
	    	GA ga=new GA(m.population, m.generations, m.supergenerations, m.tokens, m.command,
	    			m.crossprob, m.mutprob, m.parameter_dom, m.target);
	    	ga.initparams();
	    	ga.init();
	    }
	}   

	
	public void checkfiles(String p, String s) {//checks if files exit and if they are valid
		File pr=new File(p);
		File st=new File(s);
		
		if(!pr.exists()) {
			System.out.println("Profile is missing. Specify a valid profile.");
		}
		if(!st.exists()) {
			System.out.println("Settings file is missing. Specify a valid file.");
		}
		if(pr.exists() && st.exists()) {
			setsettings(st);
			setprofile(pr);
			checkvars();
		}
		
		if(checked) {
			parameter_dom=new int[tokens.length][2];

 			for(int c1=0;c1<tokens.length;c1++) {
 				for(int c2=0;c2<variables.length;c2++) {
 					if(tokens[c1].equals(variables[c2][0])) {
 						parameter_dom[c1][0]=Integer.parseInt(variables[c2][1]);
 						parameter_dom[c1][1]=Integer.parseInt(variables[c2][2]);
 					}
 				}
 			}	
		}
	}
 	
 	public void setsettings(File st) {//loads settings from file
 		
		try{
	    	Scanner scanner = new Scanner(st);

	    	while(scanner.hasNext()){
	    		String[] params= scanner.nextLine().split(" ");
	    		if(params[0].equals("population")) {
	    			population=Integer.parseInt(params[1]);
	    		}else if(params[0].equals("generations")) {
	    			generations=Integer.parseInt(params[1]);
	    		}else if(params[0].equals("supergenerations")) {
	    			supergenerations=Integer.parseInt(params[1]);
	    		}else if(params[0].equals("crossover_probability")) {
	    			crossprob=Double.parseDouble(params[1]);
	    		}else if(params[0].equals("mutation_probability")) {
	    			mutprob=Double.parseDouble(params[1]);
	    		}
	    	}
	    	scanner.close();
		}
		catch (IOException e) {
		       e.printStackTrace();
		   }
 	}
 	
 	public void setprofile(File pr) {//loads profile from file
 		
 		ArrayList<String[]> vars=new ArrayList<String[]>();
		try{
	    	Scanner scanner = new Scanner(pr);

	    	while(scanner.hasNext()){
	    		String[] params= scanner.nextLine().split(" ");
	    		if(!params[0].split("")[0].equals("%") && !params[0].split("")[0].equals("")) {
		    		if(params[0].equals("command")) {
		    			command=IntStream.range(1, params.length)
                                .mapToObj(i -> params[i])
                                .toArray(String[]::new);
		    		}else if(params[0].equals("tokens")) {
		    			tokens=IntStream.range(1, params.length)
                                .mapToObj(i -> params[i])
                                .toArray(String[]::new);
		    		}else if(params[0].equals("target")) {
		    			target=params[1];
		    		}else{
		    			String temp[]=new String[3];
		    			String temp2="";
		    			temp[0]=params[0];
		    			for(int i=1;i<params.length;i++) {
		    				temp2+=params[i];
		    			}
		    			String temp3[]=temp2.split("-");
		    			if(temp3.length==2) {
		    				temp[1]=temp3[0];
		    				temp[2]=temp3[1];
		    				vars.add(temp);
		    			}
		    		}
	    		}
	    	}
	    	scanner.close();
		}
		catch (IOException e) {
		       e.printStackTrace();
		   }
		
		variables = new String[vars.size()][3];
		
		for(int v=0;v<vars.size();v++) {
			variables[v]=vars.get(v);
		}		
 	}
  
 	public void checkvars() {//checks the validity of profile and settings files
 		boolean checkprofile=true;
 		boolean checksettings=true;
 		
		if(population<2 || generations<2 || supergenerations<1 || 
				crossprob<0.0 || mutprob<0.0) {
			checkprofile=false;
		}
 		
 		if(target.equals("")) {
 			checkprofile=false;
 		}
 		
 		for(int t1=0;t1<tokens.length;t1++) {
 			for(int t2=(t1+1);t2<tokens.length;t2++) {
 				if(tokens[t1].equals(tokens[t2])) {
 					System.out.println("There are duplicate values in the tokens section.");
 					checkprofile=false;
 					break;
 				}
 			}
 		}
 		
 		for(int t3=0;t3<variables.length;t3++) {
 			for(int t4=(t3+1);t4<variables.length;t4++) {
 				if(variables[t3][0].equals(variables[t4][0])) {
 					System.out.println("There are duplicate parameters.");
 					checkprofile=false;
 					break;
 				}
 			}
 		}
 		
 		if(checkprofile) {
 			for(int p1=0;p1<tokens.length;p1++) {
 				boolean temp=false;
 				for(int p2=0;p2<variables.length;p2++) {
 					if(tokens[p1].equals(variables[p2][0])) {
 						temp=true;
 					}
 				}
 				if(!temp) {
 					checksettings=false;
 					System.out.println("There is not 1-1 correspondence between the tokens and the parameters.");
 					break;
 				}
 			}
 		}
 		
 		if(!checkprofile) {
 			System.out.println("The profile file is invalid");
 		}
 		if(!checksettings) {
 			System.out.println("The settings file is invalid");
 		}
 		checked=checkprofile && checksettings;
 	}

}
