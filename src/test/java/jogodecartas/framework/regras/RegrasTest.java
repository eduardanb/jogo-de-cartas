package jogodecartas.framework.regras;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.excecao.JogadaInvalidaException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.partida.Partida;
import jogodecartas.framework.testutil.CartaFalsa;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegrasTest {

    /** Regras de teste: só permite jogar cartas cuja descrição comece com "V". */
    private static class RegrasQueValidamPorPrefixo extends RegrasDoJogo<CartaFalsa> {
        boolean aplicarJogadaChamado = false;

        @Override
        public void distribuirCartas(Partida<CartaFalsa> partida) {}

        @Override
        public boolean jogadaValida(Partida<CartaFalsa> partida, Jogador<CartaFalsa> jogador, CartaFalsa carta) {
            return carta.getDescricao().startsWith("V");
        }

        @Override
        protected void aplicarJogada(Partida<CartaFalsa> partida, Jogador<CartaFalsa> jogador, CartaFalsa carta) {
            aplicarJogadaChamado = true;
        }

        @Override
        public boolean partidaEncerrada(Partida<CartaFalsa> partida) {
            return false;
        }

        @Override
        public Jogador<CartaFalsa> apurarVencedor(Partida<CartaFalsa> partida) {
            return null;
        }
    }

    private Jogador<CartaFalsa> novoJogador(String nome) {
        return new Jogador<>(nome, (jogador, partida) -> null);
    }

    @Test
    void executarJogadaDeveAplicarQuandoAJogadaEValida() throws JogadaInvalidaException {
        RegrasQueValidamPorPrefixo regras = new RegrasQueValidamPorPrefixo();
        Jogador<CartaFalsa> jogador = novoJogador("Ana");
        Partida<CartaFalsa> partida = new Partida<>(List.of(jogador, novoJogador("Bia")),
                new Baralho<>(List.of()), regras);

        regras.executarJogada(partida, jogador, new CartaFalsa("Vermelho 5"));

        assertTrue(regras.aplicarJogadaChamado);
    }

    @Test
    void executarJogadaDeveLancarExcecaoQuandoAJogadaEInvalidaENaoDeveAplicar() {
        RegrasQueValidamPorPrefixo regras = new RegrasQueValidamPorPrefixo();
        Jogador<CartaFalsa> jogador = novoJogador("Ana");
        Partida<CartaFalsa> partida = new Partida<>(List.of(jogador, novoJogador("Bia")),
                new Baralho<>(List.of()), regras);
        CartaFalsa cartaInvalida = new CartaFalsa("Azul 3");

        JogadaInvalidaException excecao = assertThrows(JogadaInvalidaException.class,
                () -> regras.executarJogada(partida, jogador, cartaInvalida));

        assertFalse(regras.aplicarJogadaChamado);
        assertTrue(excecao.getMessage().contains("Azul 3"));
    }

    @Test
    void proximoIndicePadraoDeveAvancarCiclicamenteNaOrdemDaLista() {
        RegrasQueValidamPorPrefixo regras = new RegrasQueValidamPorPrefixo();
        Jogador<CartaFalsa> ana = novoJogador("Ana");
        Jogador<CartaFalsa> bia = novoJogador("Bia");
        Jogador<CartaFalsa> caio = novoJogador("Caio");
        Partida<CartaFalsa> partida = new Partida<>(List.of(ana, bia, caio),
                new Baralho<>(List.of()), regras);

        assertEquals(1, regras.proximoIndice(partida, 0));
        assertEquals(2, regras.proximoIndice(partida, 1));
        assertEquals(0, regras.proximoIndice(partida, 2)); // volta pro início
    }
}