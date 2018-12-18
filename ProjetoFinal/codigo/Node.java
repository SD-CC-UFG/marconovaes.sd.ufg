import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Node implements Runnable {

    public static boolean visualizacao = false;

    public static void main(String[] args) {

    	//Formato do argumento
    	//--verbose necessario para visualizar as mensagens sendo enviadas (opcional)
        if( args.length < 2 ) {
            System.out.println("Usage: ID do Node(Processos) <node id> <localizacao> (--verbose)");
            System.exit(1);
        }

        Node.visualizacao = ( args.length >= 3 && args[2].equals("--verbose") );

        Node node = null;

        try {
            node = ConfigFile.configurarNode(Integer.parseInt(args[0]), ConfigFile.separadorDeDadosDeEntrada(args[1]));
        } catch (IOException e) {
            System.err.printf("Falha na leitura do arquivo: %s\nsaindo...!\n", e);
            System.exit(1);
        }

        if( node != null ) {
            if( Node.visualizacao ) {
                System.out.println(node);
            }
            node.run();

            System.out.flush();

        }

        System.exit(0);

    }

    public static int nodeId;

    public static HashMap<Integer, InetSocketAddress> enderecos = new HashMap<Integer, InetSocketAddress>();
    public static HashMap<Integer, InetSocketAddress> subconjunto = new HashMap<Integer, InetSocketAddress>();

    public static final Object Lock = new Object();
    public static VectorClock relogio;

    private int totalRequisicoes;
    private Delay delays;

    private BlockingQueue<String> messageQueue;
    private TCPServer tcpServer;
    private Decisor coordenador;
    private Thread verificador;

    public Node(int qtNodes, int numReqs, Delay delays, InetSocketAddress socketAddress) {

        this.delays = delays;
        this.totalRequisicoes = numReqs;
        this.messageQueue = new LinkedBlockingQueue<String>();
        this.tcpServer = new TCPServer(socketAddress, this.messageQueue);
        this.coordenador = new Decisor(this.messageQueue);

        relogio = new VectorClock(qtNodes, Node.nodeId);

        if( Node.nodeId == 0 ) {
            this.verificador = new Thread(new Conflitos(qtNodes * this.totalRequisicoes));
            this.verificador.start();
        }

    }

    private void sessaoCritica() {

        Mensagem.multicast(Mensagem.Type.REQUEST, Node.subconjunto.values());

        ArrayList<Integer> respostas = new ArrayList<Integer>();

        while (true) {
            try {

                if( respostas.size() == Node.subconjunto.size() ) {
                    this.coordenador.entrouNaSessaoCritica.set(true);
                    this.coordenador.mensagens.clear();
                    break;
                }

                Mensagem applicationMessage = this.coordenador.mensagens.poll(1, TimeUnit.SECONDS);

                if( applicationMessage != null ) {

                    if( applicationMessage.getConteudo(Mensagem.Type.GRANT) && Node.subconjunto.containsKey(applicationMessage.getRemetenteID()) ) {
                        respostas.add(new Integer(applicationMessage.getRemetenteID()));
                    }

                    if( applicationMessage.getConteudo(Mensagem.Type.FAILED) && Node.subconjunto.containsKey(applicationMessage.getRemetenteID()) ) {
                        respostas.remove(new Integer(applicationMessage.getRemetenteID()));
                    }

                    if( applicationMessage.getConteudo(Mensagem.Type.INQUIRE) && Node.subconjunto.containsKey(applicationMessage.getRemetenteID()) ) {

                        Mensagem.send(Mensagem.Type.YIELD, applicationMessage.getRemetenteID());

                        Integer processId = applicationMessage.getRemetenteID();

                        if( respostas.contains(processId) ) {
                            respostas.remove(processId);
                        }

                    }

                }

            } catch (InterruptedException e) { }
        }

    }

    private int deixarSessaoCritica() {

        int exitCSTime = 0;
        
        // Previnir que duas thread atrapalhem o processo da outra
        synchronized (Lock) {
            Node.relogio.enviarVetorDeClock();
            exitCSTime = Node.relogio.getValorLogico();
        }

        Mensagem.multicast(Mensagem.Type.RELEASE, Node.subconjunto.values());

        this.coordenador.entrouNaSessaoCritica.set(false);

        return exitCSTime;

    }

    @Override
    public void run() {

        this.tcpServer.start();
        this.coordenador.start();

        for( int requestId = 1; requestId <= this.totalRequisicoes; requestId++ ) {

            this.sessaoCritica();

            int enterCSTime = 0, exitCSTime = 0;

            // 2. Previnir que outra thread atrapalhe na sessao critica
            synchronized (Lock) {
                enterCSTime = relogio.getValorLogico();
            }

            try {
            	//Execucao da sessao critica
                Thread.sleep(this.delays.getCSDelay());
            } catch (InterruptedException e) {}




            exitCSTime = this.deixarSessaoCritica();


            try {
                Thread.sleep(this.delays.getIRDelay());
            } catch (InterruptedException e) {}

        }

        try {
            coordenador.join();
        } catch (InterruptedException e) {}

        try {
            tcpServer.encerrarServidor();
            tcpServer.join();
        } catch (Exception e) { }

        if( this.verificador != null ) {
            try {
                this.verificador.join();
            } catch (InterruptedException e) {}
        }

    }

    @Override
    public String toString() {

        StringBuilder conversorDeSaida = new StringBuilder();

        conversorDeSaida.append(String.format("Node ID: %d\n", Node.nodeId));
        conversorDeSaida.append(String.format("Requisicoes: %d\n", this.totalRequisicoes));
        conversorDeSaida.append(String.format("Delays: %s\n", this.delays));

        synchronized (Lock) {
            conversorDeSaida.append(String.format("Vetor de Clock Inicial: %s\n", Node.relogio));
        }

        conversorDeSaida.append("Nos Vizinhos:\n");
        Set<Integer> membrosDoGrupo = Node.subconjunto.keySet();
        for( Map.Entry<Integer, InetSocketAddress> vizinho : Node.enderecos.entrySet() ) {
            conversorDeSaida.append(String.format(" >> [%s] Node(%d) at %s\n", membrosDoGrupo.contains(vizinho.getKey()) ? "Q" : "-", vizinho.getKey(), vizinho.getValue() ));
        }

        return conversorDeSaida.toString();

    }


}
