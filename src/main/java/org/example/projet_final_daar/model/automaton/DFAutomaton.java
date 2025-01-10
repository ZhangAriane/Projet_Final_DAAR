package org.example.projet_final_daar.model.automaton;
import java.util.ArrayList;
import java.util.HashMap;


public class DFAutomaton {
    protected int[][] transitionTable; // Tableau des transitions du DFA
    protected ArrayList<Integer> finalStates; // Liste des états finaux du DFA

    public DFAutomaton(int[][] transitionTable, ArrayList<Integer> finalStates) {
        this.transitionTable = transitionTable;
        this.finalStates = finalStates;
    }

    public String toString() {
        // L'état initial est toujours 0
        String result = "Initial state: 0\n";  
        result += "Final states: " + finalStates + "\n";
        result += "Transitions:\n";

        int n = transitionTable.length;
    
        for (int state = 0; state < n; state++) {
            for (int symbol = 0; symbol < 256; symbol++) {
                if (transitionTable[state][symbol] != -1) {
                    result += "  " + state + " -- " + (char) symbol + " --> " + transitionTable[state][symbol] + "\n";
                }
            }
        }
    
        return result;
    }

    public static int[][] removeRows(int[][] table, int[] rowsToRemove) {
        int[][] result = new int[table.length - rowsToRemove.length][];
    
        int newIndex = 0;
        for (int i = 0; i < table.length; i++) {
            boolean remove = false;
            for (int row : rowsToRemove) {
                if (i == row) {
                    remove = true;
                    break;
                }
            }
    
            // If the index should not be removed, copy it into the new table
            if (!remove) {
                result[newIndex] = table[i];
                newIndex++;
            }
        }
        return result;
    }
    
    public DFAutomaton minimizeDFA() {
    
        // Step 1: Clean transitionTable
        
        // Determine the largest reachable state
        int maxState = -1;
        for (int state = 0; state < transitionTable.length; state++) {
            for (int symbol = 0; symbol < 256; symbol++) {
                if (transitionTable[state][symbol] != -1 && maxState < transitionTable[state][symbol]) {
                    maxState = transitionTable[state][symbol];
                }
            }
        }
    
        // Remove unused rows (states unreachable after the largest state)
        int[] rowsToRemove = new int[transitionTable.length - maxState - 1];
        rowsToRemove[0] = maxState + 1;
    
        for (int i = 1; i < rowsToRemove.length; i++) {
            rowsToRemove[i] = rowsToRemove[i - 1] + 1;
        }
    
        int[][] dfaTransitionTable = removeRows(this.transitionTable, rowsToRemove);
    
        ArrayList<Integer> finalStates = this.finalStates;
        int numStates = dfaTransitionTable.length;
    
        // Step 2: Separate final and non-final states
        ArrayList<Integer> finalGroup = new ArrayList<>();
        ArrayList<Integer> nonFinalGroup = new ArrayList<>();
        for (int i = 0; i < numStates; i++) {
            if (finalStates.contains(i)) {
                finalGroup.add(i);
            } else {
                nonFinalGroup.add(i);
            }
        }
    
        // Create the initial partitions (final and non-final states)
        ArrayList<ArrayList<Integer>> partitions = new ArrayList<>();
        partitions.add(finalGroup);
        partitions.add(nonFinalGroup);
    
        // Step 3: Refine the partitions
        boolean changed;
        do {
            changed = false;
            ArrayList<ArrayList<Integer>> newPartitions = new ArrayList<>();
    
            for (ArrayList<Integer> partition : partitions) {
                // Divide the current partition based on transitions
                HashMap<ArrayList<Integer>, ArrayList<Integer>> splitMap = new HashMap<>();
                for (int state : partition) {
                    ArrayList<Integer> transitionPattern = new ArrayList<>();
                    for (int symbol = 0; symbol < 256; symbol++) {
                        int nextState = dfaTransitionTable[state][symbol];



                        // Find which partition the transition state belongs to
                        int nextPartition = findPartitionIndex(partitions, nextState);
                        transitionPattern.add(nextPartition);
                    }
                
                    splitMap.putIfAbsent(transitionPattern, new ArrayList<>());
                    splitMap.get(transitionPattern).add(state);
                }


                newPartitions.addAll(splitMap.values());
                if (splitMap.size() > 1) {
                    changed = true;  
                }
            }
            partitions = newPartitions;
        } while (changed);
    
        // Step 4: Renumber minimized states, keeping the initial state at 0
        HashMap<Integer, Integer> stateMapping = new HashMap<>();
        int initialState = 0;
        int initialPartitionIndex = findPartitionIndex(partitions, initialState);
        stateMapping.put(initialPartitionIndex, 0); // The initial state remains 0
    
        int newStateCounter = 1;
    
        // Assign state numbers to the other partitions
        for (int i = 0; i < partitions.size(); i++) {
            if (!stateMapping.containsKey(i)) {
                stateMapping.put(i, newStateCounter++);
            }
        }
    
        // Step 5: Rebuild the minimized DFA transition table with the correct state numbers
        int[][] minimizedTransitionTable = new int[partitions.size()][256];
        for (int i = 0; i < partitions.size(); i++) {
            ArrayList<Integer> partition = partitions.get(i);
            int representativeState = partition.get(0);  
            for (int symbol = 0; symbol < 256; symbol++) {
                int nextState = dfaTransitionTable[representativeState][symbol];
                if (nextState != -1) {
                    int nextPartition = findPartitionIndex(partitions, nextState);
                    if (nextPartition != -1) {  
                        minimizedTransitionTable[stateMapping.get(i)][symbol] = stateMapping.get(nextPartition);
                    }
                } else {
                    minimizedTransitionTable[stateMapping.get(i)][symbol] = -1;
                }
            }
        }
    
        // Step 6: Identify the new final states
        ArrayList<Integer> minimizedFinalStates = new ArrayList<>();
        for (int i = 0; i < partitions.size(); i++) {
            for (int state : partitions.get(i)) {
                if (finalStates.contains(state)) {
                    minimizedFinalStates.add(stateMapping.get(i));
                    break;
                }
            }
        }
    
        return new DFAutomaton(minimizedTransitionTable, minimizedFinalStates);
    }
    
    // To find the index of a partition given a state
    private int findPartitionIndex(ArrayList<ArrayList<Integer>> partitions, int state) {
        if (state == -1) {
            return -1; 
        }
        for (int i = 0; i < partitions.size(); i++) {
            if (partitions.get(i).contains(state)) {
                return i;
            }
        }
        return -1;  
    }
    
    
    
    
    public boolean search(String text) {
        boolean b = false;
        int n = text.length();

        //si l'état 0 est l'état final'
        if(this.finalStates.contains(0))
            return true;

        for (int i = 0; i < n; i++) {
            int currentState = 0;  
                for (int j = i; j < n; j++) {
                    char symbol = text.charAt(j);
                    currentState = this.transitionTable[currentState][(int) symbol];
        
                    if (currentState == -1) {break;}

                    // return true si on arrive à l'état final
                    if (this.finalStates.contains(currentState)) {return b = true;}
            }
        }
        
        return b;
    }
}

