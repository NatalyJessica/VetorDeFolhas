/*
ESSA É A ARVORE BINARIA DE BUSCA DEVE MUDAR OS METODOS PARA SER APENAS UMA ARVORE BINARIA 
 */

public class ArvoreBinariaDeBusca<X extends Comparable<X>> {
    private class No {
        private No esq;
        private X info;
        private No dir;

        public No(No e, X i, No d) {
            this.esq = e;
            this.info = i;
            this.dir = d;
        }

        public No(X i) {
            this.esq = null;
            this.info = i;
            this.dir = null;
        }

        public No getEsq() {
            return this.esq;
        }

        public X getInfo() {
            return this.info;
        }

        public No getDir() {
            return this.dir;
        }

        public void setEsq(No e) {
            this.esq = e;
        }

        public void setInfo(X i) {
            this.info = i;
        }

        public void setDir(No d) {
            this.dir = d;
        }
    } // fim da classe No

    private No raiz;

    public void guardeUmItem(X i) throws Exception {
        if (i == null)
            throw new Exception("Informacao ausente");

        if (this.raiz == null) {
            this.raiz = new No(i);
            return;
        }

        No atual = this.raiz;
        for (;;) // forever
        {
            int comparacao = i.compareTo(atual.getInfo());

            if (comparacao == 0)
                throw new Exception("Elemento repetido");

            if (comparacao < 0) {
                if (atual.getEsq() == null) {
                    atual.setEsq(new No(i));
                    return;
                } else
                    atual = atual.getEsq();
            } else // comparacao>0
            {
                if (atual.getDir() == null) {
                    atual.setDir(new No(i));
                    return;
                } else
                    atual = atual.getDir();
            }
        }
    }

    // metodo não recursivo
    public boolean temOItem(X i) throws Exception {
        if (i == null)
            throw new Exception("Informacao ausente");

        No atual = this.raiz;

        for (;;) {
            if (atual == null)
                return false;

            int comparacao = i.compareTo(atual.getInfo());

            if (comparacao == 0)
                return true;

            else if (comparacao < 0)
                atual = atual.getEsq();
            else
                atual = atual.getDir();
        }
    }

    // metodo recursivo
    public boolean temOItemRecursivo(X i) throws Exception {

        if (i == null)
            throw new Exception("Informacao ausente");

        return this.temOItemRecursao(this.raiz, i);
    }

    private boolean temOItemRecursao(No atual, X i) {

        if (atual == null)
            return false;

        int comparacao = i.compareTo(atual.getInfo());

        if (comparacao == 0)
            return true;

        else if (comparacao < 0)
            return temOItemRecursao(atual.getEsq(), i);

        else
            return temOItemRecursao(atual.getDir(), i);
    }

    private int getAltura(No r) {
        if (r == null)
            return 0;

        int alturaDaSubArvoreEsquerda = this.getAltura(r.getEsq()),
                alturaDaSubArvoreDireita = this.getAltura(r.getDir());

        // o ? no codigo significa "então" e o : "senão"
        return alturaDaSubArvoreEsquerda > alturaDaSubArvoreDireita
                ? alturaDaSubArvoreEsquerda + 1
                : alturaDaSubArvoreDireita + 1;
    }

    public int getAltura() {
        return this.getAltura(this.raiz);
    }

    public void removaUmItem(X i) throws Exception {
        if (i == null)
            throw new Exception("Informacao ausente");

        if (this.raiz == null)
            throw new Exception("Arvore vazia");

        No atual = this.raiz;
        No pai = null;
        boolean filhoEsquerdo = true;

        for (;;) {
            int comparacao = i.compareTo(atual.getInfo());
            if (comparacao == 0)
                break;

            pai = atual;
            if (comparacao < 0) {
                atual = atual.getEsq();
                filhoEsquerdo = true;
            } else {
                atual = atual.getDir();
                filhoEsquerdo = false;
            }

            if (atual == null)
                throw new Exception("Remocao de algo inexistente");
        }

        // se a info for encontrada numa folha, deslique a folha da árvore,
        // fazendo o ponteiro que aponta para ela dentro do seu nó pai,
        // tornar-se null
        if (atual.getEsq() == null && atual.getDir() == null) {
            if (atual == this.raiz)
                this.raiz = null;
            else if (filhoEsquerdo)
                pai.setEsq(null);
            else
                pai.setDir(null);
        }
        // se info for encontrada num nó N, que não é folha, sendo que N
        // só tem filho à esquerda, e sendo N filho esquerdo de um certo
        // pai P, faça o ponteiro esquerdo de P, passar a apontar para
        // esse filho que ha na esquerda de N
        else if (atual.getDir() == null && filhoEsquerdo) {
            if (atual == this.raiz)
                this.raiz = atual.getEsq();
            else
                pai.setEsq(atual.getEsq());
        }
        // se info for encontrada num nó N, que não é folha, sendo que N
        // só tem filho à esquerda, e sendo N filho direito de um certo
        // pai P, faça o ponteiro direito de P, passar a apontar para
        // esse filho que ha na esquerda de N
        else if (atual.getDir() == null && !filhoEsquerdo) {
            if (atual == this.raiz)
                this.raiz = atual.getEsq();
            else
                pai.setDir(atual.getEsq());
        }
        // se info for encontrada num nó N, que não é folha, sendo que N
        // só tem filho à direita, e sendo N filho esquerdo de um certo
        // pai P, faça o ponteiro esquerdo de P, passar a apontar para
        // esse filho que ha na direita de N
        else if (atual.getEsq() == null && filhoEsquerdo) {
            if (atual == this.raiz)
                this.raiz = atual.getDir();
            else
                pai.setEsq(atual.getDir());
        }
        // se info for encontrada num nó N, que não é folha, sendo que N
        // só tem filho à direita, e sendo N filho direita de um certo
        // pai P, faça o ponteiro direito de P, passar a apontar para
        // esse filho que ha na direita de N
        else if (atual.getEsq() == null && !filhoEsquerdo) {
            if (atual == this.raiz)
                this.raiz = atual.getDir();
            else
                pai.setDir(atual.getDir());
        }
        // se info for encontrada num nó N, que não é folha e tem 2 filhos,
        // encontre a informação info que existe à extrema esquerda da
        // subarvore direita de N ou à extrema direita da subarvore esquerda
        // de N; remova o nó que contém info e substitua dentro do nó N,
        // a informação que ali se encontra por info
        else {
            No sucessor = null;
            pai = atual;
            if (getQtdDeNodos(atual.getEsq()) > getQtdDeNodos(atual.getDir())) {
                sucessor = atual.getEsq();
                filhoEsquerdo = true;
                while (sucessor.getDir() != null) {
                    pai = sucessor;
                    sucessor = sucessor.getDir();
                    filhoEsquerdo = false;
                }
            } else {
                sucessor = atual.getDir();
                filhoEsquerdo = false;
                while (sucessor.getEsq() != null) {
                    pai = sucessor;
                    sucessor = sucessor.getEsq();
                    filhoEsquerdo = true;
                }
            }
            if (filhoEsquerdo) {
                pai.setEsq(sucessor.getDir());
            } else {
                pai.setDir(sucessor.getEsq());
            }

            atual.setInfo(sucessor.getInfo());
        }
    }

    public int getQtdDeNodos() {
        return getQtdDeNodos(this.raiz);
    }

    private int getQtdDeNodos(No r) {
        if (r == null)
            return 0;
        return 1 + getQtdDeNodos(r.getEsq()) + getQtdDeNodos(r.getDir());
    }

    // exercicios isEspelho() faça um metodo para retornar true ou false caso uma
    // arvore seja o espelho da outra
    /* 
    private boolean isEspelho(No arvoreA, No arvoreB){
        if(arvoreA == null && arvoreB == null) return true;
        if(arvoreA == null || arvoreB == null) return false;

        if(!arvoreA.getInfo().equals(arvoreB.getInfo())) return false;

        if(isEspelho(arvoreA.getEsq(), arvoreB.getDir())) return false;
        if(isEspelho(arvoreA.getDir(), arvoreB.getEsq())) return false;

        return true;
    }

    public boolean isEspelho(ArvoreBinariaDeBusca<X> outra) {
        return isEspelho(this.raiz, outra.raiz);
    }
    */

    // isBalanceada (arvore para verificar se a esquerda e direta da arvore tem a
    // mesma altura (0)
    // ou com diferença de (1) caso for return true e caso for a diferença amir que
    // 1 return false

    /*
     * O Vinicius propoe trocar todos os comandos abaixo por:
     * return isBalanceada(r.getEsq()) && isBalanceada(r.getDir());
     * e tudo bem!
     */
    private boolean isBalanceada(No r) {
        int qtdEsq = getQtdDeNodos(r.getEsq()),
                qtdDir = getQtdDeNodos(r.getDir());

        if (qtdEsq > qtdDir + 1 || qtdDir > qtdEsq + 1)
            return false;

        if (!isBalanceada(r.getEsq()))
            return false;
        if (!isBalanceada(r.getDir()))
            return false;

        return true;

    }

    public boolean isBalanceada() {
        return isBalanceada(this.raiz);
    }

    private void balanceieSe(No r) throws Exception {
        if (r == null)
            return;
        int qtdEsq = getQtdDeNodos(r.getEsq()),
                qtdDir = getQtdDeNodos(r.getDir());

        while (Math.abs(qtdEsq - qtdDir) > 1) {
            if (qtdEsq > qtdDir) {
                No pai = r;
                No atual = r.getEsq();
                while (atual.getDir() != null) {
                    pai = atual;
                    atual = atual.getDir();
                }
                X infoRaiz = r.getInfo();
                r.setInfo(atual.getInfo());
                pai.setDir(atual.getEsq());
                qtdEsq--;
                guardeUmItem(infoRaiz);
                qtdDir++;

            } else {
                No pai = r;
                No atual = r.getDir();
                while (atual.getEsq() != null) {
                    pai = atual;
                    atual = atual.getEsq();
                }
                X infoRaiz = r.getInfo();
                r.setInfo(atual.getInfo());
                pai.setEsq(atual.getEsq());
                qtdDir--;
                guardeUmItem(infoRaiz);
                qtdEsq++;
            }
        }

        balanceieSe(r.getEsq());
        balanceieSe(r.getDir());

    }

    // fazer metodos obrigatorios da arvore

} // fim da classe ArvoreBinariaDeBusca