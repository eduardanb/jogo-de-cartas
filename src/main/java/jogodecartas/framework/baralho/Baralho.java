package jogodecartas.framework.baralho;

import jogodecartas.framework.carta.Carta;
import jogodecartas.framework.excecao.BaralhoVazioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Componente reutilizável que representa um baralho de cartas.
 *
 * <p>Não é um ponto de extensão: independentemente do jogo, um baralho
 * sempre se comporta da mesma forma (embaralhar, comprar carta, saber
 * quantas restam). O que varia entre jogos é <b>como</b> ele é montado, o
 * que fica a cargo de {@link FabricaBaralho}.</p>
 *
 * <p>A coleção interna de cartas é encapsulada: nunca é exposta por
 * referência direta, apenas por cópia imutável via {@link #getCartas()}.</p>
 *
 * @param <C> tipo concreto de carta manipulado por este baralho
 */
public final class Baralho<C extends Carta> {

    private final List<C> cartas;

    public Baralho(List<C> cartas) {
        this.cartas = new ArrayList<>(cartas);
    }

    /**
     * Embaralha aleatoriamente a ordem das cartas restantes.
     */
    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    /**
     * Remove e retorna a carta do topo do baralho.
     *
     * @return a carta comprada
     * @throws BaralhoVazioException se não houver mais cartas para comprar
     */
    public C comprar() throws BaralhoVazioException {
        if (estaVazio()) {
            throw new BaralhoVazioException("Não é possível comprar carta: o baralho está vazio.");
        }
        return cartas.remove(cartas.size() - 1);
    }

    public boolean estaVazio() {
        return cartas.isEmpty();
    }

    public int tamanho() {
        return cartas.size();
    }

    /**
     * @return visão somente-leitura das cartas restantes no baralho
     */
    public List<C> getCartas() {
        return Collections.unmodifiableList(cartas);
    }
}
