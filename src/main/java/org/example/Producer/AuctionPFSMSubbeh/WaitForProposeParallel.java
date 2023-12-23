package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import static org.example.Producer.AuctionPFSMSubbeh.AuctionDebate.agentsPaticipant;

@Slf4j
public class WaitForProposeParallel extends ParallelBehaviour {
    private String topicName;

    private WaitForProposeBeh waitForProposeBeh;
    public static ACLMessage msg;

    public WaitForProposeParallel(String topicName, ACLMessage msg) {
        super(WHEN_ANY);
        this.topicName = topicName;
        this.msg = msg;
    }

    @Override
    public void onStart() {
        waitForProposeBeh = new WaitForProposeBeh(topicName, msg);
        this.addSubBehaviour(waitForProposeBeh);
        WaitForProposeTimeout waitForProposeTimeout = new WaitForProposeTimeout(myAgent, 50);
        this.addSubBehaviour(waitForProposeTimeout);
    }

    @Override
    public int onEnd() {
//        log.info("Timeout " + agentsPaticipant);
        return super.onEnd();
    }
}
