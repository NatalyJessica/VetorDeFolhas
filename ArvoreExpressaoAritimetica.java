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

    public static void validarExpressao(String expArit) throws Exception {
        if (expArit == null || expArit.trim().isEmpty())
            throw new Exception("Expressão vazia ou nula");
    
        expArit = expArit.replaceAll("\\s+", ""); // remove espaços
        String operadores = "+-*/^";
        int contParenteses = 0;
        char prev = '\0';
    
        for (int i = 0; i < expArit.length(); i++) {
            char c = expArit.charAt(i);
            char next = (i + 1 < expArit.length()) ? expArit.charAt(i + 1) : '\0';
    
            if (!Character.isDigit(c) && operadores.indexOf(c) == -1 && c != '(' && c != ')') {
                throw new Exception("Caractere inválido: '" + c + "' na posição " + i);
            }
    
            if (c == '(') {
                contParenteses++;
                if (next == ')')
                    throw new Exception("Parêntese vazio '()' em posição " + i);
            } else if (c == ')') {
                contParenteses--;
                if (contParenteses < 0)
                    throw new Exception("Parêntese ')' sem abertura em posição " + i);
                if (operadores.indexOf(prev) != -1)
                    throw new Exception("Operador '" + prev + "' antes de ')' em posição " + (i - 1));
            }
    
            if (operadores.indexOf(c) != -1) {
                if (i == 0 || i == expArit.length() - 1)
                    throw new Exception("Operador '" + c + "' em posição inválida");
                if (operadores.indexOf(prev) != -1)
                    throw new Exception("Operadores consecutivos: '" + prev + c + "' em posição " + (i - 1));
                if (prev == '(')
                    throw new Exception("Operador '" + c + "' após '(' em posição " + i);
                if (next == ')')
                    throw new Exception("Operador '" + c + "' antes de ')' em posição " + i);
            }
    
            if (Character.isDigit(prev) && c == '(')
                throw new Exception("Falta operador entre número e '(' em posição " + (i - 1));
    
            if (prev == ')' && Character.isDigit(c))
                throw new Exception("Falta operador entre ')' e número em posição " + (i - 1));
    
            prev = c;
        }
    
        if (contParenteses != 0)
            throw new Exception("Parênteses desbalanceados");
    }
    

    public static List<No> fragmentarExpressaoAririmetica(String expArit) throws Exception {
        // Valida antes de fragmentar
        validarExpressao(expArit);

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

        boolean potencia = Arrays.asList(operadores).contains("^");
        // tratar ^ da direita para a esquerda, porque quaso tratado da esquerda para
        // direta o resultado fica errado(2^3 fica 3^2)
        if (potencia) {
            for (int i = vetor.size() - 1; i >= 0; i--) {
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
        } else {
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
        boolean modificado;
        do {
            modificado = false;

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

            if (fechaParenteses == -1)
                break;

            int abreParenteses = -1;
            for (int i = fechaParenteses; i >= 0; i--) {
                if (vetor.get(i).valor.equals("(")) {
                    abreParenteses = i;
                    break;
                }
            }

            if (abreParenteses == -1)
                break;

            List<No> sub = new ArrayList<>();
            for (int i = abreParenteses + 1; i < fechaParenteses; i++) {
                sub.add(vetor.get(i));
            }

            agruparNodos(sub, new String[] { "^" });
            agruparNodos(sub, new String[] { "*", "/" });
            agruparNodos(sub, new String[] { "+", "-" });

            No subArvore = sub.get(0);

            vetor.set(abreParenteses, subArvore);
            // Remover os elementos de trás para frente (inclusive o ')')
            for (int i = fechaParenteses; i > abreParenteses; i--) {
                vetor.remove(i);
            }

            modificado = true;
        } while (modificado);

        agruparNodos(vetor, new String[] { "^" });
        agruparNodos(vetor, new String[] { "*", "/" });
        agruparNodos(vetor, new String[] { "+", "-" });

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
            case "+":
                return esq + dir;
            case "-":
                return esq - dir;
            case "*":
                return esq * dir;
            case "/":
                return esq / dir;
            case "^":
                return Math.pow(esq, dir);
            default:
                throw new IllegalArgumentException("Operador inválido: " + raiz.valor);
        }
    }
}
