package jogodecartas.uno.decorators;

import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.CartaUno;

/**
 * Decora uma {@link jogodecartas.uno.CartaCoringa} dando a ela o efeito
 * "Comprar Quatro": além de trocar a cor da mesa (efeito de coringa comum,
 * tratado em {@code RegrasUno}), o próximo jogador compra quatro cartas.
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
    }
}