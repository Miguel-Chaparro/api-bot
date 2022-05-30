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
import com.dom.ws.rest.bot.Request.questionsAnswersReq;
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

    public answerResp questionsBot(questionsAnswersReq req) {
        logg.info("*** Start questionsController questionsBot ***");
        msgError error = new msgError();
        customerWhatsappDTO dto = new customerWhatsappDTO();
        dto = whatsappData(req.getWhatsappId());
        answerResp statesResp = new answerResp();
        questionsVO question = new questionsVO();
        logg.log(Level.INFO, "DTO whatsappData *** {0}", dto.getError().getCode());
        switch (dto.getError().getCode()) {
            case 0:
                if (dto.getIdQuestions() == null || "".equals(dto.getIdQuestions())) {
                    question = buildNextQuestion(dto, 1);
                    dto.setIdQuestions("1");
                    statesResp.setError(updateQuestionCustomer(dto, false));
                    statesResp.setQuestion(question);
                } else {
                    statesResp = response(dto, req.getAnswer());
                }
                break;
            case 1:
                dto.setIdWhatsapp(req.getWhatsappId());
                dto.setName("");
                dto.setIdCustomer("");
                question = buildNextQuestion(dto, 1);
                logg.log(Level.INFO, "Case 1 questionsBot *** {0}", question.getIdQuestion() + " Desc:   " + question.getQuestionDesc());
                statesResp.setQuestion(question);
                statesResp.setError(updateQuestionCustomer(dto, true));
                break;
            default:
                error.setCode(-1);
                error.setMessage("¡Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
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
        logg.info("*** Start questionsController response ***");
        List<answerDTO> answerList = new ArrayList();
        answerList = validateOptions(req, option);
        logg.log(Level.INFO, "{0}***  questionsController response ***\n", "Opción: " + option + "\n solicitud" + req.getIdQuestions());
        logg.log(Level.INFO, "{0}***  segunda  ***\n", "Opción: " + answerList.get(0).getIdQuestion() + "\n ERROR " + answerList.get(0).getError().getCode());
        answerDTO val = new answerDTO();
        answerResp resp = new answerResp();

        questionsVO question = new questionsVO();
        question.setOptions(answerList);
        val = answerList.get(0);
        switch (val.getError().getCode()) {
            case 1:
                logg.log(Level.INFO, "{0}***  CASE 1 response ***\n", "Opción: " + option + "\n solicitud" + req.getIdQuestions().length());
                if (req.getIdQuestions().length() < 2) {
                    req.setIdQuestions("1.");
                }
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
        //val = answerList.get(0);
        logg.info("*** End questionsController Response ***");
        return resp;
    }

    private questionsVO buildNextQuestion(customerWhatsappDTO req, int option) {
        logg.info("*** Start questionsController buildNextQuestion ***");
        logg.log(Level.INFO, "{0}***  buildNextQuestion FIRST***\n---", req.getIdQuestions());
        questionsVO response = new questionsVO();
        questionsDAO dao = new questionsDAO();
        questionsDTO dto = new questionsDTO();
        questionsDTO questionDto = new questionsDTO();
        List<answerDTO> answerList = new ArrayList();
        answerDTO optionDto = new answerDTO();
        answerDAO ansdao = new answerDAO();
        String questionId = "";
        optionDto.setAnswerId(0);
        logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "Opción: " + req.getIdQuestions());
        if (option == 0) {
            logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "TRUE: " + questionId);
            questionId = req.getIdQuestions().substring(0, req.getIdQuestions().length() - 2);

            logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "QUESTION: " + questionId);
        } else {
            logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "Opción: " + false);
            if (req.getIdQuestions() == null || "".equals(req.getIdQuestions())) {

                questionId = "" + option;
                logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "QUIESTION: " + questionId);
            } else {
                questionId = req.getIdQuestions() + "." + option;
                logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "QUESTION: " + questionId);
                logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "QUESTION 1 : " + req.getIdQuestions());
            }
        }

        logg.log(Level.INFO, "{0}***  buildNextQuestion  2 ***\n", "question: " + questionId);

        if (questionId == null || questionId.equals("")) {
            response.setQuestionDesc("Para continuar debes seleccionar algunas de estas opciones:");
            optionDto.setIdQuestion("1");
            answerList = ansdao.readMany(optionDto);
            req.setIdQuestions("1");
            response.setIdQuestion("1");

        } else {
            optionDto.setIdQuestion(questionId);
            dto.setIdQuestions(questionId);
            answerList = ansdao.readMany(optionDto);
            questionDto = dao.readOne(dto);
            req.setIdQuestions(questionId);
            response.setQuestionDesc(message(req.getName(), questionDto));
            response.setIdQuestion(questionId);
        }

        response.setOptions(answerList);
        updateQuestionCustomer(req, false);
        logg.info("*** End questionsController buildNextQuestion ***");
        return response;
    }

    private String message(String name, questionsDTO preview) {
        logg.info("*** Start questionsController message ***");
        String question;
        String data;
        question = preview.getQuestions();

        logg.log(Level.INFO, "{0}***  Message ***\n", "name: -" + name + "-");
        if (name == null || "".equals(name)) {
            logg.log(Level.INFO, "{0}***  Message If true***\n", "name: " + name);
            data = "";
        } else {

            data = " *" + name + "* ";
        }
        //Aca va el siwtch case para validar las preguntas
        logg.log(Level.INFO, "{0}***  Message 2***\n", "preview: -" + preview.getIdQuestions() + "-");

        if (null != question) {
            if (question.split("##").length > 1) {
                logg.log(Level.INFO, "{0}***  Message 2 If true***\n", "preview: " + preview.getIdQuestions());
                question = question.replaceAll("##", data);
            }

        } else {
            question = "Lo siento, encontraste un fallo en la matrix, estoy solucionandolo";
        }

        logg.info("*** END questionsController message ***");
        return question;

    }

    private msgError updateQuestionCustomer(customerWhatsappDTO dto, boolean create) {
        logg.info("*** Start questionsController updateQuestionCustomer ***");
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
            response.setMessage("¡Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
        }
        logg.info("*** End questionsController updateQuestionCustomer ***");
        return response;

    }

    private List<answerDTO> validateOptions(customerWhatsappDTO dto, String option) {
        logg.info("*** Start questionsController validateOptions ***");
        answerDAO dao = new answerDAO();
        answerDTO req = new answerDTO();
        List<answerDTO> answerList = new ArrayList();
        req.setIdQuestion(dto.getIdQuestions());
        req.setAnswerId(0);
        msgError rta = new msgError();
        answerList = dao.readMany(req);
        int indicator = -1;
        int count = 1;
        try {
            indicator = Integer.parseInt(option);
            logg.log(Level.INFO, "{0}***  questionsController validateOptions ***\n", "Opción: " + indicator);
            rta.setCode(-1);
            rta.setMessage("¡Ups! Lo siento esta opción ingresada no es valida, Estoy aprendiendo día a día con el fin de garantizar un mejor servicio");
            if (indicator == 0) {
                logg.info("*** INDICADOR 0 ***");

                rta.setCode(1);
                rta.setMessage("Back");
            } else {
                for (answerDTO q : answerList) {
                    logg.log(Level.INFO, "{0}\n", "for: " + q.getAnswerId() + "\n" + indicator);
                    if (q.getAnswerId() == indicator) {
                        logg.log(Level.INFO, "{0}\n", "for - IF: " + q.getIdQuestion());

                        rta.setCode(0);
                        rta.setMessage("Ok");
                        req = q;
                        break;
                    }
                    count++;

                }
            }
        } catch (NumberFormatException ex) {
            if (dto.getIdQuestions().equals("") || dto.getIdQuestions() == null) {
                dto.setIdQuestions("1");
                updateQuestionCustomer(dto, false);
                rta.setCode(0);
                rta.setMessage("Start");

            } else {
                rta.setCode(-1);
                rta.setMessage("¡Ups! Estoy aprendiendo a leer... por favor ingresa solo digitos");
            }

        }
        /*if (!flgOK) {
            rta.setCode(-1);
            rta.setMessage("¡Ups! Lo siento esta opción ingresada no es valida, Estoy aprendiendo día a día con el fin de garantizar un mejor servicio");
        } else {
            String updateQuestion = dto.getIdQuestions() + "." + option;
            req.setIdQuestion(updateQuestion);
            dto.setIdQuestions(updateQuestion);
            updateQuestionCustomer(dto, false);
            
        }*/
        req.setAnswerId(indicator);
        req.setAnswerDesc("" + count);
        req.setError(rta);
        answerList.add(0, req);
        logg.info("*** End questionsController validaOptions ***");
        return answerList;

    }

}
