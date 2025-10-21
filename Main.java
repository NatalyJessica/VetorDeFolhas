import java.util.Scanner;

public class Main {
    /*
     Exemplos de expressões:
     1 = 3*(4+5^2)-8/2
     2 = 4*(3+5^2)
     3 = (100 / (5^2)) + (8 * 3)
     4 = (3+4)*(2^3)-(6/(3+1))
     5 = 6 * (2 + 3 ^ 2) - (12 / (4 + 2))
    */

    public static void main(String[] args) {
        System.out.print("Digite a expressão: ");
        Scanner scanner = new Scanner(System.in);
        String expressao = scanner.nextLine();

        try {
            var vetor = ArvoreExpressaoAritimetica.fragmentarExpressaoAririmetica(expressao);

            System.out.print("Vetor de nós: ");
            for (int i = 0; i < vetor.size(); i++) {
                System.out.print(vetor.get(i).valor);
                if (i != vetor.size() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println(); 

            var raiz = ArvoreExpressaoAritimetica.construirArvore(vetor);

            System.out.println("Árvore binária desenhada:");
            ArvoreExpressaoAritimetica.printarArvore(raiz);

            double resultado = ArvoreExpressaoAritimetica.avaliar(raiz);
            System.out.println("Resultado: " + resultado);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
