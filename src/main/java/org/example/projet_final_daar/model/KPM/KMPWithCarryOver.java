package org.example.projet_final_daar.model.KPM;



public class KMPWithCarryOver {

    public static int[] createLST(String pattern) {
        int M = pattern.length();
        int[] lst = new int[M];  

        int length = 0;  // Longueur du préfixe le plus long
        int i = 1;
        lst[0] = 0;  // 

        while (i < M) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lst[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lst[length - 1];
                } else {
                    lst[i] = 0;
                    i++;
                }
            }
        }
        // Ajout de -1 au début du tableau LST
        //lst = appendAtBeginning(lst, -1);
        return lst;
    }

    public static int[] appendAtBeginning(int[] array, int value) {
        int[] newArray = new int[array.length + 1];
        newArray[0] = value;
        // System.arraycopy(array, 0, newArray, 1, array.length);
        return newArray;
    }

    public static int[] createCarryOver(String pattern, int[] lst) {
        // Ajout de -1 au début du tableau LST
        lst = appendAtBeginning(lst, -1);
        int len = pattern.length();
        for (int i = 1; i < len - 1; i++) {
            if (pattern.charAt(i) == pattern.charAt(lst[i])) {
                if (lst[lst[i]] == -1) {
                    lst[i] = -1;
                } else {
                    lst[i] = lst[lst[i]];
                }
            }
        }
        return lst;
    }

    public static void printIntTable(int[] tab){for(int i = 0; i < tab.length; i++){System.out.println(tab[i]);}}

    public static Boolean kmpRecherche(String texte, String motif) {
        int n = texte.length();
        int m = motif.length();
        
        if (m == 0 || m > n) {
            // System.out.println("Aucune correspondance trouvée.");
            return false;
        }

        int[] lst = createLST(motif);
        // System.out.println("Tableau LST : ");
        // printIntTable(lst);

        int[] carryover = createCarryOver(motif,lst);
        // System.out.println("CarryOver : ");
        // printIntTable(carryover);
        
        int i = 0;  // Index dans le texte
        int j = 0;  // Index dans le motif 
        
        while (i < n) {
            if (texte.charAt(i) == motif.charAt(j)) {
                i++;
                j++;
            }
            
            if (j == m) {
                // Le motif est trouvé à la position i - j
                // System.out.println("Motif trouvé à l'index " + (i - j));
                j = carryover[j - 1] + 1;  // Continuer la recherche avec les informations du tableau de retenue
                return true;

            } else if (i < n && texte.charAt(i) != motif.charAt(j)) {
                // Si les caractères ne correspondent pas
                if (j > 0) {
                    j = carryover[j - 1] + 1;  // On ajuste l'index j en fonction du tableau de retenue
                } else {
                    i++;  // Si j est 0, on avance seulement dans le texte
                }
            }
        }
        return false;
    }
}
