package jogodecartas.uno.decorators;

import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.CartaUno;
import jogodecartas.uno.RegrasUno;
import jogodecartas.uno.eventos.CompraForcadaEvento;
import jogodecartas.uno.eventos.JogadorPulouVezEvento;

/**
 * Decora uma {@link jogodecartas.uno.CartaAcao} dando a ela o efeito
 * "Comprar Dois": o próximo jogador compra duas cartas e perde a vez
 * (regra oficial do UNO — quem é forçado a comprar não joga nesse turno).
 */
public final class EfeitoComprarDois extends CartaUnoDecorator {

    public EfeitoComprarDois(CartaUno componente) {
        super(componente);
    }

    @Override
    public Tipo getTipo() {
        return Tipo.COMPRAR_DOIS;
    }

    @Override
    public String getDescricao() {
        return "Comprar Dois " + getCor();
    }

    @Override
    public void aplicarEfeito(Partida<CartaUno> partida, Jogador<CartaUno> jogadorDaJogada) {
        Jogador<CartaUno> proximo = EfeitosUnoUtil.proximoJogador(partida, jogadorDaJogada);
        EfeitosUnoUtil.comprarCartas(partida, proximo, 2);
        partida.getEventos().publicar(new CompraForcadaEvento(proximo, 2));

        if (partida.getRegras() instanceof RegrasUno regrasUno) {
            regrasUno.pularProximoJogador();
            partida.getEventos().publicar(new JogadorPulouVezEvento(proximo));
        }
    }
}