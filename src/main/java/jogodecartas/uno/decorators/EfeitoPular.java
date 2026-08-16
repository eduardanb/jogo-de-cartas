package jogodecartas.uno.decorators;

import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.CartaUno;
import jogodecartas.uno.RegrasUno;

/**
 * Decora uma {@link jogodecartas.uno.CartaAcao} dando a ela o efeito
 * "Pular": o próximo jogador perde a vez.
 */
public final class EfeitoPular extends CartaUnoDecorator {

    public EfeitoPular(CartaUno componente) {
        super(componente);
    }

    @Override
    public Tipo getTipo() {
        return Tipo.PULAR;
    }

    @Override
    public String getDescricao() {
        return "Pular " + getCor();
    }

    @Override
    public void aplicarEfeito(Partida<CartaUno> partida, Jogador<CartaUno> jogadorDaJogada) {
        if (partida.getRegras() instanceof RegrasUno regrasUno) {
            regrasUno.pularProximoJogador();
        }
    }
}