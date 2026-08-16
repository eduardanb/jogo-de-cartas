package jogodecartas.uno;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.excecao.JogadaInvalidaException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.uno.decorators.EfeitoComprarDois;
import jogodecartas.uno.decorators.EfeitoComprarQuatro;
import jogodecartas.uno.decorators.EfeitoInversao;
import jogodecartas.uno.decorators.EfeitoPular;
import jogodecartas.uno.eventos.CartaJogadaEvento;
import jogodecartas.uno.eventos.CompraForcadaEvento;
import jogodecartas.uno.eventos.JogadorPulouVezEvento;
import jogodecartas.uno.eventos.PartidaEncerradaEvento;
import jogodecartas.uno.eventos.SentidoInvertidoEvento;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegrasUnoTest {

    /** Partida de UNO já iniciada, com baralho homogêneo (só cartas vermelhas) para evitar aleatoriedade nos testes. */
    private Partida<CartaUno> novaPartidaIniciada(RegrasUno regras, Jogador<CartaUno> a, Jogador<CartaUno> b) {
        List<CartaUno> cartasIniciais = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            cartasIniciais.add(new CartaNumerica(CartaUno.Cor.VERMELHO, i % 10));
        }
        Baralho<CartaUno> baralho = new Baralho<>(cartasIniciais);
        Partida<CartaUno> partida = new Partida<>(List.of(a, b), baralho, regras);
        partida.iniciar();
        return partida;
    }

    private Jogador<CartaUno> novoJogador(String nome) {
        return new Jogador<>(nome, (jogador, partida) -> null);
    }

    /** Jogador cuja estratégia sempre escolhe {@code corEscolhida} ao jogar um coringa. */
    private Jogador<CartaUno> novoJogadorComCor(String nome, CartaUno.Cor corEscolhida) {
        EstrategiaUno estrategia = new EstrategiaUno() {
            @Override
            public CartaUno escolherCarta(Jogador<CartaUno> jogador, Partida<CartaUno> partida) {
                return null;
            }

            @Override
            public CartaUno.Cor escolherCor(Jogador<CartaUno> jogador, Partida<CartaUno> partida) {
                return corEscolhida;
            }
        };
        return new Jogador<>(nome, estrategia);
    }

    @Test
    void cartaDaMesmaCorDeveSerValidaMesmoComNumeroDiferente() {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia); // todas as cartas iniciais são vermelhas

        CartaUno carta = new CartaNumerica(CartaUno.Cor.VERMELHO, 7);
        ana.getMao().adicionar(carta);

        assertTrue(regras.jogadaValida(partida, ana, carta));
    }

    @Test
    void cartaDeCorENumeroDiferentesDaMesaDeveSerInvalida() {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        int numeroDiferente = (regras.getCartaDaMesa().getNumero() + 1) % 10;
        CartaUno cartaAzulDiferente = new CartaNumerica(CartaUno.Cor.AZUL, numeroDiferente);
        ana.getMao().adicionar(cartaAzulDiferente);

        assertFalse(regras.jogadaValida(partida, ana, cartaAzulDiferente));
    }

    @Test
    void cartaQueNaoEstaNaMaoDoJogadorDeveSerInvalida() {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        CartaUno cartaForaDaMao = new CartaNumerica(CartaUno.Cor.VERMELHO, 5);

        assertFalse(regras.jogadaValida(partida, ana, cartaForaDaMao));
    }

    @Test
    void coringaDeveSerSempreValido() {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        CartaUno coringa = new CartaCoringa();
        ana.getMao().adicionar(coringa);

        assertTrue(regras.jogadaValida(partida, ana, coringa));
    }

    @Test
    void jogarCoringaDeveTravarNaCorEscolhidaPelaEstrategiaDeQuemJogou() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogadorComCor("Ana", CartaUno.Cor.AZUL);
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia); // cartas iniciais vermelhas

        CartaUno coringa = new CartaCoringa();
        ana.getMao().adicionar(coringa);

        partida.jogar(coringa);

        assertSame(CartaUno.Cor.AZUL, regras.getCorVigente());

        CartaUno cartaAzul = new CartaNumerica(CartaUno.Cor.AZUL, 5);
        CartaUno cartaVerde = new CartaNumerica(CartaUno.Cor.VERDE, 5);
        bia.getMao().adicionar(cartaAzul);
        bia.getMao().adicionar(cartaVerde);

        // Só a cor escolhida (AZUL) deve valer -- não é mais "qualquer carta",
        // como acontecia antes do coringa passar a travar numa cor.
        assertTrue(regras.jogadaValida(partida, bia, cartaAzul));
        assertFalse(regras.jogadaValida(partida, bia, cartaVerde));
    }

    @Test
    void jogarCartaComprarDoisDeveFazerOProximoJogadorComprarDuasCartasEPerderAVez() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);
        int cartasDeBiaAntes = bia.getMao().tamanho();

        List<EventoDoJogo> recebidos = new ArrayList<>();
        partida.adicionarObservador(recebidos::add);

        CartaUno comprarDois = new EfeitoComprarDois(new CartaAcao(CartaUno.Cor.VERMELHO));
        ana.getMao().adicionar(comprarDois);

        partida.jogar(comprarDois);

        assertEquals(cartasDeBiaAntes + 2, bia.getMao().tamanho());
        // Bia foi forçada a comprar: com 2 jogadores, ela também perde a vez,
        // e o turno volta pra Ana.
        assertSame(ana, partida.getJogadorDaVez());

        // A ordem publicada é a ordem em que o console narra: primeiro a
        // jogada em si, depois as consequências dela.
        assertEquals(3, recebidos.size());
        assertInstanceOf(CartaJogadaEvento.class, recebidos.get(0));
        assertInstanceOf(CompraForcadaEvento.class, recebidos.get(1));
        assertSame(bia, ((CompraForcadaEvento) recebidos.get(1)).getJogador());
        assertEquals(2, ((CompraForcadaEvento) recebidos.get(1)).getQuantidade());
        assertInstanceOf(JogadorPulouVezEvento.class, recebidos.get(2));
        assertSame(bia, ((JogadorPulouVezEvento) recebidos.get(2)).getJogador());
    }

    @Test
    void jogarCoringaComprarQuatroDeveFazerOProximoJogadorComprarQuatroCartasEPerderAVez() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);
        int cartasDeBiaAntes = bia.getMao().tamanho();

        CartaUno comprarQuatro = new EfeitoComprarQuatro(new CartaCoringa());
        ana.getMao().adicionar(comprarQuatro);

        partida.jogar(comprarQuatro);

        assertEquals(cartasDeBiaAntes + 4, bia.getMao().tamanho());
        assertSame(ana, partida.getJogadorDaVez());
    }

    @Test
    void jogarCartaPularDeveDevolverATurnoParaQuemJogou() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);
        assertSame(ana, partida.getJogadorDaVez());

        List<EventoDoJogo> recebidos = new ArrayList<>();
        partida.adicionarObservador(recebidos::add);

        CartaUno pular = new EfeitoPular(new CartaAcao(CartaUno.Cor.VERMELHO));
        ana.getMao().adicionar(pular);

        partida.jogar(pular);

        // Com 2 jogadores, pular a vez de Bia devolve o turno pra quem jogou.
        assertSame(ana, partida.getJogadorDaVez());

        assertEquals(2, recebidos.size());
        assertInstanceOf(CartaJogadaEvento.class, recebidos.get(0));
        assertInstanceOf(JogadorPulouVezEvento.class, recebidos.get(1));
        assertSame(bia, ((JogadorPulouVezEvento) recebidos.get(1)).getJogador());
    }

    @Test
    void jogarCartaInversaoComDoisJogadoresDeveSeComportarComoPular() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        List<EventoDoJogo> recebidos = new ArrayList<>();
        partida.adicionarObservador(recebidos::add);

        CartaUno inversao = new EfeitoInversao(new CartaAcao(CartaUno.Cor.VERMELHO));
        ana.getMao().adicionar(inversao);

        partida.jogar(inversao);

        assertSame(ana, partida.getJogadorDaVez());
        assertTrue(recebidos.stream().anyMatch(JogadorPulouVezEvento.class::isInstance));
        assertTrue(recebidos.stream().noneMatch(SentidoInvertidoEvento.class::isInstance));
    }

    @Test
    void jogarCartaInversaoComTresJogadoresDeveInverterOSentido() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Jogador<CartaUno> caio = novoJogador("Caio");

        List<CartaUno> cartasIniciais = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            cartasIniciais.add(new CartaNumerica(CartaUno.Cor.VERMELHO, i % 10));
        }
        Baralho<CartaUno> baralho = new Baralho<>(cartasIniciais);
        Partida<CartaUno> partida = new Partida<>(List.of(ana, bia, caio), baralho, regras);
        partida.iniciar();

        List<EventoDoJogo> recebidos = new ArrayList<>();
        partida.adicionarObservador(recebidos::add);

        CartaUno inversao = new EfeitoInversao(new CartaAcao(CartaUno.Cor.VERMELHO));
        ana.getMao().adicionar(inversao);

        partida.jogar(inversao);

        // Sentido invertido: em vez de ir pra Bia (próxima na lista), o
        // turno vai pra Caio (o anterior a Ana, no sentido inverso).
        assertSame(caio, partida.getJogadorDaVez());
        assertTrue(recebidos.stream().anyMatch(SentidoInvertidoEvento.class::isInstance));
        assertTrue(recebidos.stream().noneMatch(JogadorPulouVezEvento.class::isInstance));
    }

    @Test
    void partidaDeveTerminarQuandoUmJogadorFicaSemCartas() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        for (CartaUno carta : List.copyOf(ana.getMao().getCartas())) {
            ana.getMao().remover(carta);
        }
        CartaUno ultimaCarta = new CartaNumerica(CartaUno.Cor.VERMELHO, 3);
        ana.getMao().adicionar(ultimaCarta);

        partida.jogar(ultimaCarta);

        assertTrue(partida.isEncerrada());
        assertSame(ana, partida.getVencedor());
    }

    @Test
    void jogarUmaCartaDeveNotificarObservadoresComOEventoDeCartaJogada() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        List<EventoDoJogo> recebidos = new ArrayList<>();
        partida.adicionarObservador(recebidos::add);

        CartaUno carta = new CartaNumerica(CartaUno.Cor.VERMELHO, 4);
        ana.getMao().adicionar(carta);

        partida.jogar(carta);

        assertEquals(1, recebidos.size());
        assertInstanceOf(CartaJogadaEvento.class, recebidos.get(0));
        assertSame(carta, ((CartaJogadaEvento) recebidos.get(0)).getCarta());
    }

    @Test
    void partidaEncerradaDeveNotificarObservadoresComOEventoDeEncerramento() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        List<EventoDoJogo> recebidos = new ArrayList<>();
        partida.adicionarObservador(recebidos::add);

        for (CartaUno carta : List.copyOf(ana.getMao().getCartas())) {
            ana.getMao().remover(carta);
        }
        CartaUno ultimaCarta = new CartaNumerica(CartaUno.Cor.VERMELHO, 3);
        ana.getMao().adicionar(ultimaCarta);

        partida.jogar(ultimaCarta);

        assertTrue(recebidos.stream().anyMatch(PartidaEncerradaEvento.class::isInstance));
    }
}