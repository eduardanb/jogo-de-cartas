package jogodecartas.framework.excecao;

/**
 * Sinaliza que uma tentativa de comprar carta foi feita com o
 * {@link jogodecartas.framework.baralho.Baralho} vazio.
 *
 * <p>É uma exceção verificada (checked) porque, em vários jogos, essa
 * condição é recuperável (ex.: reaproveitar a pilha de descarte para formar
 * um novo baralho), então quem chama {@code Baralho#comprar()} deve decidir
 * explicitamente como reagir, em vez de a aplicação simplesmente encerrar.</p>
 */
public class BaralhoVazioException extends Exception {

    public BaralhoVazioException(String mensagem) {
        super(mensagem);
    }
}
