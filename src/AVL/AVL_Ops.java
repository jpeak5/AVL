package AVL;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class AVL_Ops {

	public static void main(String[] args) throws IOException {
		//timer routine sourced from  http://stackoverflow.com/questions/302026/measuring-java-execution-time-memory-usage-and-cpu-load-for-a-code-segment
		long start = System.nanoTime(); // requires java 1.5
		
		
		StringBuffer output = new StringBuffer();
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
					}else if(cmd.equals("SC")){//successor
						if(avl.search(avl.root, key)!=true){
							output.append("Error: no such key, hence, no successor\n");
						}
						else{
							output.append(avl.successor(key).key+"\n");
						}
					}else if(cmd.equals("PR")){//predecessor
						if(avl.search(avl.root, key)!=true){
							output.append("Error: no such key, hence, no predecessor\n");
						}
						else{
							Node p = avl.predecessor(key);
							if(p!=null){
								output.append((avl.predecessor(key).key+"\n"));
							}else{
								output.append(("ERROR: Searching for predecessor to min\n"));
							}
						}
					}else if(cmd.equals("SR")){//search
						output.append((avl.search(avl.root, key) ? "true" : "false")+"\n");
					}else if(cmd.equals("SE")){//select
						Node s = avl.select(avl.root, key,0);
						output.append(s!=null ? s.key+"\n" : "index out of bounds\n");
					}else if(cmd.equals("RA")){//rank
						int rank = avl.rank(key);
						output.append(rank+"\n");
					}
				}else if(cmd.equals("MI")){//min
					output.append(avl.min(avl.root).key+"\n");
				}else if(cmd.equals("MA")){//max
					output.append(avl.max(avl.root).key+"\n");
				}else if(cmd.equals("TR")){//traverse inorder
					output = (avl.inorder(avl.root, output));
					
				}
				sb.append(line);
				
			}
			
			bf.close();
		} catch (ArrayIndexOutOfBoundsException e) {

			System.out.println("Usage: be sure to supply a filename\n");

		} catch (IOException e) {
			// If another exception is generated, print a stack trace
			e.printStackTrace();
		}

		
		// Segment to monitor
		output.append("\n"+(System.nanoTime() - start) / 1000 +" microseconds");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
		bw.write(output.toString());
		
		bw.close();
	}

}
