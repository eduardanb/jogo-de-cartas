package jogodecartas.framework.jogador;

import jogodecartas.framework.carta.Carta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Componente reutilizável que representa o conjunto de cartas na mão de um
 * jogador.
 *
 * <p>A coleção interna é encapsulada: nunca é exposta por referência direta,
 * apenas por cópia imutável via {@link #getCartas()}, evitando que código
 * externo adicione ou remova cartas sem passar pelos métodos desta
 * classe.</p>
 *
 * @param <C> tipo concreto de carta manipulado por esta mão
 */
public final class Mao<C extends Carta> {

    private final List<C> cartas = new ArrayList<>();

    public void adicionar(C carta) {
        cartas.add(carta);
    }

    public boolean remover(C carta) {
        return cartas.remove(carta);
    }

    public boolean contem(C carta) {
        return cartas.contains(carta);
    }

    public int tamanho() {
        return cartas.size();
    }

    public boolean estaVazia() {
        return cartas.isEmpty();
    }

    /**
     * @return visão somente-leitura das cartas na mão
     */
    public List<C> getCartas() {
        return Collections.unmodifiableList(cartas);
    }
}
