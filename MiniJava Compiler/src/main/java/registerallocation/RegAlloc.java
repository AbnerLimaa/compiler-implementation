package registerallocation;

import registerallocation.livenessanalysis.AssemFlowGraph;
import registerallocation.livenessanalysis.Liveness;
import instructionselection.mips.MipsFrame;
import utils.assem.Instr;
import utils.frame.Frame;
import utils.graph.Node;
import utils.graph.NodeList;
import utils.graph.regalloc.InterferenceGraph;
import utils.temp.*;

import java.util.*;

public class RegAlloc implements TempMap {

    private Frame frame;
    private List<Instr> instrs;
    private InterferenceGraph interferenceGraph;

    private Set<Temp> precolored = new HashSet<>();
    private Set<Temp> initial = new HashSet<>();
    private List<Node> simplifyWorkList = new ArrayList<>();
    private List<Node> spillWorkList = new ArrayList<>();
    private List<Node> spilledNodes = new ArrayList<>();
    private List<Temp> coloredNodes = new ArrayList<>();
    private Stack<Node> selectStack = new Stack<>();

    private Map<Node, Temp> color = new HashMap<>();

    private int K;

    public RegAlloc(Frame f, List<Instr> il) {
        this.frame = f;
        this.instrs = il;
        List<Temp> specials = Arrays.asList(this.frame.specialRegisters().createTempArray());
        if (this.instrs != null) {
            for (Instr instr : this.instrs) {
                TempList useList = instr.use();
                if (useList != null)
                    populateLists(specials, useList.createTempSet());
                TempList defList = instr.def();
                if (defList != null)
                    populateLists(specials, defList.createTempSet());
            }
        }
        this.K = this.frame.registers().totalSize();
    }

    private void populateLists(List<Temp> specials, Set<Temp> set) {
        for (Temp temp : set) {
            if (temp != null) {
                if (!specials.contains(temp))
                    this.initial.add(temp);
                else
                    this.precolored.add(temp);
            }
        }
    }

    public List<Instr> getInstrs() {
        return this.instrs;
    }

    public void printInstrs() {
        for (Instr instr : this.instrs) {
            System.out.println(instr.format(this));
        }
    }

    public List<String> generateCode() {
        List<String> code = new ArrayList<>();
        for (Instr instr : this.instrs) {
            String codeFormatted = instr.format(this);
            if (!codeFormatted.contains("null"))
                code.add(codeFormatted);
        }
        return code;
    }

    public void main() {
        livenessAnalysis();
        makeWorkList();
        do {
            if (!simplifyWorkList.isEmpty())
                simplify();
            if (!spillWorkList.isEmpty())
                selectSpill();
        } while (condition());
        assignColors();
        if (!spilledNodes.isEmpty()) {
            rewriteProgram();
            main();
        }
    }

    private boolean condition() {
        return !simplifyWorkList.isEmpty() || !spillWorkList.isEmpty();
    }

    private void livenessAnalysis() {
        AssemFlowGraph flowGraph = new AssemFlowGraph(instrs);
        flowGraph.solveDataFlowEquation();
        this.interferenceGraph = new Liveness(flowGraph);
    }

    private void makeWorkList() {
        Iterator<Temp> iterator = initial.iterator();
        while (iterator.hasNext()) {
            Temp t = iterator.next();
            if (t != null) {
                Node n = this.interferenceGraph.tnode(t);
                if (n != null) {
                    if (n.degree() >= K)
                        this.spillWorkList.add(n);
                    else
                        this.simplifyWorkList.add(n);
                }
            }
            iterator.remove();
        }
    }

    private void simplify() {
        if (simplifyWorkList != null && !simplifyWorkList.isEmpty()) {
            Node n = simplifyWorkList.get(0);
            simplifyWorkList.remove(n);
            selectStack.push(n);
            for (Node m : NodeList.transformIntoJavaList(n.adj())) {
                decrementDegree(m);
                m.pred().removeNode(n);
            }
        }
    }

    private void selectSpill() {
        if (!spillWorkList.isEmpty()) {
            int bound = spillWorkList.size();
            int position = new Random().nextInt(bound);
            Node m = spillWorkList.get(position);
            spillWorkList.remove(m);
            simplifyWorkList.add(m);
        }
    }

    private void decrementDegree(Node m) {
        int d = m.degree();
        if (d == K) {
            spillWorkList.remove(m);
            simplifyWorkList.add(m);
        }
    }

    private void assignColors() {
        while (!selectStack.empty()) {
            Node n = selectStack.pop();
            List<Temp> registers = new ArrayList<>(Arrays.asList(this.frame.registers().createTempArray()));
            registers.remove(MipsFrame.AT);
            for (Node m : NodeList.transformIntoJavaList(n.adj())) {
                Temp t = interferenceGraph.gtemp(m);
                if (coloredNodes.contains(t) || precolored.contains(t)) {
                    registers.remove(color.get(m));
                }
            }
            if (registers.isEmpty()) {
                spilledNodes.add(n);
            }
            else {
                Temp t = interferenceGraph.gtemp(n);
                coloredNodes.add(t);
                int bound = registers.size();
                int position = new Random().nextInt(bound);
                if (registers.indexOf(t) != -1) color.put(n, registers.get(registers.indexOf(t)));
                else {
                    Temp register = registers.get(position);
                    color.put(n, register);
                }
            }
        }
    }

    private void rewriteProgram() {
        if (spilledNodes != null) {
            List<Temp> spills = new ArrayList<>();
            for (Node n : spilledNodes) {
                Temp t = interferenceGraph.gtemp(n);
                if (t != null)
                    spills.add(t);
            }
            List<Temp> newTemps = frame.spill(instrs, spills);
            spilledNodes = new ArrayList<>();
            initial.addAll(coloredNodes);
            initial.addAll(newTemps);
            coloredNodes = new ArrayList<>();
        }
    }

    public String tempMap(Temp temp) {
        String result = "null";
        for (Node n : color.keySet()) {
            Temp tempGraph = interferenceGraph.gtemp(n);
            if (tempGraph != null && temp != null && tempGraph.hashCode() == temp.hashCode()) {
                Temp tempAssigned = color.get(n);
                result = frame.tempMap(tempAssigned);
            }
        }
        return result;
    }
}
