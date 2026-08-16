package jogodecartas.uno.eventos;

import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.uno.CartaUno;

/**
 * Evento publicado (padrão Observer) quando um jogador é forçado a comprar
 * cartas por causa de uma carta Comprar Dois ou Coringa Comprar Quatro.
 */
public final class CompraForcadaEvento extends EventoDoJogo {

    private final Jogador<CartaUno> jogador;
    private final int quantidade;

    public CompraForcadaEvento(Jogador<CartaUno> jogador, int quantidade) {
        this.jogador = jogador;
        this.quantidade = quantidade;
    }

    public Jogador<CartaUno> getJogador() {
        return jogador;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
