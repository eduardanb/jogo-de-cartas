package jogodecartas.framework.testutil;

import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.framework.regras.RegrasDoJogo;

/**
 * Implementação mínima e configurável de {@link RegrasDoJogo}, usada nos
 * testes do framework (Partida, Template Method) para não depender de
 * nenhum jogo concreto como o UNO.
 */
public class RegrasFalsas extends RegrasDoJogo<CartaFalsa> {

    private boolean todaJogadaValida = true;
    private boolean encerrarNaProximaJogada = false;
    private Jogador<CartaFalsa> vencedorForcado;

    public int chamadasDistribuir = 0;
    public int chamadasAplicar = 0;

    public void tornarProximaJogadaInvalida() {
        this.todaJogadaValida = false;
    }

    public void encerrarAoJogar() {
        this.encerrarNaProximaJogada = true;
    }

    public void definirVencedor(Jogador<CartaFalsa> vencedor) {
        this.vencedorForcado = vencedor;
    }

    @Override
    public void distribuirCartas(Partida<CartaFalsa> partida) {
        chamadasDistribuir++;
    }

    @Override
    public boolean jogadaValida(Partida<CartaFalsa> partida, Jogador<CartaFalsa> jogador, CartaFalsa carta) {
        return todaJogadaValida;
    }

    @Override
    protected void aplicarJogada(Partida<CartaFalsa> partida, Jogador<CartaFalsa> jogador, CartaFalsa carta) {
        chamadasAplicar++;
    }

    @Override
    public boolean partidaEncerrada(Partida<CartaFalsa> partida) {
        return encerrarNaProximaJogada;
    }

    @Override
    public Jogador<CartaFalsa> apurarVencedor(Partida<CartaFalsa> partida) {
        return vencedorForcado;
    }
}