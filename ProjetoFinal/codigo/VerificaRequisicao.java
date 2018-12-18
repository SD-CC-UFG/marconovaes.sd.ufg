public class VerificaRequisicao implements Comparable<VerificaRequisicao> {

    private Mensagem mensagemDeRequisicao;

    public boolean enviouInquire = false, enviouFailed = false;

    public VerificaRequisicao(Mensagem novaMensagem) {
        this.mensagemDeRequisicao = novaMensagem;
    }

    public int getProcessId() {
        return this.mensagemDeRequisicao.getRemetenteID();
    }

    @Override
    public int compareTo(VerificaRequisicao outro) {

        int relogioLogico = this.mensagemDeRequisicao.getSourceVectorClock().getValorLogico();
        int relogioLogicoDoRemetente = outro.mensagemDeRequisicao.getSourceVectorClock().getValorLogico();

        if( relogioLogico < relogioLogicoDoRemetente ) {
            return -1;
        }

        if( relogioLogico > relogioLogicoDoRemetente ) {
            return 1;
        }

        if( this.mensagemDeRequisicao.getRemetenteID() < outro.mensagemDeRequisicao.getRemetenteID() ) {
            return -1;
        }

        if( this.mensagemDeRequisicao.getRemetenteID() > outro.mensagemDeRequisicao.getRemetenteID() ) {
            return 1;
        }

        
        return 0;

    }

}