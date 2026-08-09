package jogodecartas.framework.excecao;

/**
 * Sinaliza que um jogador tentou realizar uma jogada que viola as regras do
 * jogo em curso.
 *
 * <p>É uma exceção verificada (checked) porque representa uma condição
 * esperada durante uma partida normal (o jogador humano, ou uma estratégia
 * automatizada mal implementada, pode tentar uma jogada inválida) — quem
 * chama {@code RegrasDoJogo#executarJogada} deve tratar esse caso, por
 * exemplo pedindo uma nova jogada ao jogador em vez de encerrar a
 * aplicação.</p>
 */
public class JogadaInvalidaException extends Exception {

    public JogadaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
