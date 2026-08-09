package jogodecartas.framework.evento;

import jogodecartas.framework.partida.ObservadorDaPartida;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente reutilizável que implementa o lado "sujeito" do padrão
 * <b>Observer</b>: mantém a lista de observadores inscritos e notifica todos
 * eles sempre que um {@link EventoDoJogo} é publicado.
 *
 * <p>A lista de observadores é encapsulada — não é exposta diretamente,
 * apenas manipulada através de {@link #inscrever} e {@link #desinscrever}.</p>
 */
public final class BarramentoDeEventos {

    private final List<ObservadorDaPartida> observadores = new ArrayList<>();

    public void inscrever(ObservadorDaPartida observador) {
        observadores.add(observador);
    }

    public void desinscrever(ObservadorDaPartida observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica todos os observadores inscritos, na ordem de inscrição.
     */
    public void publicar(EventoDoJogo evento) {
        for (ObservadorDaPartida observador : observadores) {
            observador.aoReceberEvento(evento);
        }
    }
}
