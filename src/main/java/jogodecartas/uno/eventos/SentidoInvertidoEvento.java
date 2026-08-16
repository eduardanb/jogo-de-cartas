package jogodecartas.uno.eventos;

import jogodecartas.framework.evento.EventoDoJogo;

/**
 * Evento publicado (padrão Observer) quando uma carta de Inversão troca o
 * sentido da partida — só acontece com três ou mais jogadores; com dois,
 * Inversão publica {@link JogadorPulouVezEvento} em vez deste.
 */
public final class SentidoInvertidoEvento extends EventoDoJogo {
}
