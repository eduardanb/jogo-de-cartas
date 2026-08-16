package jogodecartas.uno.eventos;

import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.uno.CartaUno;

/**
 * Evento publicado (padrão Observer) quando uma partida de UNO chega ao
 * fim, informando quem venceu ({@code null} em caso de empate ou partida
 * interrompida sem vencedor apurável).
 */
public final class PartidaEncerradaEvento extends EventoDoJogo {

    private final Jogador<CartaUno> vencedor;

    public PartidaEncerradaEvento(Jogador<CartaUno> vencedor) {
        this.vencedor = vencedor;
    }

    public Jogador<CartaUno> getVencedor() {
        return vencedor;
    }
}