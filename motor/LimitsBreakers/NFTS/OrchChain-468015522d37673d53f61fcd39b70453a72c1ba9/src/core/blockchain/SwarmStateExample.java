/* AUTO-GERADO POR TRA$H PROTOCOL v1 */
package core.blockchain;

/**
 * Exemplo de uso do vetor de estado do enxame (swarmState).
 * Classe abstrata para referência arquitetural.
 */
public abstract class SwarmStateExample {
    public static void main(String[] args) {
        // O estado completo de uma civilização de drones assassinos.
        long[] swarmState = new long[10000]; // 10.000 nós, 80 KB de memória. Foda-se.

        // QUERO ATUALIZAR O NÓ 666, ELE FEZ MERDA.
        // Reputação dele vai pra 0.
        swarmState[666] = NodeStateVector.setReputation(swarmState[666], 0);

        // QUERO ACHAR O MELHOR OTÁRIO PRA FAZER UMA TAREFA.
        // Preciso de um nó ativo, com GPU e reputação > 50.
        long mascaraDoDesejo = NodeStateVector.FLAG_ATIVO | NodeStateVector.FLAG_TEM_GPU;
        long melhorOtario = -1;
        int maiorScore = -1;

        for (int i = 0; i < swarmState.length; i++) {
            long noAtual = swarmState[i];
            // 1. Filtro bitwise: o nó tem o que eu quero?
            if ((noAtual & mascaraDoDesejo) == mascaraDoDesejo) {
                int reputacao = NodeStateVector.getReputation(noAtual);
                if (reputacao > 50) {
                    // 2. Lógica de score: reputação alta, CPU baixa.
                    int score = reputacao - NodeStateVector.getCpuLoad(noAtual);
                    if (score > maiorScore) {
                        maiorScore = score;
                        melhorOtario = i; // Achei o trouxa!
                    }
                }
            }
        }
    }
}
