package jogodecartas.uno;

/**
 * Carta numérica do UNO (0–9), colorida.
 *
 * <p><b>Component</b> concreto do padrão Decorator: representa a carta mais
 * simples do jogo, sem nenhum efeito especial associado.</p>
 */
public final class CartaNumerica extends CartaUno {

    private final Cor cor;
    private final int numero;

    public CartaNumerica(Cor cor, int numero) {
        if (cor == null || cor == Cor.SEM_COR) {
            throw new IllegalArgumentException(
                    "Carta numérica deve possuir uma cor válida.");
        }
        if (numero < 0 || numero > 9) {
            throw new IllegalArgumentException(
                    "O número de uma carta UNO deve estar entre 0 e 9.");
        }
        this.cor = cor;
        this.numero = numero;
    }

    @Override
    public Cor getCor() {
        return cor;
    }

    @Override
    public Tipo getTipo() {
        return Tipo.NUMERO;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    @Override
    public String getDescricao() {
        return "Carta " + numero + " " + cor;
    }
}