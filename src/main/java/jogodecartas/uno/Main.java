package jogodecartas.uno;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.excecao.BaralhoVazioException;
import jogodecartas.framework.excecao.JogadaInvalidaException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public final class Main {

    private static final String LINHA = "--------------------------------";
    private static final int MINIMO_JOGADORES = 2;
    private static final int MAXIMO_JOGADORES = 4;

    private Main() {}

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("================================");
        System.out.println("              UNO");
        System.out.println("================================");

        // Cria as regras (compartilhada por todas as estratégias, que
        // precisam dela pra saber se uma carta é jogável agora)
        RegrasUno regras = new RegrasUno();

        // Menu inicial: quantidade de jogadores, humano ou bot cada um, e
        // confirmação antes de embaralhar/distribuir as cartas.
        ConfiguracaoJogadores configuracao = lerJogadores(scanner, regras);
        List<Jogador<CartaUno>> jogadores = configuracao.jogadores();

        // Com só um humano na mesa, não tem "oponente" que possa espiar --
        // então dá pra mostrar a mão dos bots pra ele acompanhar o jogo.
        boolean revelarMaosDosBots = (jogadores.size() - configuracao.bots().size()) == 1;

        if (!confirmarInicio(scanner)) {
            System.out.println("\nPartida cancelada.");
            scanner.close();
            return;
        }

        // Cria o baralho
        FabricaBaralhoUno fabrica = new FabricaBaralhoUno();
        Baralho<CartaUno> baralho = fabrica.criarBaralho();

        Partida<CartaUno> partida = new Partida<>(jogadores, baralho, regras);

        // Registra quem vai "assistir" a partida (padrão Observer). Partida
        // não sabe, e não precisa saber, que existe um ObservadorConsole.
        partida.adicionarObservador(new ObservadorConsole());

        partida.iniciar();

        mostrarInicio(regras);

        while (!partida.isEncerrada()) {

            // controlando de quem é a vez
            Jogador<CartaUno> jogador = partida.getJogadorDaVez();

            mostrarEstado(regras, jogadores, configuracao.bots(), revelarMaosDosBots, jogador);

            // define a carta escolhida por quem estiver na vez (humano ou bot)
            CartaUno escolhida = jogador.getEstrategia().escolherCarta(jogador, partida);

            // Se não tiver uma jogada válida, compra cartas
            if (escolhida == null) {
                escolhida = comprarAteEncontrarJogada(partida, jogador, regras);
            }

            // Pode acontecer se o baralho terminar
            if (escolhida == null) {
                System.out.println();
                System.out.println("O baralho acabou e não há jogada possível.");

                break;
            }

            try {
                partida.jogar(escolhida); // faz uma jogada; ObservadorConsole cuida do print

            } catch (JogadaInvalidaException e) {
                System.out.println("Jogada inválida: " + e.getMessage());
            }
        }

        // Quando o loop termina sem a partida ter sido encerrada normalmente
        // (baralho acabou), avisa o jogador — o caso de vitória normal já foi
        // anunciado pelo ObservadorConsole via PartidaEncerradaEvento.
        mostrarResultado(partida);

        scanner.close();
    }

    /** Jogadores configurados no menu, e quais deles são bots (usado pra decidir se mostra a mão deles). */
    private record ConfiguracaoJogadores(List<Jogador<CartaUno>> jogadores, Set<Jogador<CartaUno>> bots) {}

    private static ConfiguracaoJogadores lerJogadores(Scanner scanner, RegrasUno regras) {

        int quantidade = lerQuantidadeJogadores(scanner);
        List<Jogador<CartaUno>> jogadores = new ArrayList<>();
        Set<Jogador<CartaUno>> bots = new HashSet<>();

        for (int i = 1; i <= quantidade; i++) {

            System.out.print("\nJogador " + i + " é humano? (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();

            if (resposta.startsWith("s")) {

                System.out.print("Nome do jogador " + i + ": ");
                String nome = scanner.nextLine().trim();

                jogadores.add(new Jogador<>(
                        nome.isEmpty() ? "Jogador " + i : nome,
                        criarEstrategiaHumana(regras, scanner)));

            } else {
                Jogador<CartaUno> bot = new Jogador<>("Bot " + i, criarEstrategiaComputador(regras));
                jogadores.add(bot);
                bots.add(bot);
            }
        }

        return new ConfiguracaoJogadores(jogadores, bots);
    }

    private static int lerQuantidadeJogadores(Scanner scanner) {

        while (true) {

            System.out.print("Quantos jogadores (" + MINIMO_JOGADORES + " a " + MAXIMO_JOGADORES + ")? ");
            String entrada = scanner.nextLine();

            try {

                int quantidade = Integer.parseInt(entrada);

                if (quantidade >= MINIMO_JOGADORES && quantidade <= MAXIMO_JOGADORES) {
                    return quantidade;
                }

            } catch (NumberFormatException ignored) {}

            System.out.println("Valor inválido. Digite um número entre "
                    + MINIMO_JOGADORES + " e " + MAXIMO_JOGADORES + ".");
        }
    }

    private static boolean confirmarInicio(Scanner scanner) {

        System.out.print("\nTudo pronto. Iniciar a partida? (s/n): ");
        String resposta = scanner.nextLine().trim().toLowerCase();

        return resposta.startsWith("s");
    }

    // Estratégia de um jogador humano. É uma classe anônima, e não uma
    // lambda, porque EstrategiaUno tem dois métodos abstratos (escolherCarta
    // e escolherCor) -- lambdas só servem pra interfaces funcionais, de um
    // método só.
    private static EstrategiaUno criarEstrategiaHumana(RegrasUno regras, Scanner scanner) {
        return new EstrategiaUno() {
            @Override
            public CartaUno escolherCarta(Jogador<CartaUno> jogador, Partida<CartaUno> partida) {
                return escolherCartaHumano(jogador, partida, regras, scanner);
            }

            @Override
            public CartaUno.Cor escolherCor(Jogador<CartaUno> jogador, Partida<CartaUno> partida) {
                return escolherCorHumano(jogador, scanner);
            }
        };
    }

    // Estratégia de um jogador automatizado (bot).
    private static EstrategiaUno criarEstrategiaComputador(RegrasUno regras) {
        return new EstrategiaUno() {
            @Override
            public CartaUno escolherCarta(Jogador<CartaUno> jogador, Partida<CartaUno> partida) {
                return escolherCartaComputador(jogador, partida, regras);
            }

            @Override
            public CartaUno.Cor escolherCor(Jogador<CartaUno> jogador, Partida<CartaUno> partida) {
                return escolherCorComputador(jogador);
            }
        };
    }

    private static CartaUno escolherCartaHumano(Jogador<CartaUno> jogador, Partida<CartaUno> partida, RegrasUno regras, Scanner scanner) {

        List<CartaUno> cartasDaMao = jogador.getMao().getCartas();

        List<CartaUno> cartasValidas = new ArrayList<>();

        System.out.println();
        System.out.println("Mão de " + jogador.getNome() + ":");

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
            System.out.println("Nenhuma carta da mão pode ser jogada.");

            return null; // o fluxo interpreta esse null como: precisa comprar cartas
        }

        System.out.println();
        System.out.println("Cartas que " + jogador.getNome() + " pode jogar:");

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

    private static CartaUno.Cor escolherCorHumano(Jogador<CartaUno> jogador, Scanner scanner) {

        CartaUno.Cor[] cores = { CartaUno.Cor.VERMELHO, CartaUno.Cor.AMARELO, CartaUno.Cor.VERDE, CartaUno.Cor.AZUL };

        System.out.println();
        System.out.println(jogador.getNome() + ", escolha a nova cor:");

        for (int i = 0; i < cores.length; i++) {
            System.out.println("[" + (i + 1) + "] " + cores[i]);
        }

        while (true) {

            System.out.print("\nEscolha uma cor: ");

            String entrada = scanner.nextLine();

            try {

                int opcao = Integer.parseInt(entrada);

                if (opcao >= 1 && opcao <= cores.length) {
                    return cores[opcao - 1];
                }

            } catch (NumberFormatException ignored) {}

            System.out.println("Opção inválida. Digite um dos números acima.");
        }
    }

    private static CartaUno.Cor escolherCorComputador(Jogador<CartaUno> jogador) {

        // Escolhe a cor mais frequente na própria mão, pra maximizar a
        // chance de conseguir jogar de novo no próximo turno.
        CartaUno.Cor[] cores = { CartaUno.Cor.VERMELHO, CartaUno.Cor.AMARELO, CartaUno.Cor.VERDE, CartaUno.Cor.AZUL };
        int[] contagem = new int[cores.length];

        for (CartaUno carta : jogador.getMao().getCartas()) {
            for (int i = 0; i < cores.length; i++) {
                if (carta.getCor() == cores[i]) {
                    contagem[i]++;
                }
            }
        }

        int indiceMaisFrequente = 0;
        for (int i = 1; i < cores.length; i++) {
            if (contagem[i] > contagem[indiceMaisFrequente]) {
                indiceMaisFrequente = i;
            }
        }

        return cores[indiceMaisFrequente];
    }

    private static CartaUno comprarAteEncontrarJogada(Partida<CartaUno> partida, Jogador<CartaUno> jogador, RegrasUno regras) {

        Narrador.anunciar(jogador.getNome() + " não possui jogada válida e vai comprar.");

        // qunto tiver cartas no baralho
        while (!partida.getBaralho().estaVazio()) {

            CartaUno comprada = comprarCarta(partida, jogador);

            if (comprada == null) {
                return null;
            }

            Narrador.anunciarEfeito(jogador.getNome() + " comprou: " + comprada.getDescricao());

            // Se a carta é jogável
            if (regras.jogadaValida(partida, jogador, comprada)) {
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

        System.out.println("         Partida iniciada");

        System.out.println("================================");

        System.out.println("Carta inicial: " + regras.getCartaDaMesa().getDescricao());
        System.out.println("Cor vigente: " + regras.getCorVigente());
    }

    private static void mostrarEstado(RegrasUno regras, List<Jogador<CartaUno>> jogadores, Set<Jogador<CartaUno>> bots,
                                       boolean revelarMaosDosBots, Jogador<CartaUno> jogadorDaVez) {

        System.out.println();
        System.out.println(LINHA);

        System.out.println("Carta da mesa: " + regras.getCartaDaMesa().getDescricao());
        System.out.println("Cor vigente: " + regras.getCorVigente());

        StringBuilder placar = new StringBuilder();
        for (int i = 0; i < jogadores.size(); i++) {

            if (i > 0) {
                placar.append(" | ");
            }

            Jogador<CartaUno> jogador = jogadores.get(i);
            placar.append(jogador.getNome()).append(": ").append(jogador.getMao().tamanho()).append(" cartas");
        }
        System.out.println(placar);

        if (revelarMaosDosBots) {
            for (Jogador<CartaUno> jogador : jogadores) {
                if (bots.contains(jogador)) {
                    System.out.println("Mão de " + jogador.getNome() + ": " + descricaoMao(jogador));
                }
            }
        }

        System.out.println("Vez de: " + jogadorDaVez.getNome());
    }

    private static String descricaoMao(Jogador<CartaUno> jogador) {

        List<CartaUno> cartas = jogador.getMao().getCartas();
        StringBuilder descricao = new StringBuilder();

        for (int i = 0; i < cartas.size(); i++) {

            if (i > 0) {
                descricao.append(", ");
            }

            descricao.append(cartas.get(i).getDescricao());
        }

        return descricao.toString();
    }

    private static void mostrarResultado(Partida<CartaUno> partida) {

        // Se a partida terminou normalmente, o ObservadorConsole já anunciou
        // o vencedor via PartidaEncerradaEvento. Isso só cobre o caso do
        // baralho ter acabado antes de alguém vencer.
        if (!partida.isEncerrada()) {
            System.out.println();
            System.out.println("================================");
            System.out.println("Partida encerrada sem vencedor.");
            System.out.println("================================");
        }
    }
}
