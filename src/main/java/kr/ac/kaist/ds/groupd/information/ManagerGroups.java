
package kr.ac.kaist.ds.groupd.information;

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

/**
 * ManagerGroups class manage the GroupInformation.
 */
public class ManagerGroups {

    // delete this code when some test~.
    public static int _BLANCE_MODE = 0x0000000000000001;

    private int mode;

    private int totalNode;

    private ArrayList<GroupInformation> groupInformations;

    private int setNowGroupIndex;

    private ArrayList<Node> deleyQueue;

    public ManagerGroups(int totalNode, int mode) {
        groupInformations = new ArrayList<GroupInformation>();
        deleyQueue = new ArrayList<Node>();
        // if you initialize the setNowGroupIndexNumber. the program cannot
        // operate;

        GroupInformation temp = new GroupInformation(groupInformations.size() + 1);

        groupInformations.add(temp);

        setNowGroupIndex = groupInformations.indexOf(temp);
        this.mode = mode;
        this.totalNode = totalNode;

        System.out.println("Total Node : " + gettotalNode() + " and SuccessFully Operate MG");
    }

    /**
     * this function offers change all nodes position. if you set loopingNumber.
     * the function will be operating base on loopngNumber. Now the choice
     * algorithm based on random. so we will be modifyed this code.
     * 
     * @param loopingNumber
     */
    public void changeTheTotalNodeLocation(int loopingNumber) {
        int nodeId = new Random().nextInt(totalNode);
        Node temp = null;

        // 향후 Balance mode, etc 참조.

        for (int j = 0; j < loopingNumber; j++) {
            for (int i = 0; i < groupInformations.size(); i++) {
                if (null != groupInformations.get(i).extractNode(nodeId)) {
                    temp = groupInformations.get(i).extractNode(nodeId);
                    break;
                }
            }

            if (temp == null)
                for (int i = 0; i < deleyQueue.size(); i++)
                    if (deleyQueue.get(i).getID() == nodeId) {
                        temp = deleyQueue.get(i);
                    }

            if (temp == null)
                return;

            if (1 == new Random().nextInt(2)) {
                groupInformations.get(new Random().nextInt(groupInformations.size()))
                        .addNeighborNode(temp);
            } else {
                deleyQueue.add(temp);
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
        this.groupInformations.get(setNowGroupIndex).addNeighborNode(node);
    }

    // Set

    public void setNowGroupIndex(GroupInformation groupInformation) {
        this.setNowGroupIndex = groupInformations.indexOf(groupInformation);
    }

    public void setAlGroupInformations(ArrayList<GroupInformation> groupInformations) {
        this.groupInformations = groupInformations;
    }

    public void setmode(int mode) {
        this.mode = mode;
    }

    public void settotalNode(int totalNode) {
        this.totalNode = totalNode;
    }

    /**
     * the existNodeInManagerGroups offers check the node exist in all Node's Groups. so if the node exist any Groups. you get
     * 'true' but their not exist in the group. you will get 'false'
     * 
     * @param node
     * @return
     */
    public boolean existNodeInManagerGroups(Node node) {
        for (int i = 0; i < groupInformations.size(); i++) {

            if (false != groupInformations.get(i).getNeighborNodes().contains(node)) {
                // System.out.println("exist in ("+i+")"+"groupInformations");
                return true;
            }
        }
        return false;
    }

    // Get

    public int getNowGroupIndex() {
        return setNowGroupIndex;
    }

    public int getdeleyQueueNumber() {
        return deleyQueue.size();
    }

    public int getTotalGroupInformation() {
        return groupInformations.size();
    }

    public GroupInformation getGroupInformation(int index) {
        return groupInformations.get(index);
    }

    public ArrayList<GroupInformation> getAllGroupInformations() {
        return groupInformations;
    }

    public int getmode() {
        return mode;
    }

    public int gettotalNode() {
        return totalNode;
    }

}
