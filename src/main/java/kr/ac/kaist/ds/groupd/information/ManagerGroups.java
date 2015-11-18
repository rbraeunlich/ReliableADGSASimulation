
package kr.ac.kaist.ds.groupd.information;

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

public class ManagerGroups {

    private int nTotalNode;

    private ArrayList<GroupInformation> GroupInformations;

    private ArrayList<Node> DeleyQueue;

    public ManagerGroups(int totalNode) {
        GroupInformations = new ArrayList<GroupInformation>();
        DeleyQueue = new ArrayList<Node>();

        nTotalNode = totalNode;
    }

    public void changeTheTotalNodeLocation(int loopingNumber) {
        int nodeId = new Random().nextInt(nTotalNode);
        Node temp = null;

        // 향후 Balance mode, etc 참조.
        
        for (int j = 0; j < loopingNumber; j++) {
            for (int i = 0; i < GroupInformations.size(); i++) {
                if (null != GroupInformations.get(i).getNode(nodeId)) {
                    temp = GroupInformations.get(i).getNode(nodeId);
                    break;
                }
            }

            if (temp == null)
                for (int i = 0; i < DeleyQueue.size(); i++)
                    if (DeleyQueue.get(i).getID() == nodeId)
                        temp = DeleyQueue.get(i);

            if (temp == null)
                return;

            if (1 == new Random().nextInt(2)) {
                GroupInformations.get(new Random().nextInt(GroupInformations.size()))
                        .addNeighborNode(temp);
            } else {
                DeleyQueue.add(temp);
            }
        }

        // add the node's neighbor
    }

    public void makeGroupInformation()
    {
        addGroupInformation(new GroupInformation(GroupInformations.size()));
    }
    
    public void addGroupInformation(GroupInformation Grif) {
        this.GroupInformations.add(Grif);
    }

    public void setAlGroupInformations(ArrayList<GroupInformation> alGroupInformations) {
        this.GroupInformations = alGroupInformations;
    }

    public int getDeleyQueueNumber() {
        return DeleyQueue.size();
    }

    public int getTotalGroupInformation() {
        return GroupInformations.size();
    }

    public GroupInformation getGroupInformation(int index) {
        return GroupInformations.get(index);
    }

    public ArrayList<GroupInformation> getAlGroupInformations() {
        return GroupInformations;
    }

}
