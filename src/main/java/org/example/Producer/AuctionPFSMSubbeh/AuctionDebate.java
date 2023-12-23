package org.example.Producer.AuctionPFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.SendTaskDto;
import org.example.TopicHelper;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;



@Slf4j
public class AuctionDebate extends Behaviour {
    private String topicName;
    private AID topic;
    private boolean end;
    public static boolean flagOfFirstIter;

    public static double tecPrice;
    public static double vesPrice;
    public static double secPrice;

    private int tryCounterTec;
    private int tryCounterVes;

    private double tecMinPrice;
    private double vesMinPrice;
    private boolean vesOtkat;
    private boolean tecOtkat;
    public static ACLMessage currentMsgTec;
    public static ACLMessage currentMsgVes;
    public static ACLMessage currentMsgSec;
    private ACLMessage myPropose;
    public static Map<AID, Double> agentsPaticipant = new HashMap<>();


//    private ACLMessage proposesToTopic;

    public AuctionDebate(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);
        ACLMessage priceBet = new ACLMessage(ACLMessage.INFORM);
        priceBet.addReceiver(topic);
//        log.info("Debate " + agentsPaticipant);
        if (agentsPaticipant.containsKey(myAgent.getAID())) {
            if (myAgent.getLocalName().equals("AgentTECProducer")) {
                tecPrice = agentsPaticipant.get(myAgent.getAID()) * 2;
                priceBet.setContent(String.valueOf(agentsPaticipant.get(myAgent.getAID()) * 2));
                getAgent().send(priceBet);
            }
            if (myAgent.getLocalName().equals("AgentSECProducer")) {
                secPrice = agentsPaticipant.get(myAgent.getAID()) * 2;
                priceBet.setContent(String.valueOf(agentsPaticipant.get(myAgent.getAID()) * 2));
                getAgent().send(priceBet);
            }
            if (myAgent.getLocalName().equals("AgentVESProducer")) {
                vesPrice = agentsPaticipant.get(myAgent.getAID()) * 2;
                priceBet.setContent(String.valueOf(agentsPaticipant.get(myAgent.getAID()) * 2));
                getAgent().send(priceBet);
            }
        }

//        log.info("On start price " + " ves " + vesPrice+ " sec " + secPrice+ " tec " + tecPrice);
        done();
    }

    @Override
    public void action() {
        debate();
    }

    public void debate() {
        if (agentsPaticipant.containsKey(myAgent.getAID())) {
            ACLMessage price = getAgent().receive(MessageTemplate.and(MessageTemplate.MatchTopic(topic), MessageTemplate.MatchPerformative(ACLMessage.INFORM)));
            if (price !=null) {
                if (agentsPaticipant.size() == 1) {
                    ACLMessage myPropose = new ACLMessage(ACLMessage.INFORM);
                    if (WaitForProposeBeh.clientMaxPrice <= Double.parseDouble(price.getContent())) {
                        if(WaitForProposeBeh.clientMaxPrice >= Double.parseDouble(price.getContent())/2) {
                            double tmp = WaitForProposeBeh.clientMaxPrice;
                            if (myAgent.getLocalName().equals("AgentVESProducer")) {
                                myPropose.setContent(String.valueOf(tmp));
                                currentMsgVes = (ACLMessage) myPropose.clone();
                            }
                            if (myAgent.getLocalName().equals("AgentTECProducer")) {
                                myPropose.setContent(String.valueOf(tmp));
                                currentMsgTec = (ACLMessage) myPropose.clone();
                            }
                            if (myAgent.getLocalName().equals("AgentSecProducer")) {
                                myPropose.setContent(String.valueOf(tmp));
                                currentMsgSec = (ACLMessage) myPropose.clone();
                            }
                        }
                    }
                }
                if ( agentsPaticipant.size() > 1) {
                    ACLMessage myPropose = new ACLMessage(ACLMessage.INFORM);
                    myPropose.addReceiver(topic);
                    if(!myAgent.getLocalName().equals(price.getSender().getLocalName()) ) {
                        if(myAgent.getLocalName().equals("AgentTECProducer")) {
                            if(WaitForProposeBeh.clientMaxPrice < Double.valueOf(price.getContent())) {
                                double tmp = WaitForProposeBeh.clientMaxPrice;
                                if(WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    tecPrice = tmp;
                                }
                            }
                            if(vesPrice <= tecPrice) {
                                if (WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    double tmp = tecPrice;
                                    tecPrice = 0.95*vesPrice;
//                                log.info("Тэц конкурирую с вэс " + tecPrice + vesPrice);
                                    if (tecPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                        tecPrice = tmp;
                                    }
                                } else {
//                                log.info("ТЭЦ выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + tecPrice);
                                }

                            }
                            if(secPrice <= tecPrice) {
                                if (WaitForProposeBeh.clientMaxPrice>= Double.valueOf(price.getContent())) {
                                    double tmp = tecPrice;
                                    tecPrice = 0.95*secPrice;
//                                log.info("Тэц конкурирую с сэц " + tecPrice + vesPrice);
                                    if (tecPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                        tecPrice = tmp;
                                    }
                                } else {
//                                log.info("ТЭЦ выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + tecPrice);
                                }

                            }
                            if(vesPrice > tecPrice || secPrice > tecPrice) {
                            }
                            myPropose.setContent(String.valueOf(tecPrice));
                            currentMsgTec = (ACLMessage) myPropose.clone();
//                        log.info("Отсылка своей ставки " + myPropose);
                            getAgent().send(myPropose);
                        }

                        if(myAgent.getLocalName().equals("AgentSECProducer")) {
                            if(WaitForProposeBeh.clientMaxPrice < Double.valueOf(price.getContent())) {
                                double tmp = WaitForProposeBeh.clientMaxPrice;
                                if(WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    secPrice = tmp;
                                }
                            }
                            if(tecPrice <= secPrice) {
                                if (WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    double tmp = secPrice;
                                    secPrice = 0.95*tecPrice;
                                    if (secPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                        secPrice = tmp;
                                    }
                                } else {
//                                log.info("СЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + secPrice);
                                }

                            }
                            if(vesPrice <= secPrice) {
                                if (WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    double tmp = secPrice;
                                    secPrice = 0.95*vesPrice;
                                    if (secPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                        secPrice = tmp;
                                    }
                                } else {
//                                log.info("ВЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + secPrice);
                                }

                            }
                            if(tecPrice > secPrice || vesPrice > secPrice) {
                            }
                            myPropose.setContent(String.valueOf(secPrice));
                            currentMsgSec = (ACLMessage) myPropose.clone();
//                        log.info("Отсылка своей ставки " + myPropose);
                            getAgent().send(myPropose);
                        }

                        if(myAgent.getLocalName().equals("AgentVESProducer")) {
                            if(WaitForProposeBeh.clientMaxPrice< Double.valueOf(price.getContent())) {
                                double tmp = WaitForProposeBeh.clientMaxPrice;
                                if(WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    vesPrice = tmp;
                                }
                            }
                            if(tecPrice <= vesPrice) {
                                if (WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    double tmp = vesPrice;
                                    vesPrice = 0.95*tecPrice;
                                    if (vesPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                        vesPrice = tmp;
                                    }
                                } else {
//                                log.info("ВЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + vesPrice);
                                }

                            }
                            if(secPrice <= vesPrice) {
                                if (WaitForProposeBeh.clientMaxPrice >= agentsPaticipant.get(myAgent.getAID())) {
                                    double tmp = vesPrice;
                                    vesPrice = 0.95*secPrice;
                                    if (vesPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                        vesPrice = tmp;
                                    }
                                } else {
//                                log.info("ВЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + vesPrice);
                                }

                            }
                            if(tecPrice > vesPrice || secPrice > vesPrice) {
                            }
                            myPropose.setContent(String.valueOf(vesPrice));
                            currentMsgVes = (ACLMessage) myPropose.clone();
//                        log.info("Отсылка своей ставки " + myPropose);
                            getAgent().send(myPropose);
                        }
                    }
                    flagOfFirstIter = true;
                }

            }

        }
    }
    @Override
    public boolean done() {
        return DebateTimeout.ending;
    }

    @Override
    public int onEnd() {
        return 0;
    }

    private void oneParticipantCase(ACLMessage myPropose, String agentLocalName, double clientMaxPrice) {
        double contentPrice = Double.parseDouble(myPropose.getContent());
        if (clientMaxPrice <= contentPrice && clientMaxPrice >= contentPrice / 2) {
            myPropose.setContent(String.valueOf(clientMaxPrice));

            if (agentLocalName.equals("AgentVESProducer")) {
                currentMsgVes = (ACLMessage) myPropose.clone();
            } else if (agentLocalName.equals("AgentTECProducer")) {
                currentMsgTec = (ACLMessage) myPropose.clone();
            } else if (agentLocalName.equals("AgentSecProducer")) {
                currentMsgSec = (ACLMessage) myPropose.clone();
            }
        }
    }

}


