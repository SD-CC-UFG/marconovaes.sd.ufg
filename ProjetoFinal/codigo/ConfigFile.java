import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigFile {

    public static ArrayList<String[]> separadorDeDadosDeEntrada(String configFilePath) throws IOException {

        BufferedReader leitor = new BufferedReader(new FileReader(configFilePath));
        ArrayList<String[]> linhasFormatadas = new ArrayList<String[]>();

        while(leitor.ready()) {

            String linha = leitor.readLine().trim();

            if ( linha.length() == 0 || linha.charAt(0) == '#' ) {
                continue;
            }

            linhasFormatadas.add(linha.split("\\s+"));

        }

        leitor.close();

        return linhasFormatadas;

    }

    public static Node configurarNode(int nodeId, ArrayList<String[]> dadosSeparados) {

        // Setar ID do Node
        Node.nodeId = nodeId;

        // Ajustar Delay
        Delay delayGerado = new Delay(Integer.parseInt(dadosSeparados.get(0)[1]), Integer.parseInt(dadosSeparados.get(0)[2]));

        // Setar configuracoes de rede
        String[] enderecoConfigurado = dadosSeparados.get(1 + nodeId);
        InetSocketAddress socketNode = new InetSocketAddress(enderecoConfigurado[1], Integer.parseInt(enderecoConfigurado[2]));

        // Criar o objeto Node
        int totalNodes = Integer.parseInt(dadosSeparados.get(0)[0]);
        Node node = new Node(totalNodes, Integer.parseInt(dadosSeparados.get(0)[3]), delayGerado, socketNode);

        Set<String> subconjuntoIDs = new HashSet<String>(Arrays.asList(dadosSeparados.get(1 + totalNodes + nodeId)));

        for( String[] nodeAddress : dadosSeparados.subList(1, 1 + totalNodes) ) {

            Integer id = new Integer(nodeAddress[0]);
            InetSocketAddress enderecoDeRede = new InetSocketAddress(nodeAddress[1], Integer.parseInt(nodeAddress[2]));

            Node.enderecos.put(id, enderecoDeRede);

            if( subconjuntoIDs.contains(nodeAddress[0]) ) {
                Node.subconjunto.put(id, enderecoDeRede);
            }

        }

        return node;

    }

}
