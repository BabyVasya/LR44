package org.example.Producer.AuctionPFSMSubbeh;

import jade.core.behaviours.ParallelBehaviour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitForProposeParallel extends ParallelBehaviour {
    private String topicName;

    private WaitForProposeBeh waitForProposeBeh;

    public WaitForProposeParallel(String topicName) {
        super(WHEN_ANY);
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        waitForProposeBeh = new WaitForProposeBeh(topicName);
        this.addSubBehaviour(waitForProposeBeh);
        WaitForProposeTimeout waitForProposeTimeout = new WaitForProposeTimeout(myAgent, 1000);
        this.addSubBehaviour(waitForProposeTimeout);
    }

    @Override
    public int onEnd() {
        return super.onEnd();
    }
}
