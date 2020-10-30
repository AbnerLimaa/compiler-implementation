package utils.graph;

import utils.irtree.abstractions.Stm;
import utils.irtree.abstractions.StmList;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class NodeList {
    public Node head;
    public NodeList tail;
    public NodeList(Node h, NodeList t) {head=h; tail=t;}

    public Node getElementAt(int i) {
        if ( i < 0 ) throw new Error("Index can't be negative");

        if (i == 0) {
            return head;
        } else if (tail != null) {
            return tail.getElementAt(i - 1);
        } else {
            throw new Error("Index out of bounds");
        }
    }

    public void removeNode(Node n) {
        List<Node> javaList = transformIntoJavaList(this);
        javaList.remove(n);
        NodeList nodeList = transformIntoNodeList(javaList);
        if (nodeList != null) {
            head = nodeList.head;
            tail = nodeList.tail;
        }
    }

    public static List<Node> transformIntoJavaList(NodeList nodeList) {
        List<Node> transformed = new ArrayList<Node>();
        if (nodeList != null) {
            transformed.add(nodeList.head);
            NodeList listTail = nodeList.tail;
            while (listTail != null) {
                transformed.add(listTail.head);
                listTail = listTail.tail;
            }
        }
        return transformed;
    }

    public static NodeList transformIntoNodeList(List<Node> nodeList) {
        ListIterator<Node> listIterator = nodeList.listIterator(nodeList.size());
        NodeList newNodeList = null;
        while (listIterator.hasPrevious()) {
            newNodeList = new NodeList(listIterator.previous(), newNodeList);
        }
        return newNodeList;
    }

}

