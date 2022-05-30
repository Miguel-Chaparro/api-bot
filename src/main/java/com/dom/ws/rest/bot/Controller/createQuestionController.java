/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.questionsDAO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.Request.createQuestionsReq;
import com.dom.ws.rest.bot.Response.createQuestionsResp;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Ch
 */
public class createQuestionController {

    static final Logger log = Logger.getLogger(createQuestionController.class.getName());

    public createQuestionsResp createQuestions(createQuestionsReq req) {
        log.info("***start createQuestionController createQuestions***");
        createQuestionsResp response = new createQuestionsResp();
        List<questionsDTO> listQuestions = new ArrayList();
        questionsDTO valPreview = new questionsDTO();
        int countErrors = 0;
        int countQuestions = 0;
        msgError error = new msgError();
        String idQuestion = "";
        boolean status = false;
        if (!req.getPreviousQuestion().equals("0")) {
            valPreview = getQuestion(req.getIdProject(), req.getPreviousQuestion());
            error = valPreview.getError();
            if (error.getCode() == 0) {
                //log.log(Level.INFO, "---- {0}\n{1}", new Object[]{valPreview.getNextQuestion(), req.getPreviousAnswer()});
                if (valPreview.getNextQuestion() == 0 && !req.getPreviousAnswer().equals("0")) {
                    idQuestion = req.getPreviousQuestion() + "." + req.getPreviousAnswer() + ".";
                    status = true;
                } else {
                    error.setCode(-1);
                    error.setMessage("Por favor Valida los datos ingresados, La pregunta anterior o la respuesta");
                }
            }
        } else {
            status = true;
        }
        if (status) {
            for (questionsDTO dto : req.getQuestions()) {
                String questionsId = "";
                questionsId = idQuestion + dto.getIdQuestions();
                dto.setIdProject(req.getIdProject());
                dto.setIdQuestions(questionsId);
                msgError errorDto = new msgError();
                errorDto = createUpdateQuestions(dto);
                if (errorDto.getCode() != 0) {
                    countErrors++;
                }
                dto.setError(errorDto);
                listQuestions.add(dto);
                countQuestions++;
            }
            if (countErrors > 0) {
                error.setCode(-1);
                error.setMessage("Se presentaron " + countErrors + " Errores de " + countQuestions + " Preguntas");
            } else {
                error.setCode(0);
                error.setMessage("Registros completados con exito");
            }
        }
        response.setError(error);
        response.setQuestions(listQuestions);
        log.info("***End createQuestionController createQuestions***");
        return response;
    }

    public msgError createUpdateQuestions(questionsDTO dto) {
        log.info("***start createQuestionController createUpdateQuestions***");
        questionsDTO resp = new questionsDTO();
        questionsDAO dao = new questionsDAO();
        boolean flgCreate = false;
        boolean flgUpdate = false;
        msgError error = new msgError();
        flgCreate = dao.create(dto);
        if (flgCreate) {
            error.setCode(0);
            error.setMessage("Pregunta creada correctamente");
        } else {
            flgUpdate = dao.update(dto);
            error.setCode(0);
            error.setMessage("Pregunta Actualizada correctamente");
            if (!flgUpdate) {
                error.setCode(-1);
                error.setMessage("Uppss.. Pregunta NO gestionada, Valida nuevamente.");
            }
        }
        resp.setError(error);
        log.info("***End createQuestionController createUpdateQuestions***");
        return error;
    }

    public questionsDTO getQuestion(int idProject, String idQuestions) {
        log.info("***start getQuestion createUpdateQuestions***");
        questionsDTO req = new questionsDTO();
        questionsDTO resp = new questionsDTO();
        questionsDAO dao = new questionsDAO();
        req.setIdProject(idProject);
        req.setIdQuestions(idQuestions);
        resp = dao.readOne(req);
        log.info("***End getQuestion createUpdateQuestions***");
        return resp;
    }
}
