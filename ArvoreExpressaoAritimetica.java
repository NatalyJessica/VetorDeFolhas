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

      // Método único para validar toda a expressão
    public static void validarExpressao(String expArit) throws Exception {
        // 1. Valida se a expressão está vazia
        if (expArit == null || expArit.trim().isEmpty()) {
            throw new Exception("Expressão vazia ou nula");
        }

        // 2. Valida caracteres
        String caracteresValidos = "0123456789.+-*/^() ";
        for (char c : expArit.toCharArray()) {
            if (caracteresValidos.indexOf(c) == -1) {
                throw new Exception(
                    "Caractere inválido encontrado: '" + c + "'. Apenas números, operadores (+, -, *, /, ^) e parênteses são permitidos."
                );
            }
        }

        // 3. Valida parênteses balanceados
        int contador = 0;
        for (char c : expArit.toCharArray()) {
            if (c == '(') contador++;
            if (c == ')') contador--;
            if (contador < 0) {
                throw new Exception("Parênteses desbalanceados: ')' antes de '('");
            }
        }
        if (contador != 0) {
            throw new Exception("Parênteses desbalanceados: faltam " + 
                (contador > 0 ? "')'" : "'('"));
        }

        // 4. Valida parênteses vazios
        if (expArit.contains("()")) {
            throw new Exception("Parênteses vazios encontrados: ()");
        }

        // 5. Remove espaços e fragmenta para validações estruturais
        String expLimpa = expArit.replace(" ", "");
        String delimitadores = "+-*/^()";
        StringTokenizer separador = new StringTokenizer(expLimpa, delimitadores, true);
        List<No> vetorTemp = new ArrayList<>();

        while (separador.hasMoreTokens()) {
            String token = separador.nextToken();
            if (!token.trim().isEmpty()) {
                vetorTemp.add(new No(token));
            }
        }

        if (vetorTemp.isEmpty()) {
            throw new Exception("Expressão vazia após processamento");
        }

        // 6. Valida números
        String operadores = "+-*/^()";
        for (No no : vetorTemp) {
            if (!operadores.contains(no.valor)) {
                if (!ehNumero(no.valor)) {
                    throw new Exception("Número inválido: '" + no.valor + "'");
                }
            }
        }

        // 7. Valida operadores consecutivos
        for (int i = 0; i < vetorTemp.size() - 1; i++) {
            String atual = vetorTemp.get(i).valor;
            String proximo = vetorTemp.get(i + 1).valor;
            
            if (i == 0 && (atual.equals("-") || atual.equals("+"))) {
                if (!proximo.equals("(") && !ehNumero(proximo)) {
                    throw new Exception(
                        "Operador '" + atual + "' no início deve ser seguido de número ou '('"
                    );
                }
                continue;
            }
            
            if (atual.equals("(") && (proximo.equals("-") || proximo.equals("+"))) {
                if (i + 2 < vetorTemp.size()) {
                    String depoisDoSinal = vetorTemp.get(i + 2).valor;
                    if (!depoisDoSinal.equals("(") && !ehNumero(depoisDoSinal)) {
                        throw new Exception(
                            "Operador '" + proximo + "' após '(' deve ser seguido de número ou '('"
                        );
                    }
                }
                continue;
            }
            
            if (operadores.contains(atual) && operadores.contains(proximo)) {
                throw new Exception(
                    "Operadores consecutivos encontrados: '" + atual + "' e '" + proximo + "'"
                );
            }
        }

        // 8. Valida primeiro elemento
        String primeiro = vetorTemp.get(0).valor;
        if (operadores.contains(primeiro) && !primeiro.equals("-") && !primeiro.equals("+")) {
            throw new Exception(
                "Expressão começa com operador inválido: '" + primeiro + "'"
            );
        }

        // 9. Valida último elemento
        String ultimo = vetorTemp.get(vetorTemp.size() - 1).valor;
        if (operadores.contains(ultimo)) {
            throw new Exception(
                "Expressão termina com operador: '" + ultimo + "'"
            );
        }

        // 10. Valida operadores com parênteses
        for (int i = 0; i < vetorTemp.size(); i++) {
            String atual = vetorTemp.get(i).valor;
            
            if (atual.equals("(") && i < vetorTemp.size() - 1) {
                String proximo = vetorTemp.get(i + 1).valor;
                if (operadores.contains(proximo) && !proximo.equals("-") && !proximo.equals("+")) {
                    throw new Exception(
                        "Operador inválido após '(': '" + proximo + "'"
                    );
                }
            }
            
            if (atual.equals(")") && i > 0) {
                String anterior = vetorTemp.get(i - 1).valor;
                if (operadores.contains(anterior)) {
                    throw new Exception(
                        "Operador antes de ')': '" + anterior + "'"
                    );
                }
            }
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