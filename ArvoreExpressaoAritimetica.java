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



    // Valida se a expressão contém apenas caracteres válidos
    private static void validarCaracteres(String expArit) throws Exception {
        if (expArit == null || expArit.trim().isEmpty()) {
            throw new Exception("Expressão vazia ou nula");
        }

        String caracteresValidos = "0123456789.+-*/^() ";
        for (char c : expArit.toCharArray()) {
            if (caracteresValidos.indexOf(c) == -1) {
                throw new Exception(
                    "Caractere inválido encontrado: '" + c + "'. Apenas números, operadores (+, -, *, /, ^) e parênteses são permitidos."
                );
            }
        }
    }

    // Valida se os parênteses estão balanceados
    private static void validarParenteses(String expArit) throws Exception {
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
    }

    // Valida se não há parênteses vazios
    private static void validarParentesesVazios(String expArit) throws Exception {
        if (expArit.contains("()")) {
            throw new Exception("Parênteses vazios encontrados: ()");
        }
    }

    // Valida operadores consecutivos
    private static void validarOperadoresConsecutivos(List<No> vetor) throws Exception {
        String operadores = "+-*/^";
        for (int i = 0; i < vetor.size() - 1; i++) {
            String atual = vetor.get(i).valor;
            String proximo = vetor.get(i + 1).valor;
            
            // Permite "-" ou "+" no início como sinal
            if (i == 0 && (atual.equals("-") || atual.equals("+"))) {
                continue;
            }
            
            // Permite "-" ou "+" após "("
            if (atual.equals("(") && (proximo.equals("-") || proximo.equals("+"))) {
                continue;
            }
            
            if (operadores.contains(atual) && operadores.contains(proximo)) {
                throw new Exception(
                    "Operadores consecutivos encontrados: '" + atual + "' e '" + proximo + "'"
                );
            }
        }
    }

    // Valida se operadores têm operandos
    private static void validarOperandos(List<No> vetor) throws Exception {
        String operadores = "+-*/^";
        
        if (vetor.isEmpty()) {
            throw new Exception("Expressão vazia após processamento");
        }

        // Verifica primeiro elemento
        if (vetor.size() > 0) {
            String primeiro = vetor.get(0).valor;
            if (operadores.contains(primeiro) && !primeiro.equals("-") && !primeiro.equals("+")) {
                throw new Exception(
                    "Expressão começa com operador inválido: '" + primeiro + "'"
                );
            }
        }

        // Verifica último elemento
        if (vetor.size() > 0) {
            String ultimo = vetor.get(vetor.size() - 1).valor;
            if (operadores.contains(ultimo)) {
                throw new Exception(
                    "Expressão termina com operador: '" + ultimo + "'"
                );
            }
        }

        // Verifica operadores com parênteses
        for (int i = 0; i < vetor.size(); i++) {
            String atual = vetor.get(i).valor;
            
            if (atual.equals("(") && i < vetor.size() - 1) {
                String proximo = vetor.get(i + 1).valor;
                if (operadores.contains(proximo) && !proximo.equals("-") && !proximo.equals("+")) {
                    throw new Exception(
                        "Operador inválido após '(': '" + proximo + "'"
                    );
                }
            }
            
            if (atual.equals(")") && i > 0) {
                String anterior = vetor.get(i - 1).valor;
                if (operadores.contains(anterior)) {
                    throw new Exception(
                        "Operador antes de ')': '" + anterior + "'"
                    );
                }
            }
        }
    }
    // Valida formato de números
    private static void validarNumeros(List<No> vetor) throws Exception {
        String operadores = "+-*/^()";
        
        for (No no : vetor) {
            if (!operadores.contains(no.valor)) {
                try {
                    Double.parseDouble(no.valor);
                } catch (NumberFormatException e) {
                    throw new Exception(
                        "Número inválido: '" + no.valor + "'"
                    );
                }
            }
        }
    }

    public static List<No> fragmentarExpressaoAririmetica(String expArit) throws Exception {
        // Validações iniciais
        validarCaracteres(expArit);
        validarParenteses(expArit);
        validarParentesesVazios(expArit);
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

        // Validações da estrutura
        validarOperadoresConsecutivos(vetor);
        validarOperandos(vetor);
        validarNumeros(vetor);

        return vetor;
    }

    private static void agruparNodos(List<No> vetor, String[] operadores) {
        System.out.println("iniciouVetor");
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

    public static No construirArvore(List<No> vetor) throws Exception {
        if (vetor == null || vetor.isEmpty()) {
            throw new Exception("Vetor de nós vazio");
        }
    // Processar parênteses de dentro para fora
    while (temParenteses(vetor)) {
        for (int i = 0; i < vetor.size(); i++) {
            if (vetor.get(i).valor.equals("(")) {
                int abre = i;
                int fecha = -1;
                int balance = 1;
                
                // Encontra o fechamento correspondente
                for (int j = abre + 1; j < vetor.size(); j++) {
                    if (vetor.get(j).valor.equals("(")) balance++;
                    if (vetor.get(j).valor.equals(")")) balance--;
                    if (balance == 0) {
                        fecha = j;
                        break;
                    }
                }
                
                    if (fecha == -1) {
                        throw new Exception("Parênteses não balanceados");
                    }
                
                // Extrai sublista
                List<No> sub = new ArrayList<>(vetor.subList(abre + 1, fecha));
                    if (sub.isEmpty()) {
                        throw new Exception("Parênteses vazios");
                    }
                No subArvore = construirArvore(sub);
                
                // Remove trecho ( ... ) e substitui pela subárvore
                for (int k = fecha; k >= abre; k--) {
                    vetor.remove(k);
                }
                vetor.add(abre, subArvore);
                
                // Sai do for para recomeçar a busca por parênteses
                break;
            }
        }
    }

    // Agrupa operadores fora de parênteses (ordem de precedência)
    agruparNodos(vetor, new String[]{"^"});
    agruparNodos(vetor, new String[]{"*", "/"});
    agruparNodos(vetor, new String[]{"+", "-"});

        if (vetor.size() != 1) {
            throw new Exception("Expressão mal formada: elementos não agrupados");
        }

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
        if (raiz == null) {
            throw new Exception("Nó nulo ao avaliar");
        }

        if (raiz.esq == null && raiz.dir == null) {
            try {
                return Double.parseDouble(raiz.valor);
            } catch (NumberFormatException e) {
                throw new Exception("Valor inválido no nó: '" + raiz.valor + "'");
            }
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
//metodo da isabelle
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