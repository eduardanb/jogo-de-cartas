package jogodecartas.uno.decorators;

import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.CartaUno;
import jogodecartas.uno.RegrasUno;
import jogodecartas.uno.eventos.JogadorPulouVezEvento;
import jogodecartas.uno.eventos.SentidoInvertidoEvento;

/**
 * Decora uma {@link jogodecartas.uno.CartaAcao} dando a ela o efeito
 * "Inversão": inverte o sentido em que os jogadores jogam.
 *
 * <p>Regra clássica do UNO: em uma partida com exatamente dois jogadores,
 * inverter o sentido não tem efeito perceptível (o próximo da lista seria o
 * mesmo nos dois sentidos), então a carta se comporta como Pular. Com três
 * ou mais jogadores, o sentido é realmente invertido.</p>
 */
public final class EfeitoInversao extends CartaUnoDecorator {

    public EfeitoInversao(CartaUno componente) {
        super(componente);
    }

    @Override
    public Tipo getTipo() {
        return Tipo.INVERSAO;
    }

    @Override
    public String getDescricao() {
        return "Inversão " + getCor();
    }

    @Override
    public void aplicarEfeito(Partida<CartaUno> partida, Jogador<CartaUno> jogadorDaJogada) {
        if (partida.getRegras() instanceof RegrasUno regrasUno) {
            if (partida.getJogadores().size() == 2) {
                regrasUno.pularProximoJogador();

                Jogador<CartaUno> pulado = EfeitosUnoUtil.proximoJogador(partida, jogadorDaJogada);
                partida.getEventos().publicar(new JogadorPulouVezEvento(pulado));
            } else {
                regrasUno.inverterSentido();
                partida.getEventos().publicar(new SentidoInvertidoEvento());
            }
        }
    }
}