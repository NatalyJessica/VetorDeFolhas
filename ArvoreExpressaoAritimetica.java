import java.util.*;

public class ArvoreExpressaoAritimetica {

    static class No {
        String valor;
        No esq, dir;

        No(String valor) {
            this.valor = valor;
            this.esq = null;
            this.dir = null;
        }
    }

    public static List<No> fragmentarExpressaoAririmetica(String expArit) {
        expArit = expArit.replace(" ", "");
        String delimitadores = "+-*/^()";
        StringTokenizer separador = new StringTokenizer(expArit, delimitadores, true);
        List<No> vetor = new ArrayList<>();

        while (separador.hasMoreTokens()) {
            String token = separador.nextToken();
            if (!token.trim().isEmpty()) {
                vetor.add(new No(token));
            }
        }

        return vetor;
    }

    private static void agruparNodos(List<No> vetor, String[] operadores) {
        for (int i = 0; i < vetor.size(); i++) {
            No atual = vetor.get(i);
            boolean isOperador = false;
            for (String op : operadores) {
                if (atual.valor.equals(op)) {
                    isOperador = true;
                    break;
                }
            }

            if (isOperador && i > 0 && i < vetor.size() - 1) {
                No esquerdo = vetor.get(i - 1);
                No direito = vetor.get(i + 1);

                atual.esq = esquerdo;
                atual.dir = direito;

                vetor.set(i - 1, atual);
                vetor.remove(i + 1);
                vetor.remove(i);
                i--;
            }
        }
    }

    private static boolean temParenteses(List<No> vetor) {
        for (No no : vetor) {
            if (no.valor.equals("(") || no.valor.equals(")")) {
                return true;
            }
        }
        return false;
    }

    /*public static No construirArvore(List<No> vetor) {
        for (;;) {
            if (!temParenteses(vetor)) {
                break;
            }

            int fechaParenteses = -1;
            for (int i = 0; i < vetor.size(); i++) {
                if (vetor.get(i).valor.equals(")")) {
                    fechaParenteses = i;
                    break;
                }
            }
            if (fechaParenteses == -1) break;

            int abreParenteses = -1;
            for (int i = fechaParenteses; i >= 0; i--) {
                if (vetor.get(i).valor.equals("(")) {
                    abreParenteses = i;
                    break;
                }
            }

            List<No> sub = new ArrayList<>();
            for (int i = abreParenteses + 1; i < fechaParenteses; i++) {
                sub.add(vetor.get(i));
            }

            agruparNodos(sub, new String[]{"^"});
            agruparNodos(sub, new String[]{"*", "/"});
            agruparNodos(sub, new String[]{"+", "-"});

            No subArvore = sub.get(0);

            vetor.set(abreParenteses, subArvore);
            for (int i = 0; i < fechaParenteses - abreParenteses; i++) {
                vetor.remove(abreParenteses + 1);
            }
        }

        agruparNodos(vetor, new String[]{"^"});
        agruparNodos(vetor, new String[]{"*", "/"});
        agruparNodos(vetor, new String[]{"+", "-"});

        return vetor.get(0);
    }*/

public static No construirArvore(List<No> vetor) {
    while (temParenteses(vetor)) {
        System.out.print("\n🔍 Vetor atual: ");
        for (No n : vetor) System.out.print(n.valor + " ");
        System.out.println();

        int abreParenteses = -1;
        int fechaParenteses = -1;

        // encontra o fechamento e abertura correspondente
        for (int i = 0; i < vetor.size(); i++) {
            if (vetor.get(i).valor.equals(")")) {
                fechaParenteses = i;
                for (int j = i - 1; j >= 0; j--) {
                    if (vetor.get(j).valor.equals("(")) {
                        abreParenteses = j;
                        break;
                    }
                }
                break;
            }
        }

        if (abreParenteses == -1 || fechaParenteses == -1)
            break;

        System.out.println("➡️ Resolvendo subexpressão entre índices " + abreParenteses + " e " + fechaParenteses);

        // cria sublista com o conteúdo interno
        List<No> sub = new ArrayList<>();
        for (int i = abreParenteses + 1; i < fechaParenteses; i++) {
            sub.add(vetor.get(i));
        }

        // monta a subárvore internamente
        No subArvore = construirArvore(sub);

        // remove o trecho e substitui pela subárvore
        for (int i = fechaParenteses; i >= abreParenteses; i--) {
            vetor.remove(i);
        }
        vetor.add(abreParenteses, subArvore);

        System.out.print("✅ Após resolver parêntese: ");
        for (No n : vetor) System.out.print(n.valor + " ");
        System.out.println();
    }

    // monta operações fora de parênteses
    agruparNodos(vetor, new String[]{"^"});
    agruparNodos(vetor, new String[]{"*", "/"});
    agruparNodos(vetor, new String[]{"+", "-"});

    return vetor.get(0);
}




    public static void printarArvore(No raiz) {
        printarArvoreRec(raiz, "", true);
    }

    private static void printarArvoreRec(No no, String espacos, boolean ultimo) {
        if (no == null) {
            return;
        }

        System.out.println(espacos + (ultimo ? "└── " : "├── ") + no.valor);

        printarArvoreRec(no.esq, espacos + (ultimo ? "    " : "│   "), false);
        printarArvoreRec(no.dir, espacos + (ultimo ? "    " : "│   "), true);
    }

    public static double avaliar(No raiz) throws Exception {
        if (raiz.esq == null && raiz.dir == null) {
            return Double.parseDouble(raiz.valor);
        }

        double esq = avaliar(raiz.esq);
        double dir = avaliar(raiz.dir);

        switch (raiz.valor) {
            case "+": return esq + dir;
            case "-": return esq - dir;
            case "*": return esq * dir;
            case "/": return esq / dir;
            case "^": return Math.pow(esq, dir);
            default: throw new IllegalArgumentException("Operador inválido: " + raiz.valor);
        }
    }
}

/*
 1= 3*(4+5^2)-8/2
 2= 4*(3+5^2)
 3= (100 / (5^2)) + (8 * 3)
 4= (3+4)*(2^3)-(6/(3+1))
 5= 6 * (2 + 3 ^ 2) - (12 / (4 + 2))


 */