package jogodecartas.uno.eventos;

import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.uno.CartaUno;

/**
 * Evento publicado (padrão Observer) sempre que um jogador joga uma carta
 * válida durante uma partida de UNO.
 */
public final class CartaJogadaEvento extends EventoDoJogo {

    private final Jogador<CartaUno> jogador;
    private final CartaUno carta;

    public CartaJogadaEvento(Jogador<CartaUno> jogador, CartaUno carta) {
        this.jogador = jogador;
        this.carta = carta;
    }

    public Jogador<CartaUno> getJogador() {
        return jogador;
    }

    public CartaUno getCarta() {
        return carta;
    }
}