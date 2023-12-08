package org.example.Producer.AuctionPFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.CfgConsumerGraphic;
import org.example.Consumer.SendTaskDto;
import org.example.Producer.CfgProduceGraphic;
import org.example.Producer.ProducerAnswerDto;
import org.example.ReadProducerConfigInterface;
import org.example.TopicHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Random;

@Slf4j
public class WaitForProposeBeh extends Behaviour implements ReadProducerConfigInterface {
    private String topicName;
    private double ens =0;
    private AID topic;
    private final Gson gson = new Gson();
    private double tecStartPrice;
    private double vesStartPrice;
    private double secStartPrice;

    private CfgProduceGraphic cfgProduceGraphicTEC;
    private CfgProduceGraphic cfgProduceGraphicSEC;
    private CfgProduceGraphic cfgProduceGraphicVES;

    private boolean tecAgreeWithtask;

    private boolean secAgreeWithtask;
    private boolean vesAgreeWithtask;


    public WaitForProposeBeh(String topicName){
        this.topicName = topicName;

    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);

    }

    @Override
    public void action() {
        ACLMessage proposeMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        if(proposeMsg!=null) {
            SendTaskDto fromDistributer = gson.fromJson(proposeMsg.getContent(), SendTaskDto.class);
            ACLMessage myStartInAuction = new ACLMessage(ACLMessage.INFORM);
            cfgProduceGraphicTEC = readConfigProducer("AgentTECProducer");
            if (cfgProduceGraphicTEC!=null) {
                    tecStartPrice = cfgProduceGraphicTEC.getPrice().get(0);
            }

            cfgProduceGraphicSEC = readConfigProducer("AgentSECProducer");
            if (cfgProduceGraphicSEC!=null) {
                    secStartPrice = cfgProduceGraphicSEC.getPrice().get(0);
            }

            cfgProduceGraphicVES = readConfigProducer("AgentVESProducer");
            if (cfgProduceGraphicVES!=null) {
                    vesStartPrice = cfgProduceGraphicVES.getPrice().get(0);
            }

            if(cfgProduceGraphicSEC !=null && cfgProduceGraphicVES !=null  && cfgProduceGraphicTEC !=null  ) {
                isDebatePosibleForProduser(fromDistributer);
            }
            if(myAgent.getLocalName().equals("AgentTECProducer") && tecAgreeWithtask) {
                myStartInAuction.addReceiver(topic);
                myStartInAuction.setContent(proposeMsg.getContent() + " " +cfgProduceGraphicTEC.getPrice().get(0) + " " + cfgProduceGraphicTEC.getPrice().get(0)*2);
                getAgent().send(myStartInAuction);
            }
            if(myAgent.getLocalName().equals("AgentSECProducer") && secAgreeWithtask) {
                myStartInAuction.addReceiver(topic);
                myStartInAuction.setContent(proposeMsg.getContent() + " " + cfgProduceGraphicSEC.getPrice().get(0) + " " +cfgProduceGraphicSEC.getPrice().get(0)*2);
                getAgent().send(myStartInAuction);
            }
            if(myAgent.getLocalName().equals("AgentVESProducer") && vesAgreeWithtask) {
                myStartInAuction.addReceiver(topic);
                myStartInAuction.setContent(proposeMsg.getContent() + " " + cfgProduceGraphicVES.getPrice().get(0) + " " +cfgProduceGraphicVES.getPrice().get(0)*2);
                getAgent().send(myStartInAuction);
            }

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
    @Override
    public CfgProduceGraphic readConfigProducer(String agentLocalName) {
        CfgProduceGraphic cfgProducer = null;
        {
            try {
                JAXBContext context =
                        JAXBContext.newInstance(CfgProduceGraphic.class);
                Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
                cfgProducer = (CfgProduceGraphic) jaxbUnmarshaller.unmarshal(new
                        File("src/main/resources/" + agentLocalName.split("Agent")[1] + "Graphic.xml"));

            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
        return cfgProducer;
    }


    private void isDebatePosibleForProduser(SendTaskDto fromDistributer) {
        if( (cfgProduceGraphicSEC.getPower().get(0) >= fromDistributer.getRequiredPowerPerHour()) && (cfgProduceGraphicSEC.getPrice().get(0) <= fromDistributer.getMyMaxPrice()) ) {
            secAgreeWithtask = true;
            secStartPrice = cfgProduceGraphicSEC.getPrice().get(0)*2;
        } else {
            secAgreeWithtask = false;
            secStartPrice = 0;
        }
        if( (cfgProduceGraphicTEC.getPower().get(0) >= fromDistributer.getRequiredPowerPerHour()) && (cfgProduceGraphicTEC.getPrice().get(0) <= fromDistributer.getMyMaxPrice()) ) {
            tecAgreeWithtask = true;
            tecStartPrice = cfgProduceGraphicTEC.getPrice().get(0)*2;
        } else {
            tecAgreeWithtask = false;
            tecStartPrice = 0;
        }
        if( (cfgProduceGraphicVES.getPower().get(0) >= fromDistributer.getRequiredPowerPerHour()) && (cfgProduceGraphicVES.getPrice().get(0) <= fromDistributer.getMyMaxPrice()) ) {
            vesAgreeWithtask = true;
            vesStartPrice = cfgProduceGraphicVES.getPrice().get(0)*2;
        }
        else {
            vesAgreeWithtask = false;
            vesStartPrice = 0;
        }
    }

    private void debate(SendTaskDto sendTaskDto) {
        double taskPrice = sendTaskDto.getMyMaxPrice();
        log.info(String.valueOf("tec" +tecStartPrice));
        log.info(String.valueOf("sec"+ secStartPrice));
        log.info(String.valueOf("ves"+vesStartPrice));
    }

//    if( (tecStartPrice < secStartPrice) && tecAgreeWithtask && secAgreeWithtask) {
//        tecStartPrice = secStartPrice*0.9;
//    }
//
//        if((secStartPrice < tecStartPrice) && tecAgreeWithtask && secAgreeWithtask) {
//        secStartPrice = tecStartPrice*0.9;
//    }
//        if((secStartPrice < vesStartPrice)  && vesAgreeWithtask && secAgreeWithtask) {
//        secStartPrice = vesStartPrice*0.9;
//    }
//        if((vesStartPrice < secStartPrice) && vesAgreeWithtask && secAgreeWithtask) {
//        vesStartPrice = secStartPrice*0.9;
//    }
//        if((tecStartPrice < secStartPrice) && tecAgreeWithtask && secAgreeWithtask) {
//        tecStartPrice = secStartPrice*0.9;
//    }
//
//        if((vesStartPrice < tecStartPrice) && tecAgreeWithtask && vesAgreeWithtask) {
//        vesStartPrice = tecStartPrice*0.9;
//    }
//        if((tecStartPrice < vesStartPrice) && tecAgreeWithtask && vesAgreeWithtask) {
//        tecStartPrice = vesStartPrice*0.9;
//    }





}
