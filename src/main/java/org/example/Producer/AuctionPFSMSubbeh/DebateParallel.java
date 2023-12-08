package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.TopicHelper;

@Slf4j
public class DebateParallel extends ParallelBehaviour {
    private String topicName;


    public DebateParallel(String topicName) {
        super(WHEN_ANY);
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        AuctionDebate  auctionDebate = new AuctionDebate(topicName);
        this.addSubBehaviour(auctionDebate);
        DebateTimeout debateTimeout = new DebateTimeout(myAgent, 100);
        this.addSubBehaviour(debateTimeout);
    }

    @Override
    public int onEnd() {
        ACLMessage prices = getAgent().receive(MessageTemplate.MatchTopic(TopicHelper.register(myAgent, topicName)));
        if (prices!=null) {
            log.info("End " + prices);
        }
        return super.onEnd();
    }
}
