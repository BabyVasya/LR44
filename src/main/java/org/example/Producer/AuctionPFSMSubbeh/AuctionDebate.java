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

    public static Map<AID, Double> agentsPaticipant;
//    private ACLMessage proposesToTopic;

    public AuctionDebate(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);
//        proposesToTopic = null;
        done();
    }

    @Override
    public void action() {
        ACLMessage proposesToTopic = getAgent().receive(MessageTemplate.and(MessageTemplate.MatchTopic(topic), MessageTemplate.MatchPerformative(ACLMessage.INFORM)));
        if(proposesToTopic!=null) {
            Gson gson = new Gson();
            log.info("Пришло " + proposesToTopic );
            if(proposesToTopic.getContent().split(" ").length > 3) {
                String[] newArray = Arrays.copyOf(proposesToTopic.getContent().split(" "), proposesToTopic.getContent().split(" ").length - 1);
                proposesToTopic.setContent(String.join(" ", newArray));
            }
            log.info("Пришло ред " + proposesToTopic );
            SendTaskDto sendTaskDto = gson.fromJson(proposesToTopic.getContent().split(" ")[0], SendTaskDto.class);
            if (agentsPaticipant == null) {
                agentsPaticipant = new HashMap<>();
            }
            if (!agentsPaticipant.containsKey(proposesToTopic.getSender())) {
                agentsPaticipant.put(proposesToTopic.getSender(), Double.valueOf(proposesToTopic.getContent().split(" ")[1]));
            }

            if (agentsPaticipant.containsKey(myAgent.getAID())) {
                ACLMessage myPropose = new ACLMessage(ACLMessage.INFORM);
                myPropose.addReceiver(topic);

                if(!myAgent.getLocalName().equals(proposesToTopic.getSender().getLocalName()) ) {
                    if(myAgent.getLocalName().equals("AgentTECProducer")) {
                        if(proposesToTopic.getSender().equals("AgentVESProducer")) {
                            vesPrice = Double.parseDouble(proposesToTopic.getContent().split(" ")[2]);
                            log.info("Смотрим цены  " + vesPrice + " ves" + secPrice + " sec");
                        }
                        if(proposesToTopic.getSender().equals("AgentSECProducer")) {
                            secPrice = Double.parseDouble(proposesToTopic.getContent().split(" ")[2]);
                        }
                        if(sendTaskDto.getMyMaxPrice() < Double.valueOf(proposesToTopic.getContent().split(" ")[2])) {
                            if(sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                tecPrice = sendTaskDto.getMyMaxPrice();
                            }
                        }
                        if(vesPrice <= tecPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = tecPrice;
                                tecPrice = 0.95*vesPrice;
                                if (tecPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    tecPrice = tmp;
                                }
                            } else {
                                log.info("ТЭЦ выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + tecPrice);
                            }

                        }
                        if(secPrice <= tecPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = tecPrice;
                                tecPrice = 0.95*secPrice;
                                if (tecPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    tecPrice = tmp;
                                }
                            } else {
                                log.info("ТЭЦ выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + tecPrice);
                            }

                        }
                        if(vesPrice > tecPrice || secPrice > tecPrice) {
                        }
                        List<String> priceListTEC = new ArrayList<>(List.of(proposesToTopic.getContent().split(" ")));
                        priceListTEC.remove(2);
                        priceListTEC.remove(1);
                        priceListTEC.add(String.valueOf(agentsPaticipant.get(myAgent.getAID())));
                        priceListTEC.add(String.valueOf(tecPrice));
                        myPropose.setContent(priceListTEC.stream().collect(Collectors.joining(" ")));
                        currentMsgTec = (ACLMessage) myPropose.clone();
                        log.info("Отсылка своей ставки " + myPropose);
                        getAgent().send(myPropose);
                    }

                    if(myAgent.getLocalName().equals("AgentSECProducer")) {
                        if(proposesToTopic.getSender().equals("AgentTECProducer")) {
                            tecPrice = Double.parseDouble(proposesToTopic.getContent().split(" ")[2]);
                        }
                        if(proposesToTopic.getSender().equals("AgentVESProducer")) {
                            vesPrice = Double.parseDouble(proposesToTopic.getContent().split(" ")[2]);
                        }
                        if(sendTaskDto.getMyMaxPrice() < Double.valueOf(proposesToTopic.getContent().split(" ")[2])) {
                            if(sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                secPrice = sendTaskDto.getMyMaxPrice();
                            }
                        }
                        if(tecPrice <= secPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.parseDouble(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = secPrice;
                                secPrice = 0.95*tecPrice;
                                if (secPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    secPrice = tmp;
                                }
                            } else {
                                log.info("СЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + secPrice);
                            }

                        }
                        if(vesPrice <= secPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.parseDouble(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = secPrice;
                                secPrice = 0.95*vesPrice;
                                if (secPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    secPrice = tmp;
                                }
                            } else {
                                log.info("ВЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + secPrice);
                            }

                        }
                        if(tecPrice > secPrice || vesPrice > secPrice) {
                        }
                        List<String> priceListSEC = new ArrayList<>(List.of(proposesToTopic.getContent().split(" ")));
                        priceListSEC.remove(2);
                        priceListSEC.remove(1);
                        priceListSEC.add(String.valueOf(agentsPaticipant.get(myAgent.getAID())));
                        priceListSEC.add(String.valueOf(secPrice));
                        myPropose.setContent(priceListSEC.stream().collect(Collectors.joining(" ")));
                        currentMsgSec = (ACLMessage) myPropose.clone();
                        log.info("Отсылка своей ставки " + myPropose);
                        getAgent().send(myPropose);
                    }

                    if(myAgent.getLocalName().equals("AgentVESProducer")) {
                        if(proposesToTopic.getSender().equals("AgentTECProducer")) {
                            tecPrice = Double.parseDouble(proposesToTopic.getContent().split(" ")[2]);
                        }
                        if(proposesToTopic.getSender().equals("AgentSECProducer")) {
                            secPrice = Double.parseDouble(proposesToTopic.getContent().split(" ")[2]);
                        }
                        if(sendTaskDto.getMyMaxPrice() < Double.valueOf(proposesToTopic.getContent().split(" ")[2])) {
                            if(sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                vesPrice = sendTaskDto.getMyMaxPrice();
                            }
                        }
                        if(tecPrice <= vesPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.parseDouble(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = vesPrice;
                                vesPrice = 0.95*tecPrice;
                                if (vesPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    vesPrice = tmp;
                                }
                            } else {
                                log.info("ВЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + vesPrice);
                            }

                        }
                        if(secPrice <= vesPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.parseDouble(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = vesPrice;
                                vesPrice = 0.95*secPrice;
                                if (vesPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    vesPrice = tmp;
                                }
                            } else {
                                log.info("ВЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + vesPrice);
                            }

                        }
                        if(tecPrice > vesPrice || secPrice > vesPrice) {
                        }
                        List<String> priceListVES = new ArrayList<>(List.of(proposesToTopic.getContent().split(" ")));
                        priceListVES.remove(2);
                        priceListVES.remove(1);
                        priceListVES.add(String.valueOf(agentsPaticipant.get(myAgent.getAID())));
                        priceListVES.add(String.valueOf(vesPrice));
                        myPropose.setContent(priceListVES.stream().collect(Collectors.joining(" ")));
                        currentMsgVes = (ACLMessage) myPropose.clone();
                        log.info("Отсылка своей ставки " + myPropose);
                        getAgent().send(myPropose);
                    }
                }

            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        if (DebateTimeout.ending) {
            myPropose = null;
        }
        return DebateTimeout.ending;
    }

    @Override
    public int onEnd() {

        return 0;
    }

    private void oneParticipantCase(ACLMessage proposesToTopic, SendTaskDto sendTaskDto) {
        if (agentsPaticipant.size() == 1) {
            ACLMessage toDistributer = new ACLMessage(ACLMessage.CONFIRM);
            if (sendTaskDto.getMyMaxPrice() <= Double.parseDouble(proposesToTopic.getContent().split(" ")[1])) {
                agentsPaticipant.put(myAgent.getAID(), sendTaskDto.getMyMaxPrice());
                toDistributer.setContent(String.valueOf(new ArrayList<>(agentsPaticipant.values()).get(0)));
                toDistributer.addReceiver(topic);
                log.info("No more concurents, i am sending propose " + toDistributer);
                getAgent().send(toDistributer);
            } else {
                log.info("the price is too low for me");
            }
            end = true;
        }
    }

    private void moreThenOneParticipant(ACLMessage proposesToTopic, SendTaskDto sendTaskDto, ACLMessage myPropose) {

    }
}


