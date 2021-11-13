/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.answerDAO;
import com.dom.ws.rest.bot.DAO.customerWhatsappDAO;
import com.dom.ws.rest.bot.DAO.questionsDAO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.customerWhatsappDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.Request.answerReq;
import com.dom.ws.rest.bot.Response.answerResp;
import com.dom.ws.rest.bot.vo.msgError;
import com.dom.ws.rest.bot.vo.questionsVO;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author MIGUEL
 */
public class questionsController {

    static Logger log = Logger.getLogger(questionsController.class);

    private msgError error;

    public answerResp questionsBot(answerReq req) {
        customerWhatsappDTO dto = new customerWhatsappDTO();
        dto = whatsappData(req.getWhatsappId());
        answerResp statesResp = new answerResp();
        questionsVO question = new questionsVO();
        switch (dto.getError().getCode()) {
            case 0:
                if (dto.getIdQuestions() == null) {

                }
                break;
            case 1:

                break;
            default:
                break;
        }

        return null;
    }

    private customerWhatsappDTO whatsappData(String Whatsapp) {
        log.info("*** Start questionsController whatsappData ***");
        customerWhatsappDAO customerWap = new customerWhatsappDAO();
        customerWhatsappDTO reqDTO = new customerWhatsappDTO();
        customerWhatsappDTO respDTO = new customerWhatsappDTO();
        reqDTO.setIdWhatsapp(Whatsapp);
        respDTO = customerWap.readOne(reqDTO);

        log.info("*** Start questionsController whatsappData ***");
        return respDTO;

    }

    private answerResp response(customerWhatsappDTO req, String option) {
        List<answerDTO> answerList = new ArrayList();
        answerList = validateOptions(req, option);
        answerDTO val = new answerDTO();
        val = answerList.get(0);
        switch (val.getError().getCode()) {
            case 1:
                break;
            case -1:
                break;
            case 0:

                break;
            default:
                break;
        }
    }

    private questionsVO buildNextQuestion(customerWhatsappDTO req, int option) {
        questionsVO response = new questionsVO();
        questionsDAO dao = new questionsDAO();
        questionsDTO dto = new questionsDTO();
        questionsDTO questionDto = new questionsDTO();
        List<answerDTO> answerList = new ArrayList();
        answerDTO optionDto = new answerDTO();
        answerDAO ansdao = new answerDAO();
        String questionId;
        if (option == 0) {
            questionId = req.getIdQuestions().substring(req.getIdQuestions().length() - 2);
        } else {
            questionId = req.getIdQuestions() + "." + option;
        }

        optionDto.setIdQuestion(questionId);
        dto.setIdQuestions(questionId);
        answerList = ansdao.readMany(optionDto);
        questionDto = dao.readOne(dto);

        response.setIdQuestion(questionId);
        response.setQuestionDesc(message(req.getName(), questionDto));
        response.setOptions(answerList);
        return response;
    }

    private String message(String name, questionsDTO preview) {
        String question;
        String data;
        question = preview.getQuestions();
        if (name == null) {
            data = "";
        } else {
            data = " *" + name + "* ";
        }
        //Aca va el siwtch case para validar las preguntas
        if (preview.getIdQuestions().equals("1") || preview.getIdQuestions() == null) {
            question = question.replaceFirst("##", data);
        }
        return question;

    }

    private msgError updateQuestionCustomer(customerWhatsappDTO dto, boolean create) {
        Instant instan;
        Timestamp current;
        instan = Instant.now();
        current = Timestamp.from(instan);
        dto.setDate(current);
        msgError response = new msgError();
        boolean state;
        customerWhatsappDAO customer = new customerWhatsappDAO();
        if (create) {
            state = customer.create(dto);
        } else {
            state = customer.update(dto);
        }
        if (state) {
            response.setCode(0);
            response.setMessage("");
        } else {
            response.setCode(-1);
            response.setMessage("🤖 ¡Ups! Lo sentimos, en estos momenotos no podemos atenderlo, favor intente mas tarde");
        }
        return response;

    }

    private List<answerDTO> validateOptions(customerWhatsappDTO dto, String option) {
        answerDAO dao = new answerDAO();
        answerDTO req = new answerDTO();
        List<answerDTO> answerList = new ArrayList();
        req.setIdQuestion(dto.getIdQuestions());
        msgError rta = new msgError();
        answerList = dao.readMany(req);
        int count = 0;
        int indicator;
        boolean flgOK = false;
        try {
            indicator = Integer.parseInt(option);
            if (indicator == 0) {
                rta.setCode(1);
                rta.setMessage("");
            } else {

                for (answerDTO q : answerList) {
                    if (count == 0) {
                        req = q;
                        count++;
                    }

                    if (q.getAnswerId() == indicator) {
                        flgOK = true;
                    }
                }
            }

        } catch (NumberFormatException ex) {
            if (dto.getIdQuestions().equals("1") || dto.getIdQuestions() == null) {
                dto.setIdQuestions("1");
                rta = updateQuestionCustomer(dto, false);
            } else {
                rta.setCode(-1);
                rta.setMessage("🤖 ¡Ups! Estoy aprendiendo dia a dia... por favor ingresa solo digitos");
            }

        }
        if (!flgOK) {
            rta.setCode(-1);
            rta.setMessage("🤖 ¡Ups! Lo siento esta opción ingresada no es valida, Estoy aprendiendo dia a dia con el fin de garantizar un mejor servicio");
        } else {
            dto.setIdQuestions(dto.getIdQuestions() + "." + option);
            rta = updateQuestionCustomer(dto, false);
        }
        req.setError(rta);
        answerList.add(0, req);
        return answerList;

    }
    
    private msgError validateCustomer (customerWhatsappDTO dto, String option){
        
    }
}
