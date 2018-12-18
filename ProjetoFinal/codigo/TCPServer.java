import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;

public class TCPServer extends Thread {

    private InetSocketAddress enderecoServidor;
    private ServerSocket socketServidor;
    private BlockingQueue<String> filaDeMensagens;

    public TCPServer(InetSocketAddress novoEndereco, BlockingQueue<String> fila) {
        this.enderecoServidor = novoEndereco;
        this.filaDeMensagens = fila;
    }

    public void run() {

        try {

            this.socketServidor = new ServerSocket();
            this.socketServidor.bind(this.enderecoServidor);

            while (true) {
                new TCPMessage(socketServidor.accept(), this.filaDeMensagens).start();
            }

        } catch (IOException exception) {
            return;
        } finally {
            this.encerrarServidor();
        }

    }

    public void encerrarServidor() {
        try {
            socketServidor.close();
        } catch (Exception e) { }
    }

}
