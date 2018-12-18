import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Conflitos implements Runnable {

    private int requisicoesEsperadas = 0; // Devem seguir as regras do algoritmo
    private int requisicoesCompletadas = 0; 
    private int maximoDeRequisicoes = 0; 
    private int numeroDeNodes = 0; 

    public static BlockingQueue<List<String>> mensagensRecebidasNovas = new LinkedBlockingQueue<List<String>>();

    private PriorityQueue<VerificarConflito> prioridade;
    private HashMap<Integer, ArrayList<VerificarConflito>> nodesRequests;
    
    public Conflitos(int expectedRequests) {
        this.requisicoesEsperadas = expectedRequests;
        this.prioridade = new PriorityQueue<VerificarConflito>();
        this.nodesRequests = new HashMap<Integer, ArrayList<VerificarConflito>>();
    }

    private void inserirMensagens(VerificarConflito novo) {

        if( ! this.nodesRequests.containsKey(novo.getNodeId()) ) {
            this.nodesRequests.put(novo.getNodeId(), new ArrayList<VerificarConflito>());
        }

        this.nodesRequests.get(novo.getNodeId()).add(novo);
        this.prioridade.add(novo);

    }

    private void validarFilasDeRequisicao() {

        
        if( this.prioridade.size() < 2 ) {
            return;
        }

        
        Iterator<VerificarConflito> loggedRequestIterator = this.prioridade.iterator();
        VerificarConflito previous = null, next = null;

        while( loggedRequestIterator.hasNext() ) {

            VerificarConflito current = loggedRequestIterator.next();

            if( loggedRequestIterator.hasNext() ) {
                next = loggedRequestIterator.next();
            }

            if( previous == null && next == null ) {
                continue; 
            }

            if( previous != null ) {
                current.verificarSeExisteConflito(previous);
            }

            if( next != null ) {
                current.verificarSeExisteConflito(next);
            }

            previous = current;

        }

    }


    @Override
    public void run() {

        // Verificar se recebeu todas as requisicoes que precisa
        while ( this.requisicoesCompletadas < this.requisicoesEsperadas ) {
            try {

                List<String> logMessage = this.mensagensRecebidasNovas.poll(5, TimeUnit.SECONDS);

                if( logMessage == null ) {
                    continue;
                }

                VerificarConflito conflito = new VerificarConflito(logMessage);
                System.gc();

                this.inserirMensagens(conflito);

                this.maximoDeRequisicoes = Math.max(this.maximoDeRequisicoes, conflito.getRequestId());
                this.numeroDeNodes = Math.max(this.numeroDeNodes, conflito.getNodeId());
                this.requisicoesCompletadas++;

            } catch (InterruptedException e) {}
        }

        
        this.validarFilasDeRequisicao();

        
        for( InetSocketAddress nodeAddress : Node.enderecos.values() ) {
            while(true) {
                try {
                    Socket serverSocket = new Socket();
                    serverSocket.connect(nodeAddress);
                    PrintWriter printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
                    printWriter.println("feito");
                    printWriter.close();
                    serverSocket.close();
                    break;
                } catch(Exception exception) {
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                    continue;
                }
            }
        }

    }

}
