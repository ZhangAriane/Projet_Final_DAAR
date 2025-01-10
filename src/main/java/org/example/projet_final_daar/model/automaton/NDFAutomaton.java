package org.example.projet_final_daar.model.automaton;
import java.util.ArrayList;
import java.util.List;

public class NDFAutomaton {
    // INIT STATE IS ALWAYS 0
    // FINAL STATE IS ALWAYS transitionTable.length-1
    protected int[][] transitionTable; //ASCII transition
    protected ArrayList<Integer>[] epsilonTransitionTable; //epsilon transition list

    public NDFAutomaton(int[][] transitionTable, ArrayList<Integer>[] epsilonTransitionTable) {
        this.transitionTable = transitionTable;
        this.epsilonTransitionTable = epsilonTransitionTable;
    }

    public String toString() {
        String result = "Initial state: 0\nFinal state: " + (transitionTable.length - 1) + "\nTransition list:\n";
        for (int i = 0; i < epsilonTransitionTable.length; i++)
            for (int state : epsilonTransitionTable[i])
                result += "  " + i + " -- epsilon --> " + state + "\n";
        for (int i = 0; i < transitionTable.length; i++)
            for (int col = 0; col < 256; col++)
                if (transitionTable[i][col] != -1)
                    result += "  " + i + " -- " + (char) col + " --> " + transitionTable[i][col] + "\n";
        return result;
    }


  public DFAutomaton convertNFAtoDFA() {
    int numNFAStates = this.transitionTable.length;
    ArrayList<ArrayList<Integer>> dfaStates = new ArrayList<>(); // Liste des états du DFA (combinaisons d'états NFA)
    ArrayList<Integer> finalStatesDFA = new ArrayList<>();

    // L'état initial du DFA est la fermeture epsilon de l'état 0 du NFA
    ArrayList<Integer> startState = epsilonClosure(new ArrayList<>(List.of(0)));
    dfaStates.add(startState);


    int[][] dfaTransitionTable = new int[numNFAStates][256]; 
    for (int i = 0; i < dfaTransitionTable.length; i++) {
        for (int j = 0; j < 256; j++) {
            dfaTransitionTable[i][j] = -1; // Initialiser les transitions à -1 (pas de transition)
        }
    }

    for (int i = 0; i < dfaStates.size(); i++) {
        ArrayList<Integer> currentDFAState = dfaStates.get(i);

        // Vérifier si un des états NFA dans cet état DFA est un état final
        for (int nfaState : currentDFAState) {
            if (nfaState == numNFAStates - 1) { 
                finalStatesDFA.add(i);
                break;
            }
        }

        for (int symbol = 0; symbol < 256; symbol++) {
            ArrayList<Integer> nextDFAState = new ArrayList<>();

            // Calculer l'ensemble des états NFA accessibles depuis l'état actuel du DFA sur ce symbole
            for (int nfaState : currentDFAState) {
                int nextState = transitionTable[nfaState][symbol];
                if (nextState != -1) {
                    nextDFAState.add(nextState);
                }
            }

            // Calculer la fermeture epsilon des états atteints
            nextDFAState = epsilonClosure(nextDFAState);

            // Vérifier si cet état est déjà dans la liste des états DFA
            int existingStateIndex = findState(dfaStates, nextDFAState);
            if (existingStateIndex == -1 && !nextDFAState.isEmpty()) {
                dfaStates.add(nextDFAState);
                existingStateIndex = dfaStates.size() - 1;
            }

            // Ajouter la transition dans la table de transition DFA
            if (existingStateIndex != -1) {
                dfaTransitionTable[i][symbol] = existingStateIndex;
            }
        }
    }

    return new DFAutomaton(dfaTransitionTable, finalStatesDFA);
}

// Calculer la fermeture epsilon (epsilon-closure)
private ArrayList<Integer> epsilonClosure(ArrayList<Integer> states) {
    ArrayList<Integer> closure = new ArrayList<>(states);

    for (int i = 0; i < closure.size(); i++) {
        int state = closure.get(i);

        // Ajouter tous les états accessibles via une transition epsilon
        for (int epsilonState : this.epsilonTransitionTable[state]) {
            if (!closure.contains(epsilonState)) {
                closure.add(epsilonState);
            }
        }
    }

    return closure;
}

// Chercher l'ensemble d'états dans la liste des états DFA
private int findState(ArrayList<ArrayList<Integer>> dfaStates, ArrayList<Integer> state) {
    for (int i = 0; i < dfaStates.size(); i++) { if (dfaStates.get(i).equals(state)) { return i;}}
    return -1;
}

}
