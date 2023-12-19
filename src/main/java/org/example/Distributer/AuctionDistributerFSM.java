package org.example.Distributer;

import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.Distributer.AuctionDFSMSubbeh.MakingDesicion;
import org.example.Distributer.AuctionDFSMSubbeh.MakingDesicionParall;
import org.example.Distributer.AuctionDFSMSubbeh.ReceiveTaskAndStartAuction;

@Slf4j
public class AuctionDistributerFSM extends FSMBehaviour {
    private ACLMessage task;

    public AuctionDistributerFSM(ACLMessage task) {
        this.task = task;
    }

    @Override
    public void onStart() {
        this.registerFirstState(new ReceiveTaskAndStartAuction("Auction", task), "GET1");
        this.registerLastState(new MakingDesicionParall("Auction"), "GET2");

        this.registerDefaultTransition("GET1", "GET2");
    }

}
