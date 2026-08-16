package jogodecartas.uno.eventos;

import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.uno.CartaUno;

/**
 * Evento publicado (padrão Observer) quando um jogador perde a vez, seja por
 * uma carta Pular, por Inversão com exatamente dois jogadores (que se
 * comporta como Pular), ou por ter sido forçado a comprar cartas
 * (Comprar Dois / Coringa Comprar Quatro).
 */
public final class JogadorPulouVezEvento extends EventoDoJogo {

    private final Jogador<CartaUno> jogador;

    public JogadorPulouVezEvento(Jogador<CartaUno> jogador) {
        this.jogador = jogador;
    }

    public Jogador<CartaUno> getJogador() {
        return jogador;
    }
}
