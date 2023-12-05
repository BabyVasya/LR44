package org.example.Consumer;


import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "graphic")
public class CfgGraphic {
    @Getter
    private List<Double> time;
    @Getter
    private List<Double> power;
}
