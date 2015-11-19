
package kr.ac.kaist.ds.groupd.information;

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

public class ManagerGroups {

    public static int _BLANCE_MODE = 0x0000000000000001;

    private int nMode;

    private int nTotalNode;

    private ArrayList<GroupInformation> groupInformations;

    private int nSetNowGroupIndex;

    private ArrayList<Node> DeleyQueue;

    public ManagerGroups(int totalNode, int mode) {
        groupInformations = new ArrayList<GroupInformation>();
        DeleyQueue = new ArrayList<Node>();
        // if you initialize the nSetNowGroupIndexNumber. the program cannot
        // operate;
        nSetNowGroupIndex = -1;
        nMode = mode;
        nTotalNode = totalNode;
    }

    public void changeTheTotalNodeLocation(int loopingNumber) {
        int nodeId = new Random().nextInt(nTotalNode);
        Node temp = null;

        // 향후 Balance mode, etc 참조.

        for (int j = 0; j < loopingNumber; j++) {
            for (int i = 0; i < groupInformations.size(); i++) {
                if (null != groupInformations.get(i).getNode(nodeId)) {
                    temp = groupInformations.get(i).getNode(nodeId);
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
                groupInformations.get(new Random().nextInt(groupInformations.size()))
                        .addNeighborNode(temp);
            } else {
                DeleyQueue.add(temp);
            }
        }

        // add the node's neighbor
    }

    // Make

    public void makeGroupInformation() {
        addGroupInformation(new GroupInformation(groupInformations.size()));
    }

    // Add

    public void addGroupInformation(GroupInformation groupInformation) {
        this.groupInformations.add(groupInformation);
    }

    public void addNode(Node node) {
        this.groupInformations.get(nSetNowGroupIndex).addNeighborNode(node);
    }

    // Set

    public void setNowGroupIndex(GroupInformation groupInformation) {
        this.nSetNowGroupIndex = groupInformations.indexOf(groupInformation);
    }

    public void setAlGroupInformations(ArrayList<GroupInformation> groupInformations) {
        this.groupInformations = groupInformations;
    }

    public void setnMode(int nMode) {
        this.nMode = nMode;
    }

    public void setnTotalNode(int nTotalNode) {
        this.nTotalNode = nTotalNode;
    }

    // Find

    public boolean existnodeInManagerGroups(Node node) {
        for (int i = 0; i < groupInformations.size(); i++) {

            if (-1 != groupInformations.get(i).getNeighborNodes().indexOf(node))
                return true;
        }
        return false;
    }

    // Get

    public int getDeleyQueueNumber() {
        return DeleyQueue.size();
    }

    public int getTotalGroupInformation() {
        return groupInformations.size();
    }

    public GroupInformation getGroupInformation(int index) {
        return groupInformations.get(index);
    }

    public ArrayList<GroupInformation> getAlGroupInformations() {
        return groupInformations;
    }

    public int getnMode() {
        return nMode;
    }

    public int getnTotalNode() {
        return nTotalNode;
    }

}
