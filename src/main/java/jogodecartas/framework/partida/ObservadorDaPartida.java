package jogodecartas.framework.partida;

import jogodecartas.framework.evento.EventoDoJogo;

/**
 * Ponto de extensão (padrão <b>Observer</b>) para reagir a eventos
 * publicados durante uma partida, sem acoplar {@link Partida} a nenhuma
 * forma específica de reação.
 *
 * <p>Exemplos de implementações concretas: exibir o evento no console,
 * gravar um log da partida, atualizar um placar.</p>
 */
public interface ObservadorDaPartida {

    /**
     * Chamado pelo {@link jogodecartas.framework.evento.BarramentoDeEventos}
     * sempre que um evento é publicado.
     */
    void aoReceberEvento(EventoDoJogo evento);
}
