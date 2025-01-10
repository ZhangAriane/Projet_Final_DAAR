package org.example.projet_final_daar.model.automaton;


import java.util.ArrayList;

public class RegExTree {
    protected int root;
    protected ArrayList<RegExTree> subTrees;
    public RegExTree(int root, ArrayList<RegExTree> subTrees) {
      this.root = root;
      this.subTrees = subTrees;
    }

  
    //FROM TREE TO PARENTHESIS
    public String toString() {
      if (subTrees.isEmpty()) return rootToString();
      String result = rootToString()+"("+subTrees.get(0).toString();
      for (int i=1;i<subTrees.size();i++) result+=","+subTrees.get(i).toString();
      return result+")";
    }
    private String rootToString() {
      if (root==RegEx.CONCAT) return ".";
      if (root==RegEx.ETOILE) return "*";
      if (root==RegEx.ALTERN) return "|";
      if (root==RegEx.DOT) return ".";
      return Character.toString((char)root);
    }

  public static NDFAutomaton convertTreetoNDF(RegExTree ret) {

    if (ret.subTrees.isEmpty()) {
        // L'ÉTAT INITIAL EST TOUJOURS 0 ; 
        int[][] tTab = new int[2][256];
        ArrayList<Integer>[] eTab = new ArrayList[2];

        
        for (int i = 0; i < tTab.length; i++) for (int col = 0; col < 256; col++) tTab[i][col] = -1;
        for (int i = 0; i < eTab.length; i++) eTab[i] = new ArrayList<Integer>();

        if (ret.root != RegEx.DOT) tTab[0][ret.root] = 1; // transition depuis l'état initial "0" vers l'état final "1" avec la racine ret.root
        else
            for (int i = 0; i < 256; i++) tTab[0][i] = 1; // transition DOT depuis l'état initial "0" vers l'état final "1"

        return new NDFAutomaton(tTab, eTab);
    }

    if (ret.root == RegEx.CONCAT) {
        NDFAutomaton gauche = convertTreetoNDF(ret.subTrees.get(0));
        int[][] tTab_g = gauche.transitionTable;
        ArrayList<Integer>[] eTab_g = gauche.epsilonTransitionTable;


        NDFAutomaton droite = convertTreetoNDF(ret.subTrees.get(1));
        int[][] tTab_d = droite.transitionTable;
        ArrayList<Integer>[] eTab_d = droite.epsilonTransitionTable;

         
        int lg = tTab_g.length;
        int ld = tTab_d.length;
        int[][] tTab = new int[lg + ld][256];
        ArrayList<Integer>[] eTab = new ArrayList[lg + ld];

        // INITIALISATION
        for (int i = 0; i < tTab.length; i++) for (int col = 0; col < 256; col++) tTab[i][col] = -1;
        for (int i = 0; i < eTab.length; i++) eTab[i] = new ArrayList<Integer>();

        // transition epsilon de l'ancien état final "gauche" à l'ancien état initial "droite"
        eTab[lg - 1].add(lg); 

        // copier les anciennes transitions
        for (int i = 0; i < lg; i++)
            for (int col = 0; col < 256; col++) tTab[i][col] = tTab_g[i][col]; 

        for (int i = 0; i < lg; i++) eTab[i].addAll(eTab_g[i]); 

        for (int i = lg; i < lg + ld - 1; i++)
            for (int col = 0; col < 256; col++)
                if (tTab_d[i - lg][col] != -1) tTab[i][col] = tTab_d[i - lg][col] + lg; 

        for (int i = lg; i < lg + ld - 1; i++)
            for (int s : eTab_d[i - lg]) eTab[i].add(s + lg);

        return new NDFAutomaton(tTab, eTab);
    }

    if (ret.root == RegEx.ALTERN) {
        NDFAutomaton gauche = convertTreetoNDF(ret.subTrees.get(0));
        int[][] tTab_g = gauche.transitionTable;
        ArrayList<Integer>[] eTab_g = gauche.epsilonTransitionTable;


        NDFAutomaton droite = convertTreetoNDF(ret.subTrees.get(1));
        int[][] tTab_d = droite.transitionTable;
        ArrayList<Integer>[] eTab_d = droite.epsilonTransitionTable;


        int lg = tTab_g.length;
        int ld = tTab_d.length;

        int[][] tTab = new int[2 + lg + ld][256];
        ArrayList<Integer>[] eTab = new ArrayList[2 + lg + ld];

        // INITIALISATION
        for (int i = 0; i < tTab.length; i++) for (int col = 0; col < 256; col++) tTab[i][col] = -1;
        for (int i = 0; i < eTab.length; i++) eTab[i] = new ArrayList<Integer>();


        // transition epsilon de l'ancien état final à l'ancien état initial
        eTab[0].add(1); 
        eTab[0].add(1 + lg); 
        eTab[1 + lg - 1].add(2 + lg + ld - 1); 
        eTab[1 + lg + ld - 1].add(2 + lg + ld - 1); 


        // copier les anciennes transitions
        for (int i = 1; i < 1 + lg; i++)
            for (int col = 0; col < 256; col++)
                if (tTab_g[i - 1][col] != -1) tTab[i][col] = tTab_g[i - 1][col] + 1; 


        for (int i = 1; i < 1 + lg; i++) 
            for (int s : eTab_g[i - 1]) eTab[i].add(s + 1); 

        for (int i = 1 + lg; i < 1 + lg + ld - 1; i++)
            for (int col = 0; col < 256; col++)
                if (tTab_d[i - 1 - lg][col] != -1)
                    tTab[i][col] = tTab_d[i - 1 - lg][col] + 1 + lg; 

        for (int i = 1 + lg; i < 1 + lg + ld; i++)
            for (int s : eTab_d[i - 1 - lg]) eTab[i].add(s + 1 + lg); 

        //

        return new NDFAutomaton(tTab, eTab);
    }

    if (ret.root == RegEx.ETOILE) {
        NDFAutomaton fils = convertTreetoNDF(ret.subTrees.get(0));
        int[][] tTab_fils = fils.transitionTable;
        ArrayList<Integer>[] eTab_fils = fils.epsilonTransitionTable;
        int l = tTab_fils.length;
        int[][] tTab = new int[2 + l][256];
        ArrayList<Integer>[] eTab = new ArrayList[2 + l];

        // INITIALISATION
        for (int i = 0; i < tTab.length; i++) for (int col = 0; col < 256; col++) tTab[i][col] = -1;
        for (int i = 0; i < eTab.length; i++) eTab[i] = new ArrayList<Integer>();


        // transition epsilon de l'ancien état final à l'ancien état initial
        eTab[0].add(1); 
        eTab[0].add(2 + l - 1); 
        eTab[2 + l - 2].add(2 + l - 1); 
        eTab[2 + l - 2].add(1); 



        // copier les anciennes transitions
        for (int i = 1; i < 2 + l - 1; i++)
            for (int col = 0; col < 256; col++)
                if (tTab_fils[i - 1][col] != -1) tTab[i][col] = tTab_fils[i - 1][col] + 1; 
        for (int i = 1; i < 2 + l - 1; i++)
            for (int s : eTab_fils[i - 1]) eTab[i].add(s + 1); 

        return new NDFAutomaton(tTab, eTab);
    }

    return null;
}



}


  