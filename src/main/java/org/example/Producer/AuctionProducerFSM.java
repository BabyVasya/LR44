package org.example.Producer;

import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import org.example.Producer.AuctionPFSMSubbeh.AuctionDebate;
import org.example.Producer.AuctionPFSMSubbeh.WaitForProposeBeh;

public class AuctionProducerFSM extends FSMBehaviour {
//    private CfgProduceGraphic produceGraphic;
      private String topicName;
    public AuctionProducerFSM(String topicName){
        this.topicName = topicName;
    }
    @Override
    public void onStart() {
        this.registerFirstState(new WaitForProposeBeh(topicName), "Get propose");
        this.registerLastState(new AuctionDebate(topicName), "Debating by get best price");



        this.registerDefaultTransition("Get propose", "Debating by get best price");
    }
}
