package jogodecartas.uno;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.baralho.FabricaBaralho;

import java.util.ArrayList;
import java.util.List;

/**
 * Fábrica responsável por montar o baralho padrão do UNO.
 *
 * <p>Implementa o ponto de extensão definido pelo framework,
 * criando todas as cartas específicas do UNO.</p>
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

        // Cartas numéricas e cartas de ação coloridas
        for (CartaUno.Cor cor : cores) {

            // Carta 0: uma por cor
            cartas.add(new CartaUno(cor, 0));

            // Cartas 1 a 9: duas de cada por cor
            for (int numero = 1; numero <= 9; numero++) {
                cartas.add(new CartaUno(cor, numero));
                cartas.add(new CartaUno(cor, numero));
            }

            // Duas cartas Pular por cor
            cartas.add(new CartaUno(cor, CartaUno.Tipo.PULAR));
            cartas.add(new CartaUno(cor, CartaUno.Tipo.PULAR));

            // Duas cartas Inversão por cor
            cartas.add(new CartaUno(cor, CartaUno.Tipo.INVERSAO));
            cartas.add(new CartaUno(cor, CartaUno.Tipo.INVERSAO));

            // Duas cartas Comprar Dois por cor
            cartas.add(new CartaUno(cor, CartaUno.Tipo.COMPRAR_DOIS));
            cartas.add(new CartaUno(cor, CartaUno.Tipo.COMPRAR_DOIS));
        }

        // Quatro cartas Coringa
        for (int i = 0; i < 4; i++) {
            cartas.add(
                    new CartaUno(
                            CartaUno.Cor.SEM_COR,
                            CartaUno.Tipo.CORINGA
                    )
            );
        }

        // Quatro cartas Coringa Comprar Quatro
        for (int i = 0; i < 4; i++) {
            cartas.add(
                    new CartaUno(
                            CartaUno.Cor.SEM_COR,
                            CartaUno.Tipo.CORINGA_COMPRAR_QUATRO
                    )
            );
        }

        return new Baralho<>(cartas);
    }
}