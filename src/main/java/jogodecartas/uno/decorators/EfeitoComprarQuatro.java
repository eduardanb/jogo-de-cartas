package jogodecartas.uno.decorators;

import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.CartaUno;
import jogodecartas.uno.RegrasUno;
import jogodecartas.uno.eventos.CompraForcadaEvento;
import jogodecartas.uno.eventos.JogadorPulouVezEvento;

/**
 * Decora uma {@link jogodecartas.uno.CartaCoringa} dando a ela o efeito
 * "Comprar Quatro": além de trocar a cor vigente (efeito de coringa comum,
 * tratado em {@code RegrasUno}), o próximo jogador compra quatro cartas e
 * perde a vez (regra oficial do UNO — quem é forçado a comprar não joga
 * nesse turno).
 */
public final class EfeitoComprarQuatro extends CartaUnoDecorator {

    public EfeitoComprarQuatro(CartaUno componente) {
        super(componente);
    }

    @Override
    public Tipo getTipo() {
        return Tipo.CORINGA_COMPRAR_QUATRO;
    }

    @Override
    public String getDescricao() {
        return "Coringa Comprar Quatro";
    }

    @Override
    public void aplicarEfeito(Partida<CartaUno> partida, Jogador<CartaUno> jogadorDaJogada) {
        Jogador<CartaUno> proximo = EfeitosUnoUtil.proximoJogador(partida, jogadorDaJogada);
        EfeitosUnoUtil.comprarCartas(partida, proximo, 4);
        partida.getEventos().publicar(new CompraForcadaEvento(proximo, 4));

        if (partida.getRegras() instanceof RegrasUno regrasUno) {
            regrasUno.pularProximoJogador();
            partida.getEventos().publicar(new JogadorPulouVezEvento(proximo));
        }
    }
}