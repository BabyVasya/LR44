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
import org.example.VirtualTime;

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
    private ACLMessage proposeMsg;


    public WaitForProposeBeh(String topicName, ACLMessage msg){
        this.topicName = topicName;
        this.proposeMsg = msg;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);

    }

    @Override
    public void action() {
//        ACLMessage proposeMsg = getAgent().receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
        if(proposeMsg!=null) {
            SendTaskDto fromDistributer = gson.fromJson(proposeMsg.getContent(), SendTaskDto.class);
            ACLMessage myStartInAuction = new ACLMessage(ACLMessage.INFORM);
            cfgProduceGraphicTEC = readConfigProducer("AgentTECProducer");
            if (cfgProduceGraphicTEC!=null) {
                    tecStartPrice = cfgProduceGraphicTEC.getPrice().get(cfgProduceGraphicTEC.getTime().indexOf(VirtualTime.currentHour));
            }

            cfgProduceGraphicSEC = readConfigProducer("AgentSECProducer");
            if (cfgProduceGraphicSEC!=null) {
                    secStartPrice = cfgProduceGraphicSEC.getPrice().get(cfgProduceGraphicSEC.getTime().indexOf(VirtualTime.currentHour));
            }

            cfgProduceGraphicVES = readConfigProducer("AgentVESProducer");
            if (cfgProduceGraphicVES!=null) {
                    vesStartPrice = cfgProduceGraphicVES.getPrice().get(cfgProduceGraphicVES.getTime().indexOf(VirtualTime.currentHour));
            }

            if(cfgProduceGraphicSEC !=null && cfgProduceGraphicVES !=null  && cfgProduceGraphicTEC !=null  ) {
                isDebatePosibleForProduser(fromDistributer);
            }
            if(myAgent.getLocalName().equals("AgentTECProducer") && tecAgreeWithtask) {
                myStartInAuction.addReceiver(topic);
                myStartInAuction.setContent(proposeMsg.getContent() + " " +cfgProduceGraphicTEC.getPrice().get(cfgProduceGraphicTEC.getTime().indexOf(VirtualTime.currentHour)) + " " + cfgProduceGraphicTEC.getPrice().get(cfgProduceGraphicTEC.getTime().indexOf(VirtualTime.currentHour))*2);
                getAgent().send(myStartInAuction);
                proposeMsg = null;
            }
            if(myAgent.getLocalName().equals("AgentSECProducer") && secAgreeWithtask) {
                myStartInAuction.addReceiver(topic);
                myStartInAuction.setContent(proposeMsg.getContent() + " " + cfgProduceGraphicSEC.getPrice().get(cfgProduceGraphicSEC.getTime().indexOf(VirtualTime.currentHour)) + " " +cfgProduceGraphicSEC.getPrice().get(cfgProduceGraphicSEC.getTime().indexOf(VirtualTime.currentHour))*2);
                getAgent().send(myStartInAuction);
                proposeMsg = null;
            }
            if(myAgent.getLocalName().equals("AgentVESProducer") && vesAgreeWithtask) {
                myStartInAuction.addReceiver(topic);
                myStartInAuction.setContent(proposeMsg.getContent() + " " + cfgProduceGraphicVES.getPrice().get(cfgProduceGraphicVES.getTime().indexOf(VirtualTime.currentHour)) + " " +cfgProduceGraphicVES.getPrice().get(cfgProduceGraphicVES.getTime().indexOf(VirtualTime.currentHour))*2);
                getAgent().send(myStartInAuction);
                proposeMsg = null;
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
        if( (cfgProduceGraphicSEC.getPower().get(cfgProduceGraphicSEC.getTime().indexOf(VirtualTime.currentHour)) >= fromDistributer.getRequiredPowerPerHour()) && (cfgProduceGraphicSEC.getPrice().get(cfgProduceGraphicSEC.getTime().indexOf(VirtualTime.currentHour)) <= fromDistributer.getMyMaxPrice()) ) {
            secAgreeWithtask = true;
            secStartPrice = cfgProduceGraphicSEC.getPrice().get(cfgProduceGraphicSEC.getTime().indexOf(VirtualTime.currentHour))*2;
        } else {
            secAgreeWithtask = false;
            secStartPrice = 0;
        }
        if( (cfgProduceGraphicTEC.getPower().get(cfgProduceGraphicTEC.getTime().indexOf(VirtualTime.currentHour)) >= fromDistributer.getRequiredPowerPerHour()) && (cfgProduceGraphicTEC.getPrice().get(cfgProduceGraphicTEC.getTime().indexOf(VirtualTime.currentHour)) <= fromDistributer.getMyMaxPrice()) ) {
            tecAgreeWithtask = true;
            tecStartPrice = cfgProduceGraphicTEC.getPrice().get(cfgProduceGraphicTEC.getTime().indexOf(VirtualTime.currentHour))*2;
        } else {
            tecAgreeWithtask = false;
            tecStartPrice = 0;
        }
        if( (cfgProduceGraphicVES.getPower().get(cfgProduceGraphicVES.getTime().indexOf(VirtualTime.currentHour)) >= fromDistributer.getRequiredPowerPerHour()) && (cfgProduceGraphicVES.getPrice().get(cfgProduceGraphicVES.getTime().indexOf(VirtualTime.currentHour)) <= fromDistributer.getMyMaxPrice()) ) {
            vesAgreeWithtask = true;
            vesStartPrice = cfgProduceGraphicVES.getPrice().get(cfgProduceGraphicVES.getTime().indexOf(VirtualTime.currentHour))*2;
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


}
