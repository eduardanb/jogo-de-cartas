package jogodecartas.uno;

/**
 * Carta de ação colorida do UNO, ainda sem efeito definido.
 *
 * <p><b>Component</b> concreto do padrão Decorator — mas, ao contrário de
 * {@link CartaNumerica} e {@link CartaCoringa}, não deve circular sozinha no
 * jogo: ela existe apenas para ser envolvida por um dos decoradores em
 * {@link jogodecartas.uno.decorators} ({@code EfeitoPular},
 * {@code EfeitoInversao} ou {@code EfeitoComprarDois}), que definem seu
 * {@link Tipo} real, sua descrição e o efeito aplicado ao ser jogada.
 * {@link FabricaBaralhoUno} é o único ponto do código que deve instanciá-la
 * diretamente — sempre já envolvendo o resultado em um decorador.</p>
 */
public final class CartaAcao extends CartaUno {

    private final Cor cor;

    public CartaAcao(Cor cor) {
        if (cor == null || cor == Cor.SEM_COR) {
            throw new IllegalArgumentException(
                    "Carta de ação deve possuir uma cor válida.");
        }
        this.cor = cor;
    }

    @Override
    public Cor getCor() {
        return cor;
    }

    @Override
    public int getNumero() {
        return -1;
    }

    @Override
    public Tipo getTipo() {
        throw new IllegalStateException(
                "CartaAcao não tem tipo próprio: precisa estar decorada com um efeito "
                        + "(Pular, Inversão ou Comprar Dois) antes de entrar no jogo.");
    }

    @Override
    public String getDescricao() {
        throw new IllegalStateException(
                "CartaAcao não tem descrição própria: precisa estar decorada com um efeito.");
    }
}