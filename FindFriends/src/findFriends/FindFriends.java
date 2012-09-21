package findFriends;

import java.util.*;
import java.io.*;

/**
 * Calculate the closure of the friend network in FBook given a friend list. 
 * If the tuple (n1, n2) is in Friend (i.e. it is the Friends table). 
 * The closure of the friend network contains the edge (n1, n2) whenever there is a path from n1 to node n2 in the friend network.
 * The table is basically  person : his/her friends(separated by space)
 *  
 * @author hui jia
 * @version 2012.09.19
 *
 */
public class FindFriends {

	private HashMap<String, HashSet<String>> friendMap = new HashMap<String, HashSet<String>>();
//	private HashMap<String, HashSet<String>> friendMapAfterBFS = new HashMap<String, HashSet<String>>();
	private HashMap<String, Boolean> ifPersonIsAsRoot = new HashMap<String, Boolean>();

	public static void main(String args[]){

		FindFriends ff = new FindFriends();
		try{
			FileInputStream fstream = new FileInputStream("friendList.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line, person and his/her direct friend were separated by ":"
			while ((strLine = br.readLine()) != null)   {
				// Print the content on the console
				System.out.println (strLine);
				String[] splitLineAryByColon = strLine.split(":");
				if(splitLineAryByColon.length != 2) {
					System.out.println("Invalid input!");
					in.close();
					System.exit(0);
				}
				String rt = splitLineAryByColon[0].trim();
				String friend = splitLineAryByColon[1].trim();
				ff.madeFriendSet(rt, friend);
				
			}
			//Close the input stream
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		ff.printFriendList();
		System.out.println("*** friends' closure result ***");
		ff.madeClosure();

	}

	// set all the friend in the friendMap to be unvisited(false)
	private HashMap<String, Boolean> resetVisitList(){
		HashMap<String, Boolean> ifPersonIsVisited = new HashMap<String, Boolean>();
		for(String key : friendMap.keySet()){
			ifPersonIsVisited.put(key, false);
		}	
		return ifPersonIsVisited;		
	}

	//set the friend list based on input
	private void madeFriendSet(String rt, String friendList){
		String[] friendAry = friendList.split(" ");
		HashSet<String> set = new HashSet<String>();
		for(String f : friendAry){
			set.add(f);
		}
		friendMap.put(rt, set);
	}


	//BFS(Breath-First-Search) for each root, also print out each pair of the closure of friends
	private void BFS(String rt){
		Queue<String> q = new LinkedList<String>();
		q.add(rt);
		HashSet<String> friendSet = new HashSet<String>();
		HashMap<String, Boolean> ifVisited = this.resetVisitList();
		System.out.print(rt+"\t:");
		while(!q.isEmpty()){
			String removedNode = q.remove();
			ifVisited.put(removedNode, true);
			if(!removedNode.equals(rt) ){ 
				friendSet.add(removedNode);
				System.out.print("\t"+removedNode);
			}
			if(friendMap.containsKey(removedNode)){
				for(String s : friendMap.get(removedNode)){
					if(!friendMap.containsKey(s) || !ifVisited.get(s)) q.add(s);
				}
			}
//			friendMapAfterBFS.put(rt, friendSet);

		}
		System.out.println();
	}

	//helper func for each person who has friend BFS traverse its friends list
	private void madeClosure(){
		for(String key : friendMap.keySet()){
			this.ifPersonIsAsRoot.put(key, true);
			this.BFS(key);			
		}
	}


	//helper func to print the original friend list
	private void printFriendList() {
		System.out.println("\n***** PRINTING FRIEND LIST *****");
		int i = 1;
		for(String key : friendMap.keySet()) {
			System.out.print("#"+i+" person: "+key + " friend: ");
			HashSet<String> friendSet = friendMap.get(key);
			for(String f : friendSet) {
				System.out.print(f+"\t");
			}
			System.out.println();
			i++;
		}
	}




}
