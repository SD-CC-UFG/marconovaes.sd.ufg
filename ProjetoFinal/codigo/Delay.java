import java.util.Random;


public class Delay {

    private double delayRequisicao, delaySessaoCritica;
    private Random aleatorio = new Random(), csDelayRandom = new Random();

    public Delay(int delayReq, int delaySC) {
        this.delayRequisicao = 1.0 / (double)delayReq;
        this.delaySessaoCritica = 1.0 / (double)delaySC;
    }

    public int getIRDelay() {
        return gerarNumeroAleatorio(this.aleatorio, delayRequisicao);
    }

    public long getCSDelay() {
        return gerarNumeroAleatorio(this.csDelayRandom, delaySessaoCritica);
    }

    private static int gerarNumeroAleatorio(Random rand, double mean) {
        double randVal = Math.log(1 - rand.nextDouble()) / (0.0 - mean);
        return (int) Math.ceil(randVal);
    }

    @Override
    public String toString() {
        return String.format("{ Delay de REQUEST: %d, Delay de Sessao Critica: %d }", (int) (1.0 / this.delayRequisicao), (int) (1.0 / this.delaySessaoCritica));
    }

}
