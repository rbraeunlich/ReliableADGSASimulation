
package kr.ac.kaist.ds.groupd.information;

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

public class ManagerGroups {

    private int nTotalNode;

    private ArrayList<GroupInformation> alGroupInformations;

    private ArrayList<Node> alDeleyQueue;

    public ManagerGroups(int totalNode) {
        alGroupInformations = new ArrayList<GroupInformation>();
        alDeleyQueue = new ArrayList<Node>();

        nTotalNode = totalNode;
    }

    public void changeTheTotalNodeLocation(int loopingNumber) {
        int nodeId = new Random().nextInt(nTotalNode);
        Node temp = null;

        // 향후 Balance mode, etc 참조.
        
        for (int j = 0; j < loopingNumber; j++) {
            for (int i = 0; i < alGroupInformations.size(); i++) {
                if (null != alGroupInformations.get(i).getNode(nodeId)) {
                    temp = alGroupInformations.get(i).getNode(nodeId);
                    break;
                }
            }

            if (temp == null)
                for (int i = 0; i < alDeleyQueue.size(); i++)
                    if (alDeleyQueue.get(i).getID() == nodeId)
                        temp = alDeleyQueue.get(i);

            if (temp == null)
                return;

            if (1 == new Random().nextInt(2)) {
                alGroupInformations.get(new Random().nextInt(alGroupInformations.size()))
                        .addNeighborNode(temp);
            } else {
                alDeleyQueue.add(temp);
            }
        }

        // add the node's neighbor
    }

    public void makeGroupInformation()
    {
        addGroupInformation(new GroupInformation(alGroupInformations.size()));
    }
    
    public void addGroupInformation(GroupInformation Grif) {
        this.alGroupInformations.add(Grif);
    }

    public void setAlGroupInformations(ArrayList<GroupInformation> alGroupInformations) {
        this.alGroupInformations = alGroupInformations;
    }

    public int getDeleyQueueNumber() {
        return alDeleyQueue.size();
    }

    public int getTotalGroupInformation() {
        return alGroupInformations.size();
    }

    public GroupInformation getGroupInformation(int index) {
        return alGroupInformations.get(index);
    }

    public ArrayList<GroupInformation> getAlGroupInformations() {
        return alGroupInformations;
    }

}
