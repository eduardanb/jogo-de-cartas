package jogodecartas.uno;

/**
 * Carta coringa "simples" do UNO: muda a cor da mesa, sem nenhum efeito de
 * compra associado.
 *
 * <p><b>Component</b> concreto do padrão Decorator. Pode circular como está
 * (Coringa comum) ou ser envolvida por
 * {@link jogodecartas.uno.decorators.EfeitoComprarQuatro} para virar a carta
 * "Coringa Comprar Quatro" — ilustrando bem o ganho do Decorator: a mesma
 * base serve para duas cartas diferentes, sem precisar de uma subclasse
 * dedicada para a versão com efeito de compra.</p>
 */
public final class CartaCoringa extends CartaUno {

    @Override
    public Cor getCor() {
        return Cor.SEM_COR;
    }

    @Override
    public Tipo getTipo() {
        return Tipo.CORINGA;
    }

    @Override
    public int getNumero() {
        return -1;
    }

    @Override
    public String getDescricao() {
        return "Coringa";
    }
}