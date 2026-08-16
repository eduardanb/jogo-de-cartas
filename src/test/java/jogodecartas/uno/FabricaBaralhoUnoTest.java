package jogodecartas.uno;

import jogodecartas.framework.baralho.Baralho;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FabricaBaralhoUnoTest {

    @Test
    void baralhoPadraoDeveTerCentoEOitoCartas() {
        Baralho<CartaUno> baralho = new FabricaBaralhoUno().criarBaralho();

        assertEquals(108, baralho.tamanho());
    }

    @Test
    void baralhoDeveTerQuatroCoringasComunsEQuatroComprarQuatro() {
        Baralho<CartaUno> baralho = new FabricaBaralhoUno().criarBaralho();

        long coringasComuns = baralho.getCartas().stream()
                .filter(c -> c.getTipo() == CartaUno.Tipo.CORINGA)
                .count();
        long coringasComprarQuatro = baralho.getCartas().stream()
                .filter(c -> c.getTipo() == CartaUno.Tipo.CORINGA_COMPRAR_QUATRO)
                .count();

        assertEquals(4, coringasComuns);
        assertEquals(4, coringasComprarQuatro);
    }

    @Test
    void cadaCorDeveTerDuasCartasPularDuasInversaoEDuasComprarDois() {
        Baralho<CartaUno> baralho = new FabricaBaralhoUno().criarBaralho();

        for (CartaUno.Cor cor : new CartaUno.Cor[]{CartaUno.Cor.VERMELHO, CartaUno.Cor.AMARELO, CartaUno.Cor.VERDE, CartaUno.Cor.AZUL}) {
            assertEquals(2, contarPorCorETipo(baralho, cor, CartaUno.Tipo.PULAR));
            assertEquals(2, contarPorCorETipo(baralho, cor, CartaUno.Tipo.INVERSAO));
            assertEquals(2, contarPorCorETipo(baralho, cor, CartaUno.Tipo.COMPRAR_DOIS));
        }
    }

    private long contarPorCorETipo(Baralho<CartaUno> baralho, CartaUno.Cor cor, CartaUno.Tipo tipo) {
        return baralho.getCartas().stream()
                .filter(c -> c.getCor() == cor && c.getTipo() == tipo)
                .count();
    }
}