package org.example.Distributer;

import jade.core.behaviours.FSMBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.example.Distributer.AuctionDFSMSubbeh.MakingDesicion;
import org.example.Distributer.AuctionDFSMSubbeh.ReceiveTaskAndStartAuction;

@Slf4j
public class AuctionDistributerFSM extends FSMBehaviour {
    @Override
    public void onStart() {
        this.registerFirstState(new ReceiveTaskAndStartAuction("Auction"), "GET1");
        this.registerLastState(new MakingDesicion(), "GET2");

        this.registerDefaultTransition("GET1", "GET2");
    }

}
