package jogodecartas.uno.decorators;

import jogodecartas.uno.CartaAcao;
import jogodecartas.uno.CartaCoringa;
import jogodecartas.uno.CartaUno;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartaUnoDecoratorTest {

    @Test
    void efeitoPularDeveDelegarCorDoComponenteEDefinirTipoPular() {
        CartaUno carta = new EfeitoPular(new CartaAcao(CartaUno.Cor.AZUL));

        assertEquals(CartaUno.Cor.AZUL, carta.getCor());
        assertEquals(CartaUno.Tipo.PULAR, carta.getTipo());
        assertEquals("Pular AZUL", carta.getDescricao());
        assertFalse(carta.isNumerica());
        assertFalse(carta.isCoringa());
    }

    @Test
    void efeitoComprarQuatroDeveDelegarDeCartaCoringaEMarcarComoCoringa() {
        CartaUno carta = new EfeitoComprarQuatro(new CartaCoringa());

        assertEquals(CartaUno.Cor.SEM_COR, carta.getCor());
        assertEquals(CartaUno.Tipo.CORINGA_COMPRAR_QUATRO, carta.getTipo());
        assertTrue(carta.isCoringa());
        assertEquals(-1, carta.getNumero());
    }

    @Test
    void cartaAcaoSemDecoradorDeveLancarExcecaoAoPedirTipoOuDescricao() {
        CartaAcao cartaCrua = new CartaAcao(CartaUno.Cor.VERDE);

        assertThrows(IllegalStateException.class, cartaCrua::getTipo);
        assertThrows(IllegalStateException.class, cartaCrua::getDescricao);
    }
}