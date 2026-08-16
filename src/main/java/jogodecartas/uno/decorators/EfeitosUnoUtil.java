package jogodecartas.uno.decorators;

import jogodecartas.framework.excecao.BaralhoVazioException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.CartaUno;

/**
 * Funções auxiliares compartilhadas pelos decoradores de efeito do UNO que
 * fazem alguém comprar cartas ({@code EfeitoComprarDois} e
 * {@code EfeitoComprarQuatro}).
 *
 * <p>Classe utilitária de pacote (não faz parte da API pública do
 * framework nem do jogo).</p>
 */
final class EfeitosUnoUtil {

    private EfeitosUnoUtil() {
    }

    static Jogador<CartaUno> proximoJogador(Partida<CartaUno> partida, Jogador<CartaUno> jogadorAtual) {
        int indice = partida.getJogadores().indexOf(jogadorAtual);
        int proximo = (indice + 1) % partida.getJogadores().size();
        return partida.getJogadores().get(proximo);
    }

    static void comprarCartas(Partida<CartaUno> partida, Jogador<CartaUno> jogador, int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            try {
                CartaUno carta = partida.getBaralho().comprar();
                jogador.getMao().adicionar(carta);
            } catch (BaralhoVazioException e) {
                break;
            }
        }
    }
}