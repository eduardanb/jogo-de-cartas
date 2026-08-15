package jogodecartas.framework.partida;

import jogodecartas.framework.baralho.Baralho;
import jogodecartas.framework.carta.Carta;
import jogodecartas.framework.evento.BarramentoDeEventos;
import jogodecartas.framework.excecao.JogadaInvalidaException;
import jogodecartas.framework.jogador.Jogador;
import jogodecartas.framework.regras.RegrasDoJogo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Componente reutilizável que orquestra uma partida: mantém os jogadores, o
 * baralho, de quem é a vez e delega toda a variação de comportamento para a
 * {@link RegrasDoJogo} injetada.
 *
 * <p>Não é um ponto de extensão por herança — a variação entre jogos fica
 * inteiramente a cargo de {@link RegrasDoJogo} e
 * {@link jogodecartas.framework.estrategia.EstrategiaDeJogo}, o que mantém
 * {@code Partida} concreta e reutilizável por qualquer jogo suportado pelo
 * framework.</p>
 *
 * <p>Embora o enunciado do projeto considere partidas de dois jogadores, a
 * lista de jogadores não tem esse limite fixado em código, para permitir
 * jogos com mais participantes no futuro.</p>
 *
 * @param <C> tipo concreto de carta do jogo
 */
public final class Partida<C extends Carta> {

    private final List<Jogador<C>> jogadores;
    private final Baralho<C> baralho;
    private final RegrasDoJogo<C> regras;
    private final BarramentoDeEventos eventos = new BarramentoDeEventos();

    private int indiceJogadorDaVez = 0;
    private boolean iniciada = false;
    private boolean encerrada = false;

    public Partida(List<Jogador<C>> jogadores, Baralho<C> baralho, RegrasDoJogo<C> regras) {
        if (jogadores == null || jogadores.size() < 2) {
            throw new IllegalArgumentException("Uma partida requer ao menos dois jogadores.");
        }
        this.jogadores = new ArrayList<>(jogadores);
        this.baralho = baralho;
        this.regras = regras;
    }

    /**
     * Embaralha o baralho e distribui as cartas iniciais, delegando para
     * {@link RegrasDoJogo#distribuirCartas}.
     */
    public void iniciar() {
        baralho.embaralhar();
        regras.distribuirCartas(this);
        iniciada = true;
    }

    /**
     * Executa a jogada de {@code carta} pelo jogador da vez e avança o turno
     * para o próximo jogador.
     *
     * @throws IllegalStateException    se a partida ainda não foi iniciada ou já está encerrada
     * @throws JogadaInvalidaException  se a jogada não for válida segundo as regras do jogo
     */
    public void jogar(C carta) throws JogadaInvalidaException {
        if (!iniciada) {
            throw new IllegalStateException("A partida ainda não foi iniciada.");
        }
        if (encerrada) {
            throw new IllegalStateException("A partida já está encerrada.");
        }

        Jogador<C> jogadorDaVez = getJogadorDaVez();
        regras.executarJogada(this, jogadorDaVez, carta);

        if (regras.partidaEncerrada(this)) {
            encerrada = true;
        } else {
            avancarTurno();
        }
    }

    private void avancarTurno() {
        indiceJogadorDaVez = regras.proximoIndice(this, indiceJogadorDaVez);
    }

    public Jogador<C> getJogadorDaVez() {
        return jogadores.get(indiceJogadorDaVez);
    }

    /**
     * Delega para {@link RegrasDoJogo#jogadaValida} a verificação de se
     * {@code carta} pode ser jogada agora pelo jogador da vez. Útil para uma
     * {@link jogodecartas.framework.estrategia.EstrategiaDeJogo} decidir uma
     * jogada sem precisar conhecer as regras concretas do jogo.
     */
    public boolean jogadaValida(C carta) {
        return regras.jogadaValida(this, getJogadorDaVez(), carta);
    }

    public boolean isIniciada() {
        return iniciada;
    }

    public boolean isEncerrada() {
        return encerrada;
    }

    /**
     * @return o vencedor apurado pelas regras do jogo
     * @throws IllegalStateException se a partida ainda não terminou
     */
    public Jogador<C> getVencedor() {
        if (!encerrada) {
            throw new IllegalStateException("A partida ainda não terminou.");
        }
        return regras.apurarVencedor(this);
    }

    public Baralho<C> getBaralho() {
        return baralho;
    }

    /**
     * @return as regras que governam esta partida — útil para uma
     *         {@link jogodecartas.framework.estrategia.EstrategiaDeJogo} que
     *         precise consultar estado específico do jogo concreto
     */
    public RegrasDoJogo<C> getRegras() {
        return regras;
    }

    /**
     * @return visão somente-leitura dos jogadores da partida
     */
    public List<Jogador<C>> getJogadores() {
        return Collections.unmodifiableList(jogadores);
    }

    /**
     * Inscreve um observador para ser notificado dos eventos publicados
     * durante esta partida.
     */
    public void adicionarObservador(ObservadorDaPartida observador) {
        eventos.inscrever(observador);
    }

    /**
     * @return o barramento de eventos desta partida, usado por
     *         {@link RegrasDoJogo} para publicar eventos específicos do jogo
     */
    public BarramentoDeEventos getEventos() {
        return eventos;
    }
}
