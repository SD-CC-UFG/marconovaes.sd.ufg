import java.util.Arrays;
import java.util.List;

public class VerificarConflito implements Comparable<VerificarConflito> {

    private int nodeId;
    private int requestId;
    private int tempoDeInicio;
    private int tempoFinal;
    private VerificarConflito conflito;

    public VerificarConflito(List<String> rawData) {
        this.nodeId = Integer.parseInt(rawData.get(0));
        this.requestId = Integer.parseInt(rawData.get(1));
        this.tempoDeInicio = Integer.parseInt(rawData.get(2));
        this.tempoFinal = Integer.parseInt(rawData.get(3));
        this.conflito = null;
    }

    @Override
    public int compareTo(VerificarConflito that) {

        if( this.tempoDeInicio == that.tempoDeInicio ) {
            return ( this.nodeId < that.nodeId ) ? -1 : 1;
        }

        return ( this.tempoDeInicio < that.tempoDeInicio ) ? -1 : 1;

    }

    public int getNodeId() {
        return this.nodeId;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public boolean verificarConflito() {
        return ( this.conflito != null );
    }

    public VerificarConflito getRequisicoesConflitantes() {
        return this.conflito;
    }

    public void setConflito(VerificarConflito loggedRequest) {
        this.conflito = loggedRequest;
    }

    private static boolean ValInRange(int val, int a, int b) {
        return ( val > a && val < b );
    }

    public void verificarSeExisteConflito(VerificarConflito otherRequest) {

        if( this.verificarConflito() ) {
            return;
        }

        if(  ValInRange(otherRequest.tempoDeInicio, this.tempoDeInicio, this.tempoFinal) || ValInRange(otherRequest.tempoFinal, this.tempoDeInicio, this.tempoFinal) ) {
            this.conflito = otherRequest;
        }

    }

}
