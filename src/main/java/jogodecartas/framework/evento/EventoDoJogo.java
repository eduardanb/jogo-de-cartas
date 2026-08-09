package jogodecartas.framework.evento;

import java.time.Instant;

/**
 * Ponto de extensão (parte do padrão <b>Observer</b>) que representa um
 * evento ocorrido durante uma partida.
 *
 * <p>Propositalmente não define nenhum dado além do instante em que ocorreu:
 * cada jogo concreto cria suas próprias subclasses para os eventos que fazem
 * sentido para ele (ex.: "carta comprada", "sentido do jogo invertido",
 * "jogador estourou 21"), e publica essas instâncias através do
 * {@link BarramentoDeEventos} da partida. Isso mantém o framework agnóstico
 * a quais eventos cada jogo precisa emitir.</p>
 */
public abstract class EventoDoJogo {

    private final Instant momento = Instant.now();

    public Instant getMomento() {
        return momento;
    }
}
