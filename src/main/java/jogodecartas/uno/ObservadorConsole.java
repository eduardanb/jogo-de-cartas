package jogodecartas.uno;

import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.partida.ObservadorDaPartida;
import jogodecartas.uno.eventos.CartaJogadaEvento;
import jogodecartas.uno.eventos.PartidaEncerradaEvento;

/**
 * Observador (padrão Observer) que imprime no console os eventos publicados
 * durante uma partida de UNO.
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
            System.out.println();
            System.out.println(cartaJogada.getJogador().getNome() + " jogou: "
                    + cartaJogada.getCarta().getDescricao());
            System.out.println("Cartas restantes: " + cartaJogada.getJogador().getMao().tamanho());

        } else if (evento instanceof PartidaEncerradaEvento partidaEncerrada) {
            System.out.println();
            System.out.println("================================");
            if (partidaEncerrada.getVencedor() != null) {
                System.out.println("Vencedor: " + partidaEncerrada.getVencedor().getNome());
            } else {
                System.out.println("Partida encerrada sem vencedor.");
            }
            System.out.println("================================");
        }
    }
}