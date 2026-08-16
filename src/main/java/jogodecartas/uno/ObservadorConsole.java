package jogodecartas.uno;

import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.partida.ObservadorDaPartida;
import jogodecartas.uno.eventos.CartaJogadaEvento;
import jogodecartas.uno.eventos.CompraForcadaEvento;
import jogodecartas.uno.eventos.JogadorPulouVezEvento;
import jogodecartas.uno.eventos.PartidaEncerradaEvento;
import jogodecartas.uno.eventos.SentidoInvertidoEvento;

/**
 * Observador (padrão Observer) que narra no console os eventos publicados
 * durante uma partida de UNO, usando {@link Narrador} para carimbar o
 * horário e pausar entre mensagens -- sem isso, uma partida com vários bots
 * despeja dezenas de linhas instantaneamente e quem acompanha se perde de
 * qual jogada causou o quê.
 *
 * <p>É só uma das reações possíveis a esses eventos — outra implementação de
 * {@link ObservadorDaPartida} poderia, por exemplo, gravar um log em
 * arquivo ou atualizar um placar, sem que {@code Partida} precise saber
 * disso.</p>
 */
public final class ObservadorConsole implements ObservadorDaPartida {

    @Override
    public void aoReceberEvento(EventoDoJogo evento) {
        if (evento instanceof CartaJogadaEvento cartaJogada) {

            Narrador.separador();
            Narrador.anunciar(cartaJogada.getJogador().getNome() + " jogou: "
                    + cartaJogada.getCarta().getDescricao()
                    + " (restam " + cartaJogada.getJogador().getMao().tamanho() + " cartas)");

            if (cartaJogada.getJogador().getMao().tamanho() == 1) {
                Narrador.anunciarEfeito(cartaJogada.getJogador().getNome() + " gritou UNO!");
            }

        } else if (evento instanceof JogadorPulouVezEvento pulou) {
            Narrador.anunciarEfeito(pulou.getJogador().getNome() + " perde a vez.");

        } else if (evento instanceof SentidoInvertidoEvento) {
            Narrador.anunciarEfeito("O sentido do jogo foi invertido.");

        } else if (evento instanceof CompraForcadaEvento compra) {
            Narrador.anunciarEfeito(compra.getJogador().getNome() + " compra " + compra.getQuantidade() + " cartas.");

        } else if (evento instanceof PartidaEncerradaEvento partidaEncerrada) {

            Narrador.separador();
            if (partidaEncerrada.getVencedor() != null) {
                Narrador.anunciar("Vencedor: " + partidaEncerrada.getVencedor().getNome());
            } else {
                Narrador.anunciar("Partida encerrada sem vencedor.");
            }
        }
    }
}
