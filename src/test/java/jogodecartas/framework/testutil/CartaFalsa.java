package jogodecartas.framework.testutil;

import jogodecartas.framework.carta.Carta;

/** Carta mínima usada apenas nos testes do framework, sem depender de nenhum jogo concreto. */
public class CartaFalsa extends Carta {

    private final String descricao;

    public CartaFalsa(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}