import java.util.Arrays;

public class VectorClock {

    private int[] vector;
    private int donoID;

    public VectorClock(int numProcesses, int ownerProcessId) {
        this.donoID = ownerProcessId;
        this.vector = new int[numProcesses];
        this.vector[ownerProcessId] = 1;
    }

    public VectorClock(int[] aVector, int ownerProcessId) {
        this.donoID = ownerProcessId;
        this.vector = new int[aVector.length];
        for( int i = 0; i < aVector.length; i++ ) {
            this.vector[i] = aVector[i];
        }
    }

    public VectorClock(String vectorClockData) {

        String[] split = vectorClockData.split("\\[");
        String[] valoresDoVetor = split[1].substring(0, split[1].length() - 1).split(":");

        this.donoID = Integer.parseInt(split[0]);
        this.vector = new int[valoresDoVetor.length];

        for( int i = 0; i < this.vector.length; i++ ) {
            this.vector[i] = Integer.parseInt(valoresDoVetor[i]);
        }

    }

    public int getDonoClock() {
        return this.donoID;
    }

    public String enviarVetorDeClock() {
        this.vector[this.donoID]++;
        return this.toString();
    }

    public void receber(String novo) {
        this.comparar(new VectorClock(novo));
    }

    public void comparar(VectorClock vetorDeClockRecebido) {

        for( int i = 0; i < this.vector.length; i++ ) {
            this.vector[i] = Math.max(this.vector[i], vetorDeClockRecebido.vector[i]);
        }
        this.vector[this.donoID]++;
    }

    public boolean happenedBefore(VectorClock outroVetorDeClock) {

        if( outroVetorDeClock == null ) {
            return false;
        }

        boolean atLeastOneLessThan = false;

        for( int i = 0; i < this.vector.length; i++ ) {


            if( this.vector[i] > outroVetorDeClock.vector[i] ) {
                return false; 
            }

            if( this.vector[i] < outroVetorDeClock.vector[i] ) {
                atLeastOneLessThan = true;
            }

        }

        return atLeastOneLessThan;

    }

    public int getValorLogico() {

        int sum = 0;

        for( int val : this.vector ) {
            sum += val;
        }

        return sum;

    }

    @Override
    public String toString() {
        String out = String.format("%d[", this.getDonoClock());
        for( int val : vector ) {
            out += String.format("%d:", val);
        }
        return out.substring(0, out.length() - 1) + "]";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VectorClock outro = (VectorClock) o;

        if (this.donoID != outro.donoID) {
            return false;
        }

        return Arrays.equals(this.vector, outro.vector);

    }

}
