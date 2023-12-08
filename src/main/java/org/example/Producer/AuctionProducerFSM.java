package org.example.Producer;


import jade.core.behaviours.FSMBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Producer.AuctionPFSMSubbeh.AuctionDebate;
import org.example.Producer.AuctionPFSMSubbeh.DebateParallel;
import org.example.Producer.AuctionPFSMSubbeh.WaitForProposeBeh;
import org.example.Producer.AuctionPFSMSubbeh.WaitForProposeParallel;

@Slf4j
public class AuctionProducerFSM extends FSMBehaviour {
//    private CfgProduceGraphic produceGraphic;
      private String topicName;
    public AuctionProducerFSM(String topicName){
        this.topicName = topicName;
    }
    @Override
    public void onStart() {
        this.registerFirstState(new WaitForProposeParallel(topicName), "GET PROPOSE AND GIVE PRICE");
//        this.registerLastState(new WaitForProposeBeh(topicName), "GET PROPOSE AND GIVE PRICE");
        this.registerLastState(new DebateParallel(topicName), "GET PROPOSE AND GIVE PRICE1");
//        this.registerLastState(new AuctionDebate(topicName), "Debating by get best price");



        this.registerDefaultTransition("GET PROPOSE AND GIVE PRICE", "GET PROPOSE AND GIVE PRICE1");
    }
}
