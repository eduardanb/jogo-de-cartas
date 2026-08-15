package jogodecartas.framework.regras;

import jogodecartas.framework.carta.Carta;
import jogodecartas.framework.excecao.JogadaInvalidaException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;

/**
 * Ponto de extensão (padrão <b>Template Method</b>) que define o esqueleto de
 * uma jogada e concentra tudo que varia de jogo para jogo: como distribuir
 * cartas, quando uma jogada é válida, o efeito dela na partida e a condição
 * de vitória.
 *
 * <p>{@link #executarJogada} é o método template: fixo para todos os jogos,
 * ele sempre valida a jogada antes de aplicá-la, garantindo que nenhuma
 * subclasse concreta possa "esquecer" a validação.</p>
 *
 * @param <C> tipo concreto de carta do jogo
 */
public abstract class RegrasDoJogo<C extends Carta> {

    /**
     * Valida e, se válida, aplica a jogada de {@code carta} por
     * {@code jogador} na {@code partida}. Método template: não deve ser
     * sobrescrito pelas regras concretas.
     *
     * @throws JogadaInvalidaException se a jogada não for válida segundo
     *                                 {@link #jogadaValida}
     */
    public final void executarJogada(Partida<C> partida, Jogador<C> jogador, C carta) throws JogadaInvalidaException {
        if (!jogadaValida(partida, jogador, carta)) {
            throw new JogadaInvalidaException(
                    jogador.getNome() + " tentou uma jogada inválida: " + carta.getDescricao());
        }
        aplicarJogada(partida, jogador, carta);
    }

    /**
     * Distribui as cartas iniciais entre os jogadores da partida, de acordo
     * com as regras do jogo concreto.
     */
    public abstract void distribuirCartas(Partida<C> partida);

    /**
     * Verifica se {@code carta} pode ser jogada por {@code jogador} no
     * estado atual da partida.
     */
    public abstract boolean jogadaValida(Partida<C> partida, Jogador<C> jogador, C carta);

    /**
     * Aplica os efeitos de uma jogada já validada (ex.: mover a carta da mão
     * para a mesa, comprar cartas extras, pular o próximo jogador).
     */
    protected abstract void aplicarJogada(Partida<C> partida, Jogador<C> jogador, C carta);

    /**
     * Verifica se a partida já atingiu sua condição de encerramento.
     */
    public abstract boolean partidaEncerrada(Partida<C> partida);

    /**
     * Determina o vencedor da partida encerrada.
     *
     * @return o jogador vencedor, ou {@code null} em caso de empate, se o
     *         jogo concreto permitir empate
     */
    public abstract Jogador<C> apurarVencedor(Partida<C> partida);

    /**
     * Calcula o índice, em {@link Partida#getJogadores()}, do próximo
     * jogador da vez. Chamado por {@link Partida} após cada jogada
     * bem-sucedida que não encerre a partida.
     *
     * <p>Implementação padrão: avança um jogador, sempre na mesma ordem da
     * lista — suficiente para jogos sem sentido de jogo variável nem cartas
     * que pulam a vez. Jogos que têm esses mecanismos (ex.: Uno, com cartas
     * Inverter e Pular) sobrescrevem este método.</p>
     *
     * @param partida     partida em andamento
     * @param indiceAtual índice do jogador que acabou de jogar
     * @return índice do próximo jogador da vez
     */
    public int proximoIndice(Partida<C> partida, int indiceAtual) {
        return (indiceAtual + 1) % partida.getJogadores().size();
    }
}
