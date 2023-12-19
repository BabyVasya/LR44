package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitForProposeParallel extends ParallelBehaviour {
    private String topicName;

    private WaitForProposeBeh waitForProposeBeh;
    private ACLMessage msg;

    public WaitForProposeParallel(String topicName, ACLMessage msg) {
        super(WHEN_ANY);
        this.topicName = topicName;
        this.msg = msg;
    }

    @Override
    public void onStart() {
        waitForProposeBeh = new WaitForProposeBeh(topicName, msg);
        this.addSubBehaviour(waitForProposeBeh);
        WaitForProposeTimeout waitForProposeTimeout = new WaitForProposeTimeout(myAgent, 1000);
        this.addSubBehaviour(waitForProposeTimeout);
    }

    @Override
    public int onEnd() {
        return super.onEnd();
    }
}
