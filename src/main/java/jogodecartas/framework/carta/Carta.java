package jogodecartas.framework.carta;

/**
 * Representa uma carta genérica dentro do framework.
 *
 * <p>Esta é a abstração raiz de todo jogo de cartas: por si só ela não define
 * naipe, valor ou cor, pois esses atributos variam de jogo para jogo (ex.:
 * Uno usa cores e tipos especiais, Truco e Blackjack usam naipe e valor
 * numérico, Super Trunfo usa atributos livres).</p>
 *
 * <p><b>Ponto de extensão:</b> cada jogo concreto deve criar sua própria
 * subclasse de {@code Carta}, definindo os atributos e a semântica de
 * comparação/valor específicos daquele jogo.</p>
 */
public abstract class Carta {

    /**
     * Retorna a descrição textual da carta, usada para exibição em console.
     *
     * @return descrição legível da carta
     */
    public abstract String getDescricao();

    @Override
    public String toString() {
        return getDescricao();
    }
}
