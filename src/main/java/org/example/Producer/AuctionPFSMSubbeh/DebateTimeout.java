package org.example.Producer.AuctionPFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.SendTaskDto;
import org.example.TopicHelper;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class DebateTimeout extends WakerBehaviour {
    public static boolean ending;

    public DebateTimeout(Agent a, long timeout) {
        super(a, timeout);
    }

    @Override
    protected void onWake() {
        ending = true;
        AuctionDebate.flagOfFirstIter = false;
        log.info("Timeout");
    }
}
