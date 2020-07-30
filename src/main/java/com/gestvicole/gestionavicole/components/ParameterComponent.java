package com.gestvicole.gestionavicole.components;

import com.gestvicole.gestionavicole.entities.Parameter;
import com.gestvicole.gestionavicole.repositories.ParameterRepository;
import com.gestvicole.gestionavicole.utils.ProjectUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ParameterComponent {
    private final ParameterRepository parameterRepository;

    public ParameterComponent(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    public String generateInvoiceNumber() {
        try {
            Parameter params = parameterRepository.findParameterById(1L);
            return ProjectUtils.toString(new Date(), "MMyyyy") + "-" + ProjectUtils.generateStringNumber(params.getLastInvoiceNumber() + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateOrderNumber() {
        try {
            Parameter params = parameterRepository.findParameterById(1L);
            return ProjectUtils.toString(new Date(), "MMyyyy") + "-" + ProjectUtils.generateStringNumber(params.getLastOrderNumber() + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void updateOrderNumber() {
        Parameter params = parameterRepository.findParameterById(1L);
        params.setLastOrderNumber(params.getLastOrderNumber() + 1);
        parameterRepository.save(params);
    }

    public void updateInvoiceNumber() {
        Parameter params = parameterRepository.findParameterById(1L);
        params.setLastInvoiceNumber(params.getLastInvoiceNumber() + 1);
        parameterRepository.save(params);
    }

}
