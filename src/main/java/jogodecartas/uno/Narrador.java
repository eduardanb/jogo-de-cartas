package jogodecartas.uno;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Ponto único de saída narrativa de uma partida de UNO no console.
 *
 * <p>Estrutura cada turno em um pequeno bloco visual: {@link #separador()}
 * marca o início do bloco, {@link #anunciar} narra a ação principal (uma
 * jogada, uma compra), e {@link #anunciarEfeito} narra, indentado, uma
 * consequência dessa ação (pular a vez, inverter o sentido, compra
 * forçada) -- deixando claro no console o que é causa e o que é efeito, em
 * vez de uma lista plana de mensagens soltas.</p>
 *
 * <p>Cada mensagem narrada (exceto o separador, que é só decoração) é
 * seguida de uma pequena pausa, para que uma partida com vários bots -- que,
 * sem isso, despejaria dezenas de linhas instantaneamente -- fique
 * acompanhável a olho nu. Não é usada para prompts interativos (menu,
 * escolha de carta): ali o atraso só atrapalharia quem está digitando.</p>
 */
public final class Narrador {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final long ATRASO_MS = 1200;
    private static final String SEPARADOR = "-".repeat(32);

    private Narrador() {
    }

    /**
     * Marca visualmente o início de um novo bloco (o resultado de um turno,
     * ou um evento avulso como o fim da partida). Não pausa: é só decoração.
     */
    public static void separador() {
        System.out.println(SEPARADOR);
    }

    /**
     * Narra a ação principal do bloco atual (ex.: "Fulano jogou: carta").
     */
    public static void anunciar(String mensagem) {
        System.out.println("[" + LocalTime.now().format(FORMATO_HORA) + "] " + mensagem);
        pausar();
    }

    /**
     * Narra, indentado, uma consequência da ação principal já anunciada
     * (ex.: "Fulano perde a vez.", causada por uma carta Pular).
     */
    public static void anunciarEfeito(String mensagem) {
        System.out.println("            > " + mensagem);
        pausar();
    }

    private static void pausar() {
        try {
            Thread.sleep(ATRASO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
