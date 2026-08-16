package jogodecartas.framework.partida;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.evento.EventoDoJogo;
import jogodecartas.framework.excecao.JogadaInvalidaException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.testutil.CartaFalsa;
import jogodecartas.framework.testutil.RegrasFalsas;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    private Jogador<CartaFalsa> novoJogador(String nome) {
        return new Jogador<>(nome, (jogador, partida) -> null);
    }

    @Test
    void naoDevePermitirPartidaComMenosDeDoisJogadores() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());
        RegrasFalsas regras = new RegrasFalsas();

        assertThrows(IllegalArgumentException.class,
                () -> new Partida<>(List.of(novoJogador("Ana")), baralho, regras));
    }

    @Test
    void iniciarDeveEmbaralharEDelegarDistribuicaoParaAsRegras() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of(new CartaFalsa("A")));
        RegrasFalsas regras = new RegrasFalsas();
        Partida<CartaFalsa> partida = new Partida<>(List.of(novoJogador("Ana"), novoJogador("Bia")), baralho, regras);

        partida.iniciar();

        assertTrue(partida.isIniciada());
        assertEquals(1, regras.chamadasDistribuir);
    }

    @Test
    void jogarAntesDeIniciarDeveLancarExcecao() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());
        RegrasFalsas regras = new RegrasFalsas();
        Partida<CartaFalsa> partida = new Partida<>(List.of(novoJogador("Ana"), novoJogador("Bia")), baralho, regras);

        assertThrows(IllegalStateException.class, () -> partida.jogar(new CartaFalsa("A")));
    }

    @Test
    void jogadaValidaDeveAplicarEAvancarParaOProximoJogador() throws JogadaInvalidaException {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());
        RegrasFalsas regras = new RegrasFalsas();
        Jogador<CartaFalsa> ana = novoJogador("Ana");
        Jogador<CartaFalsa> bia = novoJogador("Bia");
        Partida<CartaFalsa> partida = new Partida<>(List.of(ana, bia), baralho, regras);
        partida.iniciar();

        assertSame(ana, partida.getJogadorDaVez());

        partida.jogar(new CartaFalsa("A"));

        assertEquals(1, regras.chamadasAplicar);
        assertSame(bia, partida.getJogadorDaVez());
        assertFalse(partida.isEncerrada());
    }

    @Test
    void jogadaInvalidaDeveLancarExcecaoENaoAvancarOTurno() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());
        RegrasFalsas regras = new RegrasFalsas();
        regras.tornarProximaJogadaInvalida();
        Jogador<CartaFalsa> ana = novoJogador("Ana");
        Jogador<CartaFalsa> bia = novoJogador("Bia");
        Partida<CartaFalsa> partida = new Partida<>(List.of(ana, bia), baralho, regras);
        partida.iniciar();

        assertThrows(JogadaInvalidaException.class, () -> partida.jogar(new CartaFalsa("A")));
        assertSame(ana, partida.getJogadorDaVez());
        assertEquals(0, regras.chamadasAplicar);
    }

    @Test
    void jogadaQueEncerraAPartidaDeveDisponibilizarOVencedor() throws JogadaInvalidaException {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());
        RegrasFalsas regras = new RegrasFalsas();
        regras.encerrarAoJogar();
        Jogador<CartaFalsa> ana = novoJogador("Ana");
        Jogador<CartaFalsa> bia = novoJogador("Bia");
        regras.definirVencedor(ana);
        Partida<CartaFalsa> partida = new Partida<>(List.of(ana, bia), baralho, regras);
        partida.iniciar();

        partida.jogar(new CartaFalsa("A"));

        assertTrue(partida.isEncerrada());
        assertSame(ana, partida.getVencedor());
    }

    @Test
    void getVencedorAntesDaPartidaTerminarDeveLancarExcecao() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());
        RegrasFalsas regras = new RegrasFalsas();
        Partida<CartaFalsa> partida = new Partida<>(List.of(novoJogador("Ana"), novoJogador("Bia")), baralho, regras);
        partida.iniciar();

        assertThrows(IllegalStateException.class, partida::getVencedor);
    }

    @Test
    void adicionarObservadorDevePermitirReceberEventosPublicados() {
        Baralho<CartaFalsa> baralho = new Baralho<>(List.of());
        RegrasFalsas regras = new RegrasFalsas();
        Partida<CartaFalsa> partida = new Partida<>(List.of(novoJogador("Ana"), novoJogador("Bia")), baralho, regras);

        List<EventoDoJogo> recebidos = new ArrayList<>();
        partida.adicionarObservador(recebidos::add);

        EventoDoJogo evento = new EventoDoJogo() {};
        partida.getEventos().publicar(evento);

        assertEquals(1, recebidos.size());
        assertSame(evento, recebidos.get(0));
    }
}