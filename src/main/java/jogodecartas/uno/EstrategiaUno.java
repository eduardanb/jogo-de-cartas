package jogodecartas.uno;

import jogodecartas.framework.estrategia.EstrategiaDeJogo;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;

/**
 * Especialização de {@link EstrategiaDeJogo} (padrão Strategy) para o UNO.
 *
 * <p>Cartas coringa não têm cor própria (ver {@link CartaUno.Cor#SEM_COR}):
 * quem joga uma escolhe a cor que passa a valer. Essa decisão não cabe em
 * {@link #escolherCarta}, que só devolve a carta — por isso esta interface
 * acrescenta {@link #escolherCor}, chamado por {@code RegrasUno} sempre que
 * uma carta coringa é jogada. Toda estratégia usada em uma partida de UNO
 * deve implementar esta interface, e não apenas {@link EstrategiaDeJogo}.</p>
 */
public interface EstrategiaUno extends EstrategiaDeJogo<CartaUno> {

    /**
     * Escolhe a cor a valer a partir de agora, chamado logo após
     * {@code jogador} jogar uma carta coringa.
     */
    CartaUno.Cor escolherCor(Jogador<CartaUno> jogador, Partida<CartaUno> partida);
}
