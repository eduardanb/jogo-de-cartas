package jogodecartas.framework.carta;

/**
 * Naipes do baralho francês tradicional (52 cartas).
 *
 * <p><b>Componente reutilizável opcional:</b> jogos baseados no baralho
 * francês (ex.: Blackjack, Truco, Poker) podem usar este enum ao compor sua
 * {@link Carta} concreta. Jogos com sistema próprio de classificação (ex.:
 * Uno, que usa cores) simplesmente não o utilizam — {@code Naipe} não faz
 * parte do contrato de {@link Carta}.</p>
 */
public enum Naipe {
    OUROS,
    ESPADAS,
    COPAS,
    PAUS
}
