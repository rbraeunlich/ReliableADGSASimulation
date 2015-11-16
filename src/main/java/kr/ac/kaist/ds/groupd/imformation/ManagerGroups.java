
package kr.ac.kaist.ds.groupd.imformation;

import java.util.ArrayList;
import java.util.Random;

import peersim.core.Node;

public class ManagerGroups {
    private static ArrayList<GroupInformation> alGroupInformations = new ArrayList<GroupInformation>();

    private static ArrayList<Node> alDeleyQueue = new ArrayList<Node>();

    public static void changeTheTotalNodeLocation(int totalNode) {
        int nodeId = new Random().nextInt(totalNode);
        Node temp = null;

        for (int i = 0; i < alGroupInformations.size(); i++) {
            if (null != alGroupInformations.get(i).getNode(nodeId)) {
                temp = alGroupInformations.get(i).getNode(nodeId);
                break;
            }
        }

        if (temp == null)
            for(int i = 0 ; i < alDeleyQueue.size(); i++)
                if (alDeleyQueue.get(i).getID() == nodeId)
                    temp = alDeleyQueue.get(i);
        
        if( temp == null)
            return;

        if (1 == new Random().nextInt(2)) {
            alGroupInformations.get(new Random().nextInt(alGroupInformations.size()))
                    .addNeighborNode(temp);
        }
        else
        {
            alDeleyQueue.add(temp);
        }

        // add the node's neighbor
    }

    public static void addGroupInformation(GroupInformation Grif) {
        ManagerGroups.alGroupInformations.add(Grif);
    }

    public static ArrayList<GroupInformation> getAlGroupInformations() {
        return alGroupInformations;
    }

    public static void setAlGroupInformations(ArrayList<GroupInformation> alGroupInformations) {
        ManagerGroups.alGroupInformations = alGroupInformations;
    }

}
