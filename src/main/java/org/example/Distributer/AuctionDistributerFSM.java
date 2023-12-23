package org.example.Distributer;

import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Distributer.AuctionDFSMSubbeh.*;

@Slf4j
public class AuctionDistributerFSM extends FSMBehaviour {
    private ACLMessage task;

    public AuctionDistributerFSM(ACLMessage task) {
        this.task = task;
//        log.info("Problem distribFSM???");
    }

    @Override
    public void onStart() {
        this.registerFirstState(new ReceiveTaskAndStartAuction("Auction", task), "GET1");
        this.registerState(new MakingDesicionParall("Auction"), "GET2");
        this.registerState(new NoPaticipants(task), "GET3");
        this.registerState(new MakingDesicionParall("Auction"), "GET22");
        this.registerLastState(new GoodEnd(), "GET4");

        this.registerDefaultTransition("GET1", "GET2");
        this.registerTransition("GET2" , "GET4", 1);
        this.registerTransition("GET2" , "GET3", 0);
        this.registerDefaultTransition("GET3", "GET22");
        this.registerTransition("GET22" , "GET4", 1);
    }

}
