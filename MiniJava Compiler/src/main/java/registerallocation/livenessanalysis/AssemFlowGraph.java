package registerallocation.livenessanalysis;

import utils.graph.flowgraph.FlowGraph;
import utils.graph.Node;
import utils.temp.TempList;
import utils.temp.Temp;
import utils.assem.Instr;
import utils.temp.Label;
import utils.assem.LABEL;
import utils.assem.MOVE;
import utils.assem.Targets;
import utils.temp.LabelList;
import utils.graph.NodeList;

import java.util.List;
import java.util.Hashtable;
import java.util.HashSet;


class MutableBoolean {
    boolean value;
    public MutableBoolean(boolean v) { value = v; }
    public void setValue(boolean v) { value = v;  }
    public boolean getValue() { return value; }
}

public class AssemFlowGraph extends FlowGraph {
    public Hashtable<Node, Integer> graphNodesToInstrs;
    public Hashtable<Integer, Node> instrsToGraphNodes;
    public Hashtable<Node, HashSet<Temp> > nodeLiveIn;
    public Hashtable<Node, HashSet<Temp> > nodeLiveOut;
    public List<Instr> assemInstrs;

    public AssemFlowGraph(List<Instr> assemInstrs) {
        /* Percorrer cada elemento da lista. Se houver um Node associado
         * ao elemento, usar esse Node e criar uma Edge dele a seus vizinhos.
         * Se não houver um Node associado, criar um e criar o Edge. Os vizinhos
         * podem ser descobertos pelo métodos `jumps` do Instr. Se o método jumps
         * retornar vazio, use como vizinho o próximo elemento da lista, se houver. 
         * Se o método jumps retornar uma lista não vazia, descubra os índices
         * correspondetes a cada label e então use como vizinho a próxima instrução 
         * que não é um label
         */
        this.assemInstrs = assemInstrs; 
        Hashtable<Label, Integer> instrLabelIndexes = this.findInstrLabelIndexes(assemInstrs);
        this.graphNodesToInstrs = new Hashtable<Node, Integer>();
        this.instrsToGraphNodes = new Hashtable<Integer, Node>();

        for (Integer i = 0; i < assemInstrs.size(); i++) {
            Node currentNode = getNodeAndAddToMapIfAbsent(i); 
            //System.out.println("CURRENT NODE KEY: " + currentNode);
            Instr currentInstr = assemInstrs.get(i);
            //System.out.println("CURRENT INSTR: " + currentInstr.format(new DefaultMap()));
            Targets jumpToInstrs = currentInstr.jumps();
            //System.out.println("TARGET: " + jumpToInstrs);

            if (jumpToInstrs == null) {
                Node neighbourNode = getNodeAndAddToMapIfAbsent(i + 1);
                if (neighbourNode != null) {
                    //System.out.println("NEIGHBOUR: " + neighbourNode);
                    this.addEdge(currentNode, neighbourNode);
                }
            } else {
                for (LabelList l = jumpToInstrs.labels; l != null; l = l.tail) {
                    Integer nextInstrIndex = instrLabelIndexes.get(l.head);
                    Node neighbourNode = getNodeAndAddToMapIfAbsent(nextInstrIndex);
                    this.addEdge(currentNode, neighbourNode);
                    //System.out.println("NEIGHBOUR: " + neighbourNode);
                }
            }
            //System.out.println(" ###### ");

        }
    }

    public boolean isMove(Node node) {
        return this.assemInstrs.get(graphNodesToInstrs.get(node)) instanceof MOVE;
    } 

    private Node getNodeAndAddToMapIfAbsent(Integer key) {
            if (key >= this.assemInstrs.size()) return null;
            Node currentNode;
            if (instrsToGraphNodes.containsKey(key)) currentNode = instrsToGraphNodes.get(key);
            else { currentNode = this.newNode(); instrsToGraphNodes.put(key, currentNode); graphNodesToInstrs.put(currentNode, key); }

            return currentNode;
    }
    private Hashtable<Label, Integer> findInstrLabelIndexes(List<Instr> assemInstrs) { 
        Hashtable<Label, Integer> instrLabelIndexes = new Hashtable<Label, Integer>();
        for (int i = 0; i < assemInstrs.size(); i++) {
            Instr currentInstr = assemInstrs.get(i);
            if (currentInstr instanceof LABEL) {
                instrLabelIndexes.put(((LABEL)currentInstr).label, i);
            }
        }
        return instrLabelIndexes; 
    }
    public TempList def(Node node) {
        Instr instr = this.assemInstrs.get(graphNodesToInstrs.get(node));
        return instr.def();
    }

    public TempList use(Node node) { 
        Instr instr = this.assemInstrs.get(graphNodesToInstrs.get(node));
        return instr.use();
    }

    public void solveDataFlowEquation() {
        this.nodeLiveIn = new Hashtable<Node, HashSet<Temp>>();
        this.nodeLiveOut = new Hashtable<Node, HashSet<Temp>>();

        this.graphNodesToInstrs.forEach((node, instr) -> {
                this.nodeLiveIn.put(node, new HashSet<Temp>());
                this.nodeLiveOut.put(node, new HashSet<Temp>());
                });

        MutableBoolean fixedPointReached = new MutableBoolean(true);

        do {
            fixedPointReached.setValue(true);
            this.graphNodesToInstrs.forEach((node, instr) -> {
                    HashSet<Temp> oldOut = this.nodeLiveOut.get(node);
                    HashSet<Temp> oldIn = this.nodeLiveIn.get(node);

                    TempList instrDef = this.assemInstrs.get(instr).def();
                    HashSet<Temp> def = instrDef != null ? instrDef.createTempSet() : new HashSet<Temp>();
                    def.remove(null);
                    TempList instrUse = this.assemInstrs.get(instr).use();
                    HashSet<Temp> use = instrUse != null ? instrUse.createTempSet() : new HashSet<Temp>();
                    use.remove(null);
                    
                    HashSet<Temp> differenceOutDef = new HashSet<Temp>(oldOut);
                    differenceOutDef.removeAll(def);
                    HashSet<Temp> in = new HashSet<Temp>(use);
                    in.addAll(differenceOutDef);
                    this.nodeLiveIn.put(node, in);

                    NodeList nodeSuccessors = node.succ();
                    HashSet<Temp> out = new HashSet<Temp>();
                    for (NodeList nl = nodeSuccessors; nl != null; nl = nl.tail) {
                        Node successor = nl.head;
                        HashSet<Temp> successorIn = this.nodeLiveIn.get(successor);
                        out.addAll(successorIn);
                    }
                    this.nodeLiveOut.put(node, out);

                    if (!out.equals(oldOut) || !in.equals(oldIn))  fixedPointReached.setValue(false); 

                    });
        } while(!fixedPointReached.getValue());
    }

    
}


