package registerallocation.livenessanalysis;

import utils.graph.regalloc.InterferenceGraph;
import utils.graph.Node;
import utils.temp.Temp;
import utils.temp.TempList;
import utils.graph.regalloc.MoveList;

import java.util.Hashtable;
import java.util.HashSet;

public class Liveness extends InterferenceGraph {
    public Hashtable<Node, Temp> nodeToTemp;
    private Hashtable<Temp, Node> tempToNode;

    public Liveness(AssemFlowGraph flowGraph) {
        nodeToTemp = new Hashtable<Node, Temp>();
        tempToNode = new Hashtable<Temp, Node>();

        flowGraph.graphNodesToInstrs.forEach((node, instr) -> {
            HashSet<Temp> liveOut = flowGraph.nodeLiveOut.get(node);
            TempList instrDef = flowGraph.assemInstrs.get(instr).def();
            HashSet<Temp> def = instrDef != null ? instrDef.createTempSet() : new HashSet<Temp>();
            def.remove(null);

            TempList instrUse = flowGraph.assemInstrs.get(instr).use();
            HashSet<Temp> use = instrUse != null ? instrUse.createTempSet() : new HashSet<Temp>();
            use.remove(null);

            for (Temp defTemp : def) {
                Node defNodeInterference = this.getNodeAndAddToMapIfAbsent(defTemp);
                for (Temp outTemp : liveOut) {
                    Node outNodeInterference = this.getNodeAndAddToMapIfAbsent(outTemp);
                    if ((!flowGraph.isMove(node) || !use.contains(outTemp)) && defTemp != outTemp) {
                        this.addEdge(outNodeInterference, defNodeInterference);
                        this.addEdge(defNodeInterference, outNodeInterference);
                    }
                }
            }

        });
    }

    public Node tnode(Temp temp) { return tempToNode.get(temp); }
    public Temp gtemp(Node node) { return nodeToTemp.get(node); }
    public MoveList moves() { return null; }
    public int spillCost(Node node) { return 1; }

    private Node getNodeAndAddToMapIfAbsent(Temp key) {
        Node interferenceNode;

        if (tempToNode.containsKey(key)) interferenceNode = tempToNode.get(key);
        else { interferenceNode = this.newNode(); tempToNode.put(key, interferenceNode); nodeToTemp.put(interferenceNode, key); }

        return interferenceNode;
    }
}

