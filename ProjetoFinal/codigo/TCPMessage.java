import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class TCPMessage extends Thread {

    private Socket receptor;
    private BlockingQueue<String> filaDeMensagens;

    public TCPMessage(Socket novoSocket, BlockingQueue<String> fila) {
        this.receptor = novoSocket;
        this.filaDeMensagens = fila;
    }

    public void run() {
        try {
            BufferedReader dado = new BufferedReader(new InputStreamReader(this.receptor.getInputStream()));
            String dadoRecebido = dado.readLine().trim();
            filaDeMensagens.put(dadoRecebido);
            dado.close();
            receptor.close();
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }

}
