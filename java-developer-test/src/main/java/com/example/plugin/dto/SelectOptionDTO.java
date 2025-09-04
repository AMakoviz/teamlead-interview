package com.example.plugin.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class SelectOptionDTO implements Serializable {

    public static final SelectOptionDTO EMPTY = new SelectOptionDTO();
    @XmlElement
    private String id;
    @XmlElement
    private String name;
    @XmlElement
    private String dropDownValue;

    @XmlElement
    private String dropDownHeader;

    @XmlElement
    private List<SelectOptionDTO> options;

    public SelectOptionDTO() {
        options = new ArrayList<>();
    }

    public SelectOptionDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SelectOptionDTO(String id, String name, String dropDownValue) {
        this.id = id;
        this.name = name;
        this.dropDownValue = dropDownValue;
    }

    public SelectOptionDTO(String id, String name, List<SelectOptionDTO> options) {
        this.id = id;
        this.name = name;
        this.options = options;
    }

    public void addOption(SelectOptionDTO optionDTO) {
        this.options.add(optionDTO);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDropDownValue() {
        return dropDownValue;
    }

    public void setDropDownValue(String dropDownValue) {
        this.dropDownValue = dropDownValue;
    }

    public String getDropDownHeader() {
        return dropDownHeader;
    }

    public void setDropDownHeader(String dropDownHeader) {
        this.dropDownHeader = dropDownHeader;
    }

    public List<SelectOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOptionDTO> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "SelectOptionDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dropDownValue='" + dropDownValue + '\'' +
                ", dropDownHeader='" + dropDownHeader + '\'' +
                ", options=" + options +
                '}';
    }
}
