package jogodecartas.framework.estrategia;

import jogodecartas.framework.carta.Carta;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;

/**
 * Ponto de extensão (padrão <b>Strategy</b>) responsável por decidir qual
 * carta um jogador vai jogar em seu turno.
 *
 * <p>É esta interface — e não uma hierarquia de subclasses de
 * {@link Jogador} — que permite variar o comportamento entre jogador humano
 * (lê a jogada do console) e jogador automatizado (decide sozinho, com
 * alguma heurística), mantendo {@link Jogador} como uma classe concreta e
 * reutilizável.</p>
 *
 * @param <C> tipo concreto de carta do jogo
 */
public interface EstrategiaDeJogo<C extends Carta> {

    /**
     * Escolhe a carta que o jogador vai jogar, dado o estado atual da
     * partida.
     *
     * @param jogador jogador que está decidindo a jogada
     * @param partida partida em andamento, usada como contexto de decisão
     * @return carta escolhida pelo jogador (deve pertencer à mão dele)
     */
    C escolherCarta(Jogador<C> jogador, Partida<C> partida);
}
