package kr.ac.kaist.ds.groupd.example.token;

import java.util.ArrayList;
import java.util.Random;

import peersim.cdsim.CDProtocol;
import peersim.config.FastConfig;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.vector.SingleValueHolder;

public class TokenProtocol extends SingleValueHolder implements CDProtocol {

	private boolean token;
	private ArrayList<Node> alResultQueue;

	public TokenProtocol(String name) {
		super(name);
		alResultQueue = new ArrayList<Node>();
	}

	public boolean hasToken() {
		return token;
	}

	public void resetToken() {
		this.token = false;
	}
	
	public void clearQueue() {
		this.alResultQueue.clear();
	}

	@Override
	public void nextCycle(Node node, int pid) {
		//boolean isOnlyOneTime = true;
		//boolean isOnlyOneInput = true;
		int a= 10;
		
		
		if (token = true) {
			Linkable linkable = (Linkable) node.getProtocol(FastConfig
					.getLinkable(pid));
		
			System.out.println("\n\n--------");
			System.out.print("Neighbors of "+node.getID()+"are[");
	         for(int j=0; j<linkable.degree();j++)
	         {
	            System.out.print(linkable.getNeighbor(j).getID());
	         }
	        System.out.print("]\n"+linkable);
			//System.out.println("\ntest : " + linkable.getNeighbor(0));
			int degree = linkable.degree();
			System.out.print(linkable.getNeighbor(0).getID());
			Node neighbor = linkable.getNeighbor(new Random().nextInt(degree));  
			TokenProtocol protocol = (TokenProtocol) neighbor.getProtocol(pid);
			
			protocol.setToken(true);
			
			for( int i =0 ; i < alResultQueue.size(); i++)
			{
				if(alResultQueue.get(i).getID() == node.getID())
				{
					alResultQueue.remove(i);
					break;
				}
			}
			
			if(alResultQueue.size() < 1)
				alResultQueue.add(neighbor);
			else
			{
				for( int i =0 ; i < alResultQueue.size(); i++)
				{
					if(alResultQueue.get(i).getID() == neighbor.getID())
					{
						break;
					}else if(i == alResultQueue.size()-1 && alResultQueue.get(i).getID() != neighbor.getID())
					{
						alResultQueue.add(neighbor);
					}
				}
			}
				
			
			
			
			/*else
			for(int i = alResultQueue.size()-1 ; i > -1; i --)
			{
				if(alResultQueue.get(i).getID() == node.getID() &&isOnlyOneTime)
				{
					alResultQueue.remove(i);
					isOnlyOneTime = false;
				}else if(alResultQueue.get(i).getID() == neighbor.getID() && isOnlyOneInput)
				{
					isOnlyOneInput = false;
				}else if(i == 0 && alResultQueue.get(i).getID() != neighbor.getID() && isOnlyOneInput)
					{
						alResultQueue.add(neighbor);
						break;
					}
			}
			*/
			
			System.out.println("");
			System.out.println("pre: "+node.getID()+" now: "+neighbor.getID());
			
			System.out.print("output ID Set :");
			for(int i = 0 ; i < alResultQueue.size() ; i ++)
				System.out.print(alResultQueue.get(i).getID()+" ");

			
			resetToken();
		}

	}

	public void setToken(boolean b) {
		this.token = true;
	}
}
