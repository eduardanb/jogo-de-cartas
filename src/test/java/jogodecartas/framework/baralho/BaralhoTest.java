package jogodecartas.framework.baralho;

import jogodecartas.framework.excecao.BaralhoVazioException;
import jogodecartas.framework.testutil.CartaFalsa;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaralhoTest {

    @Test
    void deveComecarComOTamanhoDaListaRecebida() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of(new CartaFalsa("A"), new CartaFalsa("B"), new CartaFalsa("C")));

        assertEquals(3, baralho.tamanho());
        assertFalse(baralho.estaVazio());
    }

    @Test
    void comprarDeveRemoverUmaCartaEDiminuirOTamanho() throws BaralhoVazioException {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of(new CartaFalsa("A"), new CartaFalsa("B")));

        CartaFalsa comprada = baralho.comprar();

        assertNotNull(comprada);
        assertEquals(1, baralho.tamanho());
        assertFalse(baralho.getCartas().contains(comprada));
    }

    @Test
    void comprarDeBaralhoVazioDeveLancarExcecao() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());

        assertTrue(baralho.estaVazio());
        assertThrows(BaralhoVazioException.class, baralho::comprar);
    }

    @Test
    void getCartasDeveSerSomenteLeitura() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of(new CartaFalsa("A")));

        List<CartaFalsa> cartas = baralho.getCartas();

        assertThrows(UnsupportedOperationException.class, () -> cartas.add(new CartaFalsa("B")));
    }

    @Test
    void construtorDeveCopiarAListaRecebidaEmVezDeReferenciarDiretamente() {
        List<CartaFalsa> listaOriginal = new ArrayList<>(List.of(new CartaFalsa("A")));
        Baralho<CartaFalsa> baralho = new Baralho<>(listaOriginal);

        listaOriginal.add(new CartaFalsa("B")); // alterar a lista original não deve afetar o baralho

        assertEquals(1, baralho.tamanho());
    }

    @Test
    void embaralharNaoDeveAlterarAQuantidadeDeCartas() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of(
                new CartaFalsa("A"), new CartaFalsa("B"), new CartaFalsa("C"), new CartaFalsa("D")));

        baralho.embaralhar();

        assertEquals(4, baralho.tamanho());
    }
}