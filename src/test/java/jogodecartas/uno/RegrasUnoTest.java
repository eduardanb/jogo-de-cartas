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
import jogodecartas.uno.eventos.PartidaEncerradaEvento;
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
    void jogarCartaComprarDoisDeveFazerOProximoJogadorComprarDuasCartas() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);
        int cartasDeBiaAntes = bia.getMao().tamanho();

        CartaUno comprarDois = new EfeitoComprarDois(new CartaAcao(CartaUno.Cor.VERMELHO));
        ana.getMao().adicionar(comprarDois);

        partida.jogar(comprarDois);

        assertEquals(cartasDeBiaAntes + 2, bia.getMao().tamanho());
    }

    @Test
    void jogarCoringaComprarQuatroDeveFazerOProximoJogadorComprarQuatroCartas() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);
        int cartasDeBiaAntes = bia.getMao().tamanho();

        CartaUno comprarQuatro = new EfeitoComprarQuatro(new CartaCoringa());
        ana.getMao().adicionar(comprarQuatro);

        partida.jogar(comprarQuatro);

        assertEquals(cartasDeBiaAntes + 4, bia.getMao().tamanho());
    }

    @Test
    void jogarCartaPularDeveDevolverATurnoParaQuemJogou() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);
        assertSame(ana, partida.getJogadorDaVez());

        CartaUno pular = new EfeitoPular(new CartaAcao(CartaUno.Cor.VERMELHO));
        ana.getMao().adicionar(pular);

        partida.jogar(pular);

        // Com 2 jogadores, pular a vez de Bia devolve o turno pra quem jogou.
        assertSame(ana, partida.getJogadorDaVez());
    }

    @Test
    void jogarCartaInversaoComDoisJogadoresDeveSeComportarComoPular() throws JogadaInvalidaException {
        RegrasUno regras = new RegrasUno();
        Jogador<CartaUno> ana = novoJogador("Ana");
        Jogador<CartaUno> bia = novoJogador("Bia");
        Partida<CartaUno> partida = novaPartidaIniciada(regras, ana, bia);

        CartaUno inversao = new EfeitoInversao(new CartaAcao(CartaUno.Cor.VERMELHO));
        ana.getMao().adicionar(inversao);

        partida.jogar(inversao);

        assertSame(ana, partida.getJogadorDaVez());
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