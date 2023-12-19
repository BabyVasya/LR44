package org.example.Producer;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "graphic")
public class CfgProduceGraphic {
    @Getter
    private List<Integer> time;
    @Getter
    private List<Double> power;
    @Getter
    private List<Double> price;
}
