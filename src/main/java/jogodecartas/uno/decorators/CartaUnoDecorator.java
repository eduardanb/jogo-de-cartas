package jogodecartas.uno.decorators;

import jogodecartas.uno.CartaUno;

/**
 * Decorador abstrato (padrão <b>Decorator</b>) para {@link CartaUno}.
 *
 * <p>Por padrão delega todo o comportamento para a carta envolvida
 * ({@link #componente}). As subclasses concretas
 * ({@code EfeitoPular}, {@code EfeitoInversao}, {@code EfeitoComprarDois},
 * {@code EfeitoComprarQuatro}) sobrescrevem apenas o que muda —
 * {@code getTipo()}, {@code getDescricao()} e {@code aplicarEfeito} —
 * permitindo adicionar um efeito especial a uma carta em tempo de execução,
 * sem precisar de uma subclasse de {@link CartaUno} para cada combinação de
 * cor x efeito.</p>
 */
public abstract class CartaUnoDecorator extends CartaUno {

    protected final CartaUno componente;

    protected CartaUnoDecorator(CartaUno componente) {
        this.componente = componente;
    }

    @Override
    public Cor getCor() {
        return componente.getCor();
    }

    @Override
    public Tipo getTipo() {
        return componente.getTipo();
    }

    @Override
    public int getNumero() {
        return componente.getNumero();
    }

    @Override
    public String getDescricao() {
        return componente.getDescricao();
    }
}