import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;

public class Mensagem {

    public static enum Type { REQUEST, YIELD, INQUIRE, RELEASE, FAILED, GRANT}

    private Type tipoMensagem;
    private int nodeRemetenteID;
    private VectorClock sourceVectorClock;

    public Object data = null;

    public static void send(Type mensagem, int nodeDestinatario) {

        InetSocketAddress nodeAddress = Node.enderecos.get(new Integer(nodeDestinatario));

        if( nodeAddress == null ) {
            return;
        }

        send(mensagem, nodeAddress);

    }

    public static void send(Type mensagem, InetSocketAddress nodeDestinatario) {

        String message = null;

        synchronized (Node.Lock) {
            message = String.format("%s(%s,%d)", mensagem.name(), Node.relogio.enviarVetorDeClock(), Node.nodeId);
        }

        send(message, nodeDestinatario);

    }

    public static void multicast(Type mensagem, Collection<InetSocketAddress> nodeDestinatario) {

        String message = null;

        synchronized (Node.Lock) {
            message = String.format("%s(%s,%d)", mensagem.name(), Node.relogio.enviarVetorDeClock(), Node.nodeId);
        }

        for( InetSocketAddress nodeAddress : nodeDestinatario ) {
            send(message, nodeAddress);
        }

    }

    public static void send(String mensagem, InetSocketAddress nodeDestinatario) {
        send(mensagem, nodeDestinatario, true);
    }

    public static void send(String data, InetSocketAddress address, boolean retry) {

        if( address == null ) {
            return;
        }

        while(true) {
            try {
                Socket serverSocket = new Socket();
                serverSocket.connect(address);
                PrintWriter printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
                printWriter.println(data);
                printWriter.close();
                serverSocket.close();
                return;
            } catch(Exception exception) {


                if( ! retry ) {
                    return;
                }

                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                continue;
            }
        }

    }

    public static Mensagem receberMensagem(String mensagem) {

        if( mensagem == null || mensagem.length() == 0 ) {
            return null;
        }

        String[] mensagemCompleta = mensagem.substring(0, mensagem.length() - 1).split("\\(");

        String tipoMensagem = mensagemCompleta[0];
        String[] argumentos = mensagemCompleta[1].split(",");

        Mensagem message = null;

        if( tipoMensagem.equals("REQUEST") ) {
            message = new Mensagem(Integer.parseInt(argumentos[1]), new VectorClock(argumentos[0]));
            message.tipoMensagem = Type.REQUEST;
            System.out.println("Request!");
        }

        else if( tipoMensagem.equals("YIELD") ) {
            message = new Mensagem(Integer.parseInt(argumentos[1]), new VectorClock(argumentos[0]));
            message.tipoMensagem = Type.YIELD;
            System.out.println("Yield!");
        }

        else if( tipoMensagem.equals("INQUIRE") ) {
            message = new Mensagem(Integer.parseInt(argumentos[1]), new VectorClock(argumentos[0]));
            message.tipoMensagem = Type.INQUIRE;
            System.out.println("Inquire!");
        }

        else if( tipoMensagem.equals("RELEASE") ) {
            message = new Mensagem(Integer.parseInt(argumentos[1]), new VectorClock(argumentos[0]));
            message.tipoMensagem = Type.RELEASE;
            System.out.println("Release!");
        }

        else if( tipoMensagem.equals("FAILED") ) {
            message = new Mensagem(Integer.parseInt(argumentos[1]), new VectorClock(argumentos[0]));
            message.tipoMensagem = Type.FAILED;
            System.out.println("Falha!");
        }

        else if( tipoMensagem.equals("GRANT") ) {
            message = new Mensagem(Integer.parseInt(argumentos[1]), new VectorClock(argumentos[0]));
            message.tipoMensagem = Type.GRANT;
            System.out.println("Grant!");
        }

      
        return message;

    }

    public Mensagem(int sourceProcessId, VectorClock sourceVectorClock) {
        this.nodeRemetenteID = sourceProcessId;
        this.sourceVectorClock = sourceVectorClock;
    }

    public boolean getConteudo(Mensagem.Type tipo) {
        return ( this.tipoMensagem == tipo );
    }

    public int getRemetenteID() {
        return this.nodeRemetenteID;
    }

    public VectorClock getSourceVectorClock() {
        return this.sourceVectorClock;
    }

    public VerificaRequisicao getRequest() {

        if( ! this.getConteudo(Type.REQUEST) ) {
            return null;
        }

        return new VerificaRequisicao(this);

    }

}
