package jogodecartas.uno;

import jogodecartas.framework.carta.Carta;
/**
 * Representa uma carta do jogo UNO.
 *
 * <p>O UNO não utiliza os naipes tradicionais do baralho francês.
 * Por isso, a carta define sua própria classificação por meio de
 * cores e tipos.</p>
 */
public class CartaUno extends Carta {

    /**
     * Cores utilizadas pelas cartas coloridas do UNO.
     */
    public enum Cor {
        VERMELHO,
        AMARELO,
        VERDE,
        AZUL,
        SEM_COR
    }

    /**
     * Tipos de carta existentes no UNO.
     */
    public enum Tipo {
        NUMERO,
        PULAR,
        INVERSAO,
        COMPRAR_DOIS,
        CORINGA,
        CORINGA_COMPRAR_QUATRO
    }

    private final Cor cor;
    private final Tipo tipo;
    private final int numero;

    /**
     * Construtor para cartas numéricas.
     *
     * @param cor cor da carta
     * @param numero valor numérico da carta, de 0 a 9
     */
    public CartaUno(Cor cor, int numero) {
        if (cor == null || cor == Cor.SEM_COR) {
            throw new IllegalArgumentException(
                    "Carta numérica deve possuir uma cor válida."
            );
        }

        if (numero < 0 || numero > 9) {
            throw new IllegalArgumentException(
                    "O número de uma carta UNO deve estar entre 0 e 9."
            );
        }

        this.cor = cor;
        this.tipo = Tipo.NUMERO;
        this.numero = numero;
    }

    /**
     * Construtor para cartas especiais.
     *
     * @param cor cor da carta; cartas coringa devem utilizar SEM_COR
     * @param tipo tipo especial da carta
     */
    public CartaUno(Cor cor, Tipo tipo) {
        if (cor == null || tipo == null) {
            throw new IllegalArgumentException(
                    "Cor e tipo não podem ser nulos."
            );
        }

        if (tipo == Tipo.NUMERO) {
            throw new IllegalArgumentException(
                    "Cartas numéricas devem utilizar o construtor específico."
            );
        }

        if ((tipo == Tipo.CORINGA || tipo == Tipo.CORINGA_COMPRAR_QUATRO)
                && cor != Cor.SEM_COR) {
            throw new IllegalArgumentException(
                    "Cartas coringa devem possuir SEM_COR."
            );
        }

        if (tipo != Tipo.CORINGA && tipo != Tipo.CORINGA_COMPRAR_QUATRO
                && cor == Cor.SEM_COR) {
            throw new IllegalArgumentException(
                    "Cartas especiais coloridas devem possuir uma cor."
            );
        }

        this.cor = cor;
        this.tipo = tipo;
        this.numero = -1;
    }

    public Cor getCor() {
        return cor;
    }

    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Retorna o valor numérico da carta.
     *
     * @return valor entre 0 e 9 para cartas numéricas; -1 para cartas especiais
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Verifica se esta carta é numérica.
     */
    public boolean isNumerica() {
        return tipo == Tipo.NUMERO;
    }

    /**
     * Verifica se esta carta é uma carta coringa.
     */
    public boolean isCoringa() {
        return tipo == Tipo.CORINGA
                || tipo == Tipo.CORINGA_COMPRAR_QUATRO;
    }

    /**
     * Retorna a descrição textual da carta.
     */
    @Override
    public String getDescricao() {
        if (tipo == Tipo.NUMERO) {
            return "Carta " + numero + " " + cor;
        }

        return switch (tipo) {
            case PULAR -> "Pular " + cor;
            case INVERSAO -> "Inversão " + cor;
            case COMPRAR_DOIS -> "Comprar Dois " + cor;
            case CORINGA -> "Coringa";
            case CORINGA_COMPRAR_QUATRO -> "Coringa Comprar Quatro";
            case NUMERO -> "Carta " + numero + " " + cor;
        };
    }
}