import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Node extends Thread{

    private int id;
    private String state;
    private Set set;
    private volatile List<Message> messageQueue = Collections.synchronizedList(new LinkedList<Message>());
    private volatile List<Message> requireQueue = Collections.synchronizedList(new LinkedList<Message>());
    private volatile boolean voted;
    private volatile Integer votesCount;
    private volatile boolean permitted;
    public static volatile Long lastChange;
    public volatile Integer lastVote;

    public Node(int id) {
        this.id = id;
        this.voted = false;
        this.votesCount = 0;
        this.permitted = false;
        state = "Init";
        Listener listener = new Listener();
        listener.start();
        lastChange = System.currentTimeMillis();
    }

    public void setLastVote(Integer lastVote) {
        this.lastVote = lastVote;
    }

    public void decreaseVotesCount(){
        votesCount--;
    }

    public Integer getLastVote() {
        return lastVote;
    }

    public Set getSet() {
        return set;
    }

    public int getIdProcess(){
        return id;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void queueMessage(Message message){
        messageQueue.add(message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (state.equals("request")) {
                    for (Integer id : set) {
                        Node node = Mutex.nodes.get(id);
                        Message message = new Message(this.id, id, "require");
                        node.queueMessage(message);
                    }
                    while (!permitted) {
                    	//Estado de HELD
                    }
                    state = "held";
                    lastChange = System.currentTimeMillis();
                    System.out.println(id + " held");
                    sleep(Mutex.CS_INT);
                    votesCount = 0;
                    permitted = false;
                    state = "release";
                    lastChange = System.currentTimeMillis();
                    System.out.println(id + " release");
                    for (Integer id : set) {
                        Node node = Mutex.nodes.get(id);
                        Message message = new Message(this.id, id, "release");
                        node.queueMessage(message);
                    }
                    sleep(Mutex.NEXT_REQ);
                    state = "request";
                    lastChange = System.currentTimeMillis();
                }
            }
        } catch (Exception e){

        }
    }

    private class Listener extends Thread{

        @Override
        public void run() {
            while(true) {
                if (messageQueue.size() != 0) {
                    Message message = messageQueue.remove(0);
                    if(message.getContent().equals("require") && !voted){
                        Node node = Mutex.nodes.get(message.getFrom());
                        Message messageOut = new Message(id, message.getFrom(), "yes");
                        node.queueMessage(messageOut);
                        System.out.println(messageOut);
                        lastVote = message.getFrom();
                        voted = true;
                    }
                    else if(message.getContent().equals("require") && voted){
                        requireQueue.add(message);
                    }
                    else if(message.getContent().equals("yes")){
                        votesCount++;
                        if(votesCount == 5){
                            permitted = true;
                        }
                        System.out.println("id=" + id + " : " + votesCount);
                    } else if(message.getContent().equals("no")){
                        votesCount--;
                    }
                    else if(message.getContent().equals("release")){
                        if(requireQueue.size() != 0){
                            Message message1 = requireQueue.remove(0);
                            Node node = Mutex.nodes.get(message1.getFrom());
                            Message messageOut = new Message(id, message1.getFrom(), "yes");
                            node.queueMessage(messageOut);
                            System.out.println(messageOut);
                        } else{
                            voted = false;
                        }
                    }
                }
            }
        }
    }
}
