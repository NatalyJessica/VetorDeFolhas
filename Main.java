import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.print("Digite a expressão: ");
        Scanner scanner = new Scanner(System.in);
        String expressao = scanner.nextLine();

        var vetor = ArvoreExpressaoAritimetica.fragmentarExpressaoAririmetica(expressao);
        var raiz = ArvoreExpressaoAritimetica.construirArvore(vetor);

        System.out.println("Árvore binária desenhada:");
        ArvoreExpressaoAritimetica.printarArvore(raiz);

        double resultado = ArvoreExpressaoAritimetica.avaliar(raiz);
        System.out.println("Resultado: " + resultado);
    }
}