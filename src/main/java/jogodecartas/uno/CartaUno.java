package jogodecartas.uno;

import jogodecartas.framework.carta.Carta;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;

/**
 * Carta do jogo UNO.
 *
 * <p>É o <b>Component</b> do padrão Decorator: define o contrato comum a
 * toda carta de UNO (cor, tipo, número, descrição) e um ponto de extensão de
 * comportamento, {@link #aplicarEfeito}, usado pelas cartas especiais para
 * reagir quando são jogadas (pular o próximo jogador, inverter o sentido,
 * fazer alguém comprar cartas...).</p>
 *
 * <p>As implementações concretas "base" ({@link CartaNumerica},
 * {@link CartaAcao}, {@link CartaCoringa}) não têm nenhum efeito especial
 * por padrão. Efeitos são adicionados em tempo de execução pelos decoradores
 * do pacote {@link jogodecartas.uno.decorators}, evitando criar uma
 * subclasse de carta para cada combinação de cor x efeito.</p>
 */
public abstract class CartaUno extends Carta {

    /**
     * Cores utilizadas pelas cartas coloridas do UNO.
     */
    public enum Cor {
        VERMELHO,
        AMARELO,
        VERDE,
        AZUL,
        SEM_COR
    }

    /**
     * Tipos de carta existentes no UNO.
     */
    public enum Tipo {
        NUMERO,
        PULAR,
        INVERSAO,
        COMPRAR_DOIS,
        CORINGA,
        CORINGA_COMPRAR_QUATRO
    }

    public abstract Cor getCor();

    public abstract Tipo getTipo();

    /**
     * @return valor numérico da carta, de 0 a 9; -1 para cartas não numéricas
     */
    public abstract int getNumero();

    public boolean isNumerica() {
        return getTipo() == Tipo.NUMERO;
    }

    public boolean isCoringa() {
        return getTipo() == Tipo.CORINGA || getTipo() == Tipo.CORINGA_COMPRAR_QUATRO;
    }

    /**
     * Efeito aplicado no momento em que esta carta é jogada (além de virar a
     * carta da mesa, que é responsabilidade de {@code RegrasUno}).
     *
     * <p>Implementação padrão: nenhum efeito. Apenas as cartas decoradas com
     * um efeito específico (ver {@link jogodecartas.uno.decorators})
     * sobrescrevem este método — é esse despacho polimórfico que substitui
     * os antigos condicionais {@code if (tipo == X)} em {@code RegrasUno}.</p>
     *
     * @param partida         partida em andamento
     * @param jogadorDaJogada jogador que acabou de jogar esta carta
     */
    public void aplicarEfeito(Partida<CartaUno> partida, Jogador<CartaUno> jogadorDaJogada) {
        // sem efeito por padrão
    }
}