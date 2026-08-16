package jogodecartas.uno;

import jogodecartas.framework.excecao.BaralhoVazioException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.framework.regras.RegrasDoJogo;

public final class RegrasUno extends RegrasDoJogo<CartaUno> {

    // Quantidade de cartas recebidas no início da partida
    private static final int CARTAS_INICIAIS = 7;

    // Guarda a última carta jogada
    private CartaUno cartaDaMesa;

    @Override
    public void distribuirCartas(Partida<CartaUno> partida) {

        try {

            // Distribui 7 cartas para cada jogador
            for (int i = 0; i < CARTAS_INICIAIS; i++) {

                // percorre cada jogador, fazendo com que cada um recebe uma carta por vez
                for (Jogador<CartaUno> jogador : partida.getJogadores()) {
                    CartaUno carta = partida.getBaralho().comprar();
                    jogador.getMao().adicionar(carta);
                }
            }

            // Coloca uma carta inicial na mesa
            cartaDaMesa = partida.getBaralho().comprar();

        } catch (BaralhoVazioException e) {
            throw new IllegalStateException("Não há cartas suficientes para iniciar a partida.", e);
        }
    }

    @Override
    public boolean jogadaValida(Partida<CartaUno> partida, Jogador<CartaUno> jogador, CartaUno carta) {

        if (carta == null) {
            return false;
        }

        // A carta precisa estar na mão do jogador
        if (!jogador.getMao().contem(carta)) {
            return false;
        }

        // Coringas podem ser jogados sobre qualquer carta
        if (carta.isCoringa()) {
            return true;
        }

        if (cartaDaMesa == null) {
            return true;
        }

        // Depois de um coringa, qualquer carta pode ser jogada
        if (cartaDaMesa.isCoringa()) {
            return true;
        }

        // Mesma cor
        if (carta.getCor() == cartaDaMesa.getCor()) {
            return true;
        }

        // Mesmo número
        if (carta.isNumerica() && cartaDaMesa.isNumerica()) {

            return carta.getNumero() == cartaDaMesa.getNumero();
        }

        // Mesmo tipo de carta especial
        return !carta.isNumerica() && !cartaDaMesa.isNumerica() && carta.getTipo() == cartaDaMesa.getTipo();
    }

    @Override
    protected void aplicarJogada(Partida<CartaUno> partida, Jogador<CartaUno> jogador, CartaUno carta) {

        // Retira a carta da mão
        jogador.getMao().remover(carta);

        // A carta passa a ser a nova carta da mesa
        cartaDaMesa = carta;

        // Efeito: Comprar 2
        if (carta.getTipo() == CartaUno.Tipo.COMPRAR_DOIS) {
            Jogador<CartaUno> proximo = buscarProximoJogador(partida, jogador);
            comprarCartas(partida, proximo, 2);
        }

        // Efeito: Comprar 4
        if (carta.getTipo() == CartaUno.Tipo.CORINGA_COMPRAR_QUATRO) {
            Jogador<CartaUno> proximo = buscarProximoJogador(partida, jogador);
            comprarCartas(partida, proximo, 4);
        }
    }

    @Override
    public boolean partidaEncerrada(Partida<CartaUno> partida) {

        // A partida termina quando alguém fica sem cartas
        for (Jogador<CartaUno> jogador : partida.getJogadores()) {
            if (jogador.getMao().estaVazia()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Jogador<CartaUno> apurarVencedor(Partida<CartaUno> partida) {

        // O vencedor é quem ficou sem cartas
        for (Jogador<CartaUno> jogador : partida.getJogadores()) {
            if (jogador.getMao().estaVazia()) {
                return jogador;
            }
        }

        return null;
    }

    // Encontra o próximo jogador da partida
    private Jogador<CartaUno> buscarProximoJogador(Partida<CartaUno> partida, Jogador<CartaUno> jogadorAtual) {

        int indice = partida.getJogadores().indexOf(jogadorAtual); // descobre a posição do jogador atual

        int proximoIndice = (indice + 1) % partida.getJogadores().size();

        return partida.getJogadores().get(proximoIndice);
    }

    // Faz um jogador comprar a quantidade informada
    private void comprarCartas(Partida<CartaUno> partida, Jogador<CartaUno> jogador, int quantidade) {

        // tenta comprar a quantidade recebida
        for (int i = 0; i < quantidade; i++) {

            try {
                CartaUno carta = partida.getBaralho().comprar(); // comprar uma carta
                jogador.getMao().adicionar(carta); // coloca na mão

            } catch (BaralhoVazioException e) { // se as catas acabar durante o processo, vai ser interrompido
                break;
            }
        }
    }

    public CartaUno getCartaDaMesa() {
        return cartaDaMesa;
    }
}