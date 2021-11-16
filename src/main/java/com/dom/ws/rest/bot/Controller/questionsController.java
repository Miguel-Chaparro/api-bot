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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author MIGUEL
 */
public class questionsController {

    static final Logger logg = Logger.getLogger(questionsController.class.getName());

  

    public answerResp questionsBot(answerReq req) {
        logg.info("*** Start questionsController questionsBot ***");
        msgError error = new msgError();
        customerWhatsappDTO dto = new customerWhatsappDTO();
        dto = whatsappData(req.getWhatsappId());
        answerResp statesResp = new answerResp();
        questionsVO question = new questionsVO();
        logg.log(Level.INFO, "DTO whatsappData *** {0}", dto.getError().getCode());
        switch (dto.getError().getCode()) {
            case 0:
                if (dto.getIdQuestions() == null) {
                    question = buildNextQuestion(dto, 1);
                    statesResp.setError(updateQuestionCustomer(dto,false));
                    statesResp.setQuestion(question);
                }else{
                    statesResp = response(dto,req.getAnswer());
                }
                break;
            case 1:
                dto.setIdWhatsapp(req.getWhatsappId());
                dto.setName("");
                dto.setIdCustomer("");
                question = buildNextQuestion(dto, 1);
                logg.log(Level.INFO, "Case 1 questionsBot *** {0}", question.getIdQuestion() + " Desc:   "+ question.getQuestionDesc());
                statesResp.setQuestion(question);
                statesResp.setError(updateQuestionCustomer(dto,true));
                break;
            default:
                error.setCode(-1);
                error.setMessage("? ¡Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
                break;
        }
        statesResp.setError(error);
        statesResp.setWhatsapp(req.getWhatsappId());
        logg.log(Level.INFO, "{0}*** End questionsController whatsappData ***", statesResp.getError().getMessage());
        logg.info("*** End questionsController whatsappData ***");
        return statesResp;
    }

    private customerWhatsappDTO whatsappData(String Whatsapp) {
        logg.info("*** Start questionsController whatsappData ***");
        customerWhatsappDAO customerWap = new customerWhatsappDAO();
        customerWhatsappDTO reqDTO = new customerWhatsappDTO();
        customerWhatsappDTO respDTO = new customerWhatsappDTO();
        reqDTO.setIdWhatsapp(Whatsapp);
        respDTO = customerWap.readOne(reqDTO);
        logg.info("*** End questionsController whatsappData ***");
        return respDTO;

    }

    private answerResp response(customerWhatsappDTO req, String option) {
        List<answerDTO> answerList = new ArrayList();
        answerList = validateOptions(req, option);
        answerDTO val = new answerDTO();
        answerResp resp = new answerResp();
        questionsVO question = new questionsVO();
        val = answerList.get(0);
        switch (val.getError().getCode()) {
            case 1:
                question = buildNextQuestion(req, 0);
                break;
            case -1:
                question.setQuestionDesc(val.getError().getMessage());
                break;
            case 0:
                question = buildNextQuestion(req, val.getAnswerId());
                break;
            default:
                question.setQuestionDesc(val.getError().getMessage()); 
                break;
        }
        resp.setQuestion(question);
        
        resp.setError(val.getError());
        return resp;
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
            if (req.getIdQuestions() == null){
                questionId = ""+option;
            }else{
                questionId = req.getIdQuestions() + "." + option;
            }
            
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
            response.setMessage("? ¡Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
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
        int indicator;
        boolean flgOK = false;
        try {
            indicator = Integer.parseInt(option);
            if (indicator == 0) {
                rta.setCode(1);
                rta.setMessage("");
            } else {

                for (answerDTO q : answerList) {
                 
                    if (q.getAnswerId() == indicator) {
                        flgOK = true;
                        req.setAnswerId(indicator);
                        req = q;
                        break;
                    }
                }
            }

        } catch (NumberFormatException ex) {
            if (dto.getIdQuestions().equals("1") || dto.getIdQuestions() == null) {
                dto.setIdQuestions("1");
                rta = updateQuestionCustomer(dto, false);
            } else {
                rta.setCode(-1);
                rta.setMessage("? ¡Ups! Estoy aprendiendo a leer... por favor ingresa solo digitos");
            }

        }
        if (!flgOK) {
            rta.setCode(-1);
            rta.setMessage("? ¡Ups! Lo siento esta opción ingresada no es valida, Estoy aprendiendo día a día con el fin de garantizar un mejor servicio");
        } else {
            req.setIdQuestion(dto.getIdQuestions() + "." + option);
            
            rta = updateQuestionCustomer(dto, false);
        }
        req.setError(rta);
        answerList.add(0, req);
        return answerList;

    }
    
 
}
