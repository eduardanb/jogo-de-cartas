package jogodecartas.uno;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.baralho.FabricaBaralho;
import jogodecartas.uno.decorators.EfeitoComprarDois;
import jogodecartas.uno.decorators.EfeitoComprarQuatro;
import jogodecartas.uno.decorators.EfeitoInversao;
import jogodecartas.uno.decorators.EfeitoPular;

import java.util.ArrayList;
import java.util.List;

/**
 * Fábrica responsável por montar o baralho padrão do UNO.
 *
 * <p>Implementa o ponto de extensão definido pelo framework, criando todas
 * as cartas específicas do UNO. As cartas de ação e as cartas coringa com
 * efeito de compra são montadas envolvendo um componente "base"
 * ({@link CartaAcao} / {@link CartaCoringa}) com o decorador de efeito
 * correspondente (padrão Decorator).</p>
 */
public class FabricaBaralhoUno implements FabricaBaralho<CartaUno> {

    @Override
    public Baralho<CartaUno> criarBaralho() {
        List<CartaUno> cartas = new ArrayList<>();

        CartaUno.Cor[] cores = {
                CartaUno.Cor.VERMELHO,
                CartaUno.Cor.AMARELO,
                CartaUno.Cor.VERDE,
                CartaUno.Cor.AZUL
        };

        for (CartaUno.Cor cor : cores) {

            // Carta 0: uma por cor
            cartas.add(new CartaNumerica(cor, 0));

            // Cartas 1 a 9: duas de cada por cor
            for (int numero = 1; numero <= 9; numero++) {
                cartas.add(new CartaNumerica(cor, numero));
                cartas.add(new CartaNumerica(cor, numero));
            }

            // Duas cartas Pular por cor
            cartas.add(new EfeitoPular(new CartaAcao(cor)));
            cartas.add(new EfeitoPular(new CartaAcao(cor)));

            // Duas cartas Inversão por cor
            cartas.add(new EfeitoInversao(new CartaAcao(cor)));
            cartas.add(new EfeitoInversao(new CartaAcao(cor)));

            // Duas cartas Comprar Dois por cor
            cartas.add(new EfeitoComprarDois(new CartaAcao(cor)));
            cartas.add(new EfeitoComprarDois(new CartaAcao(cor)));
        }

        // Quatro cartas Coringa (sem efeito de compra)
        for (int i = 0; i < 4; i++) {
            cartas.add(new CartaCoringa());
        }

        // Quatro cartas Coringa Comprar Quatro
        for (int i = 0; i < 4; i++) {
            cartas.add(new EfeitoComprarQuatro(new CartaCoringa()));
        }

        return new Baralho<>(cartas);
    }
}