package jogodecartas.uno;

import jogodecartas.framework.excecao.BaralhoVazioException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.framework.regras.RegrasDoJogo;
import jogodecartas.uno.eventos.CartaJogadaEvento;
import jogodecartas.uno.eventos.PartidaEncerradaEvento;

/**
 * Regras concretas do UNO (padrão Template Method: implementa os passos
 * abstratos definidos por {@link RegrasDoJogo}).
 *
 * <p>Também mantém o estado de sentido da partida ({@link #direcao}) e um
 * sinalizador de turno pulado ({@link #pularProximo}), usados pelas cartas
 * decoradas com {@code EfeitoInversao} e {@code EfeitoPular}
 * (padrão Decorator, pacote {@link jogodecartas.uno.decorators}) para
 * alterar a ordem dos turnos sem que {@code RegrasUno} precise saber qual
 * carta específica foi jogada.</p>
 *
 * <p>Publica {@link CartaJogadaEvento} e {@link PartidaEncerradaEvento} no
 * {@link jogodecartas.framework.evento.BarramentoDeEventos} da partida
 * (padrão Observer), sem saber quem — se alguém — está escutando.</p>
 */
public final class RegrasUno extends RegrasDoJogo<CartaUno> {

    // Quantidade de cartas recebidas no início da partida
    private static final int CARTAS_INICIAIS = 7;

    // Guarda a última carta jogada
    private CartaUno cartaDaMesa;

    // Sentido da partida: 1 = horário, -1 = anti-horário. Alterado por EfeitoInversao.
    private int direcao = 1;

    // Sinaliza que o próximo jogador deve ser pulado. Ligado por EfeitoPular.
    private boolean pularProximo = false;

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

        // Cada carta decide, sozinha, se tem algum efeito ao ser jogada (Decorator).
        carta.aplicarEfeito(partida, jogador);

        // Publica o evento para quem estiver observando a partida (Observer).
        partida.getEventos().publicar(new CartaJogadaEvento(jogador, carta));

        if (partidaEncerrada(partida)) {
            partida.getEventos().publicar(new PartidaEncerradaEvento(apurarVencedor(partida)));
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

    /**
     * Sobrescreve o cálculo padrão de "próximo jogador" ({@link RegrasDoJogo#proximoIndice})
     * para levar em conta o sentido da partida e um possível pulo de turno,
     * ambos sinalizados pelas cartas especiais via {@link #inverterSentido()}
     * e {@link #pularProximoJogador()}.
     */
    @Override
    public int proximoIndice(Partida<CartaUno> partida, int indiceAtual) {
        int quantidadeJogadores = partida.getJogadores().size();
        int passo = pularProximo ? 2 : 1;
        pularProximo = false;
        return Math.floorMod(indiceAtual + direcao * passo, quantidadeJogadores);
    }

    /** Chamado por {@code EfeitoInversao} quando uma carta de Inversão é jogada. */
    public void inverterSentido() {
        direcao = -direcao;
    }

    /** Chamado por {@code EfeitoPular} quando uma carta de Pular é jogada. */
    public void pularProximoJogador() {
        pularProximo = true;
    }

    public CartaUno getCartaDaMesa() {
        return cartaDaMesa;
    }
}