package jogodecartas.uno;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.estrategia.EstrategiaDeJogo;
import jogodecartas.framework.excecao.BaralhoVazioException;
import jogodecartas.framework.excecao.JogadaInvalidaException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Main {

    private static final String LINHA = "--------------------------------";

    private Main() {}

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Cria o baralho
        FabricaBaralhoUno fabrica = new FabricaBaralhoUno();
        Baralho<CartaUno> baralho = fabrica.criarBaralho();

        // Cria as regras
        RegrasUno regras = new RegrasUno();

        // Estratégia do jogador humano
        EstrategiaDeJogo<CartaUno> estrategiaHumana = (jogador, partida) -> escolherCartaHumano(jogador, partida, regras, scanner);
        // Estratégia do computador
        EstrategiaDeJogo<CartaUno> estrategiaComputador = (jogador, partida) -> escolherCartaComputador(jogador, partida, regras);

        Jogador<CartaUno> humano = new Jogador<>("Você", estrategiaHumana);
        Jogador<CartaUno> computador = new Jogador<>("Computador", estrategiaComputador);

        Partida<CartaUno> partida = new Partida<>(List.of(humano, computador), baralho, regras);

        partida.iniciar();

        mostrarInicio(regras);

        while (!partida.isEncerrada()) {

            // controlando de quem é a vez
            Jogador<CartaUno> jogador = partida.getJogadorDaVez();

            mostrarEstado(regras, humano, computador, jogador);

            // define a carta escolhida pelo humano e pelo computador
            CartaUno escolhida = jogador.getEstrategia().escolherCarta(jogador, partida);

            // Se não tiver uma jogada válida, compra cartas
            if (escolhida == null) {
                escolhida = comprarAteEncontrarJogada(partida, jogador, regras, jogador == humano);
            }

            // Pode acontecer se o baralho terminar
            if (escolhida == null) {
                System.out.println();
                System.out.println("O baralho acabou e não há jogada possível.");

                break;
            }

            try {
                partida.jogar(escolhida); // faz uma jogada
                mostrarJogada(jogador, escolhida, humano);

            } catch (JogadaInvalidaException e) {
                System.out.println("Jogada inválida: " + e.getMessage());
            }
        }

        // Quando o loop termina, mostra o resultado da partida.
        mostrarResultado(partida);

        scanner.close();
    }

    private static CartaUno escolherCartaHumano(Jogador<CartaUno> jogador, Partida<CartaUno> partida, RegrasUno regras, Scanner scanner) {

        List<CartaUno> cartasDaMao = jogador.getMao().getCartas();

        List<CartaUno> cartasValidas = new ArrayList<>();

        System.out.println();
        System.out.println("Sua mão:");

        // Mostra todas as cartas da mão
        for (CartaUno carta : cartasDaMao) {

            System.out.println("- " + carta.getDescricao());
            if (regras.jogadaValida(partida, jogador, carta)) {
                cartasValidas.add(carta);
            }
        }

        // Não existe nenhuma opção possível
        if (cartasValidas.isEmpty()) {

            System.out.println();
            System.out.println("Nenhuma carta da sua mão pode ser jogada.");

            return null; // o fluxo interpreta esse null como: precisa comprar cartas
        }

        System.out.println();
        System.out.println("Cartas que você pode jogar:");

        // Somente as cartas válidas recebem número
        for (int i = 0; i < cartasValidas.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + cartasValidas.get(i).getDescricao());
        }

        // pede uma opção ate receber uma válida
        while (true) {

            System.out.print("\nEscolha uma carta: ");

            String entrada = scanner.nextLine();

            try {

                int opcao = Integer.parseInt(entrada);

                if (opcao >= 1 && opcao <= cartasValidas.size()) {
                    return cartasValidas.get(opcao - 1);
                }

            } catch (NumberFormatException ignored) {}

            System.out.println("Opção inválida. Digite um dos números acima.");
        }
    }

    private static CartaUno escolherCartaComputador(Jogador<CartaUno> jogador, Partida<CartaUno> partida, RegrasUno regras) {

        for (CartaUno carta : jogador.getMao().getCartas()) {
            if (regras.jogadaValida(partida, jogador, carta)) {
                return carta;
            }
        }

        return null;
    }

    private static CartaUno comprarAteEncontrarJogada(Partida<CartaUno> partida, Jogador<CartaUno> jogador, RegrasUno regras, boolean jogadorHumano) {

        System.out.println();

        if (jogadorHumano) {
            System.out.println("Comprando até encontrar uma carta válida...");
        } else {
            System.out.println("Computador não possui jogada válida e vai comprar.");
        }

        // qunto tiver cartas no baralho
        while (!partida.getBaralho().estaVazio()) {

            CartaUno comprada = comprarCarta(partida, jogador);

            if (comprada == null) {
                return null;
            }

            if (jogadorHumano) {
                System.out.println("Você comprou: " + comprada.getDescricao());
            } else {
                System.out.println("Computador comprou uma carta.");
            }

            // Se a carta é jogável
            if (regras.jogadaValida(partida, jogador, comprada)) {
                if (jogadorHumano) {
                    System.out.println("Essa carta pode ser jogada.");
                }

                return comprada;
            }
        }

        return null;
    }

    private static CartaUno comprarCarta(Partida<CartaUno> partida, Jogador<CartaUno> jogador) {

        try {
            CartaUno carta = partida.getBaralho().comprar();
            jogador.getMao().adicionar(carta);
            return carta;

        } catch (BaralhoVazioException e) {
            return null;
        }
    }

    private static void mostrarInicio(RegrasUno regras) {

        System.out.println();
        System.out.println("================================");

        System.out.println("              UNO");

        System.out.println("================================");

        System.out.println("Carta inicial: " + regras.getCartaDaMesa().getDescricao());
    }

    private static void mostrarEstado(RegrasUno regras, Jogador<CartaUno> humano, Jogador<CartaUno> computador, Jogador<CartaUno> jogadorDaVez) {

        System.out.println();
        System.out.println(LINHA);

        System.out.println("Carta da mesa: " + regras.getCartaDaMesa().getDescricao());

        System.out.println("Você: " + humano.getMao().tamanho() + " cartas | Computador: " + computador.getMao().tamanho() + " cartas");

        System.out.println("Vez de: " + jogadorDaVez.getNome());
    }

    private static void mostrarJogada(Jogador<CartaUno> jogador, CartaUno carta, Jogador<CartaUno> humano) {

        System.out.println();

        if (jogador == humano) {
            System.out.println("Você jogou: " + carta.getDescricao());
        } else {
            System.out.println("Computador jogou: " + carta.getDescricao());
        }

        System.out.println("Cartas restantes: " + jogador.getMao().tamanho());
    }

    private static void mostrarResultado(Partida<CartaUno> partida) {

        System.out.println();
        System.out.println("================================");

        if (partida.isEncerrada()) {
            System.out.println("Vencedor: " + partida.getVencedor().getNome());
        } else {
            System.out.println("Partida encerrada sem vencedor.");
        }

        System.out.println("================================");
    }
}