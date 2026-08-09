package jogodecartas.framework.baralho;

import jogodecartas.framework.carta.Carta;

/**
 * Ponto de extensão (padrão <b>Factory Method</b>) responsável por montar o
 * conjunto de cartas de um jogo específico.
 *
 * <p>Cada jogo concreto (ex.: Uno, Blackjack) implementa esta interface para
 * decidir quais cartas existem e em que quantidade, sem que o restante do
 * framework ({@link jogodecartas.framework.partida.Partida},
 * {@link jogodecartas.framework.regras.RegrasDoJogo}) precise conhecer esses
 * detalhes.</p>
 *
 * @param <C> tipo concreto de carta produzido por esta fábrica
 */
public interface FabricaBaralho<C extends Carta> {

    /**
     * Cria um novo {@link Baralho} completo e pronto para uso (ainda não
     * embaralhado).
     *
     * @return baralho recém-montado
     */
    Baralho<C> criarBaralho();
}
