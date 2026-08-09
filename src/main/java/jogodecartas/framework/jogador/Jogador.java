package jogodecartas.framework.jogador;

import jogodecartas.framework.carta.Carta;
import jogodecartas.framework.estrategia.EstrategiaDeJogo;

/**
 * Componente reutilizável que representa um jogador dentro de uma partida.
 *
 * <p>Não é um ponto de extensão por herança: a variação de comportamento
 * entre jogador humano e automatizado é obtida por composição, delegando a
 * decisão de jogada para uma {@link EstrategiaDeJogo} (padrão Strategy). Isso
 * evita uma hierarquia paralela de subclasses de {@code Jogador} para cada
 * combinação de jogo x tipo de jogador.</p>
 *
 * @param <C> tipo concreto de carta manipulado por este jogador
 */
public final class Jogador<C extends Carta> {

    private final String nome;
    private final Mao<C> mao;
    private final EstrategiaDeJogo<C> estrategia;

    public Jogador(String nome, EstrategiaDeJogo<C> estrategia) {
        this.nome = nome;
        this.estrategia = estrategia;
        this.mao = new Mao<>();
    }

    public String getNome() {
        return nome;
    }

    public Mao<C> getMao() {
        return mao;
    }

    public EstrategiaDeJogo<C> getEstrategia() {
        return estrategia;
    }
}
