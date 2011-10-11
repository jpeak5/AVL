package AVL;

import java.io.*;

public class AVLops {

	public static void main(String[] args) {
		//timer routine sourced from  http://stackoverflow.com/questions/302026/measuring-java-execution-time-memory-usage-and-cpu-load-for-a-code-segment
		long start = System.nanoTime(); // requires java 1.5
		
		
		AVL avl = new AVL();
		StringBuffer debug = new StringBuffer();
		try {
			FileReader input = new FileReader(args[0]);
			BufferedReader bf = new BufferedReader(input);
			StringBuffer sb = new StringBuffer();
			String line = null;
			
			// Read through file one line at time. Print line # and line
			while ((line = bf.readLine()) != null) {
				String cmd = line.substring(0, 2);
				int key;
				
				if (line.length() > 2) {
					key = Integer.parseInt(line.substring(3));
					if (cmd.equals("IN")) {//insert
						avl.insert(new Node(key), avl.root);
						System.out.println(cmd +" : "+key);
					}else if(cmd.equals("SC")){//successor
						
						if(avl.search(avl.root, key)!=true){
							System.out.println("Error: no such key, hence, no successor");
						}
						else{
							System.out.println(avl.successor(key).key+"     //SC "+ key);
						}
					}else if(cmd.equals("PR")){//predecessor
						if(avl.search(avl.root, key)!=true){
							System.out.println("Error: no such key, hence, no successor");
						}
						else{
							Node p = avl.predecessor(key);
							if(p!=null){
								System.out.println(avl.predecessor(key).key+"     //PR "+ key);
							}else{
								System.out.println("ERROR: Searching for predecessor to min     //PR "+ key);
							}
						}
					}else if(cmd.equals("SR")){//search
						System.out.println(avl.search(avl.root, key)+"     //SR "+key);
					}else if(cmd.equals("SE")){//select
						
					}else if(cmd.equals("RA")){//rank
						
					}
				}else if(cmd.equals("MI")){//min
					System.out.println(avl.min(avl.root).key + "     //Min");
				}else if(cmd.equals("MA")){//max
					System.out.println(avl.max(avl.root).key + "     //Max");
				}else if(cmd.equals("TR")){//traverse inorder
					System.out.println("inorder:\n");
					avl.inorder(avl.root);
				}
				sb.append(line);
			}
			
			bf.close();
			// Segment to monitor
			System.out.println("\n"+(System.nanoTime() - start) / 1000 +" microseconds");
		} catch (ArrayIndexOutOfBoundsException e) {

			System.out.println("Usage: be sure to supply a filename\n");

		} catch (IOException e) {
			// If another exception is generated, print a stack trace
			e.printStackTrace();
		}

		
	}

}
