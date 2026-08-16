package jogodecartas.uno.decorators;

import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.CartaUno;

/**
 * Decora uma {@link jogodecartas.uno.CartaAcao} dando a ela o efeito
 * "Comprar Dois": o próximo jogador compra duas cartas.
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
    }
}