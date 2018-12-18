import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Decisor extends Thread {

    private BlockingQueue<String> mensagensChegandoNoNode;

    private PriorityQueue<VerificaRequisicao> filaDeRequisicao =  new PriorityQueue<VerificaRequisicao>();
    private VerificaRequisicao permissao = null;

    public BlockingQueue<Mensagem> mensagens = new LinkedBlockingQueue<Mensagem>();
    private AtomicBoolean todasRequisicoesFinalizadas = new AtomicBoolean(false);
    public AtomicBoolean entrouNaSessaoCritica = new AtomicBoolean(false);

    public Decisor(BlockingQueue<String> mensangensChegando) {
        this.mensagensChegandoNoNode = mensangensChegando;
    }

    @Override
    public void run() {
        while (true) {
            try {

                if( this.todasRequisicoesFinalizadas.get() && this.mensagensChegandoNoNode.isEmpty() && this.filaDeRequisicao.isEmpty() && this.permissao == null ) {
                    return;
                }

                String dado = this.mensagensChegandoNoNode.poll(1, TimeUnit.SECONDS);

                if( dado == null ) {
                    continue;
                }

                this.validarMensagem(Mensagem.receberMensagem(dado));

            } catch (InterruptedException e) {}
        }
    }

    private void validarMensagem(Mensagem mensagemRecebida) {

        // Caso a mensagens tenha um valor nao conhecido, nao seja os valores enumerados
        if( mensagemRecebida == null ) {
            return;
        }

        

        synchronized (Node.Lock) {
            Node.relogio.comparar(mensagemRecebida.getSourceVectorClock());
        }

        if( mensagemRecebida.getConteudo(Mensagem.Type.REQUEST) ) {

            VerificaRequisicao novaRequisicao = mensagemRecebida.getRequest();
            

            // Enviar GRANT se nao enviamos para ninguem, junto com o vetor de clock ja verificado
            if( permissao == null ) {

                this.permissao = novaRequisicao;
                Mensagem.send(Mensagem.Type.GRANT, this.permissao.getProcessId());

                return;

            }

            if( requestChangesHead(novaRequisicao) ) {

                Iterator<VerificaRequisicao> requestIterator = this.filaDeRequisicao.iterator();
                while(requestIterator.hasNext()) {

                    VerificaRequisicao aRequest = requestIterator.next();

                    if( ! aRequest.enviouFailed ) {
                        Mensagem.send(Mensagem.Type.FAILED, aRequest.getProcessId());
                    }

                }

                // Envia Somente se nao mandou ainda
                if( novaRequisicao.compareTo(this.permissao) == -1 ) {

                    if( ! this.permissao.enviouInquire ) {
                        Mensagem.send(Mensagem.Type.INQUIRE, this.permissao.getProcessId());
                    }

                }

            } else {
            	
                Mensagem.send(Mensagem.Type.FAILED, novaRequisicao.getProcessId());
                novaRequisicao.enviouFailed = true;

            }

            // Adiciona requisicao na fila
            this.filaDeRequisicao.add(novaRequisicao);

        }

        else if ( mensagemRecebida.getConteudo(Mensagem.Type.YIELD) ) {

            // Apenas o YIELD do processo atual
            if( mensagemRecebida.getRemetenteID() != this.permissao.getProcessId() ) {
                return;
            }

            // armazenar a Mensagem de YIELD na fila
            this.filaDeRequisicao.add(this.permissao);

            // enviar GRANT para o primeiro da fila
            this.permissao = this.filaDeRequisicao.poll();

            if( this.permissao != null ) {
                Mensagem.send(Mensagem.Type.GRANT, this.permissao.getProcessId());
            }

        }

        else if ( mensagemRecebida.getConteudo(Mensagem.Type.INQUIRE) ) {

            // Se ja esta na sessao critica entao ignorar
            if( this.entrouNaSessaoCritica.get() == true ) {
                return;
            }

            
            try { this.mensagens.put(mensagemRecebida); } catch (InterruptedException e) {}

        }

        else if ( mensagemRecebida.getConteudo(Mensagem.Type.RELEASE) ) {

            // retirar o REQUEST da fila
            this.permissao = this.filaDeRequisicao.poll();

            // enviar GRANT
            if( this.permissao != null ) {
                Mensagem.send(Mensagem.Type.GRANT, this.permissao.getProcessId());
            }

        }

        else if ( mensagemRecebida.getConteudo(Mensagem.Type.FAILED) ) {

            
            try { this.mensagens.put(mensagemRecebida); } catch (InterruptedException e) {}

        }

        else if ( mensagemRecebida.getConteudo(Mensagem.Type.GRANT) ) {

            
            try { this.mensagens.put(mensagemRecebida); } catch (InterruptedException e) {}

        }

    }

    private boolean requestChangesHead(VerificaRequisicao requisicaoNova) {

        // verificar se a fila esta vazia
        if( this.filaDeRequisicao.size() == 0 ) {
            return true;
        }

        VerificaRequisicao currentHead = this.filaDeRequisicao.peek();

        if( currentHead.compareTo(requisicaoNova) == 1 ) {
            return true;
        }

        return false;

    }

}
