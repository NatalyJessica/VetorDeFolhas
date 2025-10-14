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

    public static No construirArvore(List<No> vetor) {
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
