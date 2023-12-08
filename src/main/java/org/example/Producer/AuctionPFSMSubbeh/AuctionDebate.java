package org.example.Producer.AuctionPFSMSubbeh;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.example.Consumer.CfgConsumerGraphic;
import org.example.Consumer.SendTaskDto;
import org.example.Producer.CfgProduceGraphic;
import org.example.ReadProducerConfigInterface;
import org.example.TopicHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AuctionDebate extends Behaviour  {
    private String topicName;
    private AID topic;
    private boolean end;
    private boolean flagOfFirstIter;

    private double tecPrice;
    private double vesPrice;

    private double tecMinPrice;
    private double vesMinPrice;

    private Map<AID , Double> agentsPaticipant = new HashMap<>();

    public AuctionDebate(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onStart() {
        topic = TopicHelper.register(myAgent, this.topicName);
    }

    @Override
    public void action() {
        ACLMessage proposesToTopic = getAgent().receive(MessageTemplate.MatchTopic(topic));
        if(proposesToTopic!=null) {
            Gson gson = new Gson();
            SendTaskDto sendTaskDto = gson.fromJson(proposesToTopic.getContent().split(" ")[0], SendTaskDto.class);

            if (!agentsPaticipant.containsKey(proposesToTopic.getSender())) {
                agentsPaticipant.put(proposesToTopic.getSender(), Double.valueOf(proposesToTopic.getContent().split(" ")[1]));
                log.info(String.valueOf(agentsPaticipant));
            }
            if (agentsPaticipant.containsKey(myAgent.getAID())) {

//                oneParticipantCase(proposesToTopic, sendTaskDto);
                ACLMessage myPropose = new ACLMessage(ACLMessage.REQUEST);
                myPropose.addReceiver(topic);
                if(!flagOfFirstIter) {
                    if(myAgent.getLocalName().equals("AgentTECProducer")) {
                        myPropose.setContent(proposesToTopic.getContent());
                        getAgent().send(myPropose);
                        log.info("First iterate" + myPropose);
                        flagOfFirstIter = true;
                    }
                    if(myAgent.getLocalName().equals("AgentSECProducer")) {
                        myPropose.setContent(proposesToTopic.getContent());
                        getAgent().send(myPropose);
                        log.info("First iterate" + myPropose);
                        flagOfFirstIter = true;
                    }
                    if(myAgent.getLocalName().equals("AgentVESProducer")) {
                        myPropose.setContent(proposesToTopic.getContent());
                        getAgent().send(myPropose);
                        log.info("First iterate" + myPropose);
                        log.info("First iterate" + myPropose);
                        flagOfFirstIter = true;
                    }
                }

                if(!myAgent.getLocalName().equals(proposesToTopic.getSender().getLocalName()) && flagOfFirstIter) {
                    if(myAgent.getLocalName().equals("AgentTECProducer")) {
                        vesPrice = Double.valueOf(proposesToTopic.getContent().split(" ")[2]);
                        if(sendTaskDto.getMyMaxPrice() < Double.valueOf(proposesToTopic.getContent().split(" ")[2])) {
                            if(sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                tecPrice = sendTaskDto.getMyMaxPrice();
                                log.info("ТЭЦ уравнял цену с клиентом" + tecPrice);
                            }
                        }
                        if(vesPrice <= tecPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = tecPrice;
                                tecPrice = 0.95*vesPrice;
                                log.info("ТЭЦ Конкурирую с ВЭС " + tecPrice );
                                if (tecPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    tecPrice = tmp;
                                    log.info("ТЭЦ Откат конкуренции " + tecPrice + "Моя минимальная цена " + agentsPaticipant.get(myAgent.getAID()));
                                }
                            } else {
                                log.info("ТЭЦ выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + tecPrice);
                            }

                        }
                        if(vesPrice > tecPrice) {
                            log.info("ТЭЦ Продолжаю торг со своей ценой " + tecPrice);
                        }
                        List<String> priceListTEC = new ArrayList<>(List.of(proposesToTopic.getContent().split(" ")));
                        priceListTEC.remove(2);
                        priceListTEC.remove(1);
                        priceListTEC.add(String.valueOf(agentsPaticipant.get(myAgent.getAID())));
                        priceListTEC.add(String.valueOf(tecPrice));
                        myPropose.setContent(priceListTEC.stream().collect(Collectors.joining(" ")));
                        log.info("ТЭЦ отсылаю свою цену " + myPropose);
                        getAgent().send(myPropose);
                    }
                    if(myAgent.getLocalName().equals("AgentSECProducer")) {

                    }
                    if(myAgent.getLocalName().equals("AgentVESProducer")) {
                        tecPrice = Double.valueOf(proposesToTopic.getContent().split(" ")[2]);
                        if(sendTaskDto.getMyMaxPrice() < Double.valueOf(proposesToTopic.getContent().split(" ")[2])) {
                            if(sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                vesPrice = sendTaskDto.getMyMaxPrice();
                                log.info("ВЭС уравнял цену с клиентом" + vesPrice);
                            }
                        }
                        if(tecPrice <= vesPrice) {
                            if (sendTaskDto.getMyMaxPrice() >= Double.valueOf(proposesToTopic.getContent().split(" ")[1])) {
                                double tmp = vesPrice;
                                vesPrice = 0.95*tecPrice;
                                log.info("ВЭС Конкурирую с ТЭЦ " + vesPrice);
                                if (vesPrice <= agentsPaticipant.get(myAgent.getAID())) {
                                    vesPrice = tmp;
                                    log.info("ВЭС Откат конкуренции " + vesPrice + " моя минимальная цена " + agentsPaticipant.get(myAgent.getAID()));
                                }
                            } else {
                                log.info("ВЭС выходит из торогов изза того, что будет продавать в минус на текущей ситуации" + vesPrice);
                            }

                        }
                        if(tecPrice > vesPrice) {
                            log.info("ВЭС Продолжаю торг со своей ценой " + vesPrice);
                        }
                        List<String> priceListVES = new ArrayList<>(List.of(proposesToTopic.getContent().split(" ")));
                        priceListVES.remove(2);
                        priceListVES.remove(1);
                        priceListVES.add(String.valueOf(agentsPaticipant.get(myAgent.getAID())));
                        priceListVES.add(String.valueOf(vesPrice));
                        myPropose.setContent(priceListVES.stream().collect(Collectors.joining(" ")));
                        log.info("ВЭС отсылаю свою цену " +myPropose);
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
        return end;
    }

    private void oneParticipantCase(ACLMessage proposesToTopic, SendTaskDto sendTaskDto) {
        if (agentsPaticipant.size() ==1 ) {
            ACLMessage toDistributer = new ACLMessage(ACLMessage.CONFIRM);
            if(sendTaskDto.getMyMaxPrice() <= Double.parseDouble(proposesToTopic.getContent().split(" ")[1])) {
                agentsPaticipant.put(myAgent.getAID(), sendTaskDto.getMyMaxPrice());
                toDistributer.setContent(String.valueOf(new ArrayList<>(agentsPaticipant.values()).get(0)));
                toDistributer.addReceiver(topic);
                log.info("No more concurents, i am sending propose " + toDistributer );
                getAgent().send(toDistributer);
            } else {
                log.info("the price is too low for me");
            }
            end = true;
        }
    }

    private void moreThenOneParticipant() {

    }

}
