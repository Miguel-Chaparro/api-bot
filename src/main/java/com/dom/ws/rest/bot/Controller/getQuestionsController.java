/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.answerDAO;
import com.dom.ws.rest.bot.DAO.questionsDAO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.Request.answerReq;
import com.dom.ws.rest.bot.Request.createProjectsReq;
import com.dom.ws.rest.bot.Response.getAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsResp;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Teletrabajo
 */
public class getQuestionsController {
    
    static final Logger log = Logger.getLogger(getQuestionsController.class.getName());
    
    public getQuestionsAnswerResp getQuestionAnswer (createProjectsReq req){
        log.info("***start getQuestionsController getProjectUser***");
        getQuestionsAnswerResp resp = new getQuestionsAnswerResp();
        questionsDTO quesDto = new questionsDTO();
        answerDTO ansDto = new answerDTO();
        questionsDAO quesDao = new questionsDAO();
        answerDAO ansDAO = new answerDAO();
        msgError errorQuery = new msgError();
        ansDto.setAnswerId(100);
        ansDto.setIdProject(req.getIdCodeProject());
        quesDto.setIdProject(req.getIdCodeProject());
        List<questionsDTO> arrayQuestion = new ArrayList();
        List<answerDTO> arrayAnswer = new ArrayList();
        arrayQuestion = quesDao.readMany(quesDto);
        arrayAnswer = ansDAO.readMany(ansDto);
        errorQuery = arrayQuestion.get(0).getError();
        if(errorQuery.getCode()==0){
            resp.setError(arrayAnswer.get(0).getError());
        }else{
            resp.setError(errorQuery);
        }
        resp.setAnswers(arrayAnswer);
        resp.setQuestions(arrayQuestion);
        log.info("***end getQuestionsController getProjectUser***");
        return resp;
        
    }
    
    public getQuestionsResp getQuestions(createProjectsReq req){
        log.info("***start getQuestionsController getQuestions***");
        getQuestionsResp resp = new getQuestionsResp();
        questionsDTO quesDto = new questionsDTO();
        msgError errorQuery = new msgError();
        quesDto.setIdProject(req.getIdCodeProject());
        questionsDAO quesDao = new questionsDAO();
        List<questionsDTO> arrayQuestion = new ArrayList();
        arrayQuestion = quesDao.readMany(quesDto);
        errorQuery = arrayQuestion.get(0).getError();
        resp.setError(errorQuery);
        resp.setQuestions(arrayQuestion);
        log.info("***end getQuestionsController getQuestions***");
        return resp;
    }
    
    public getAnswerResp getAnswers(answerReq req){
        log.info("***Start getQuestionsController getAnswers***");
        getAnswerResp resp = new getAnswerResp();
        answerDTO ansDto = new answerDTO();
        answerDAO ansDAO = new answerDAO();
        msgError errorQuery = new msgError();
        List<answerDTO> arrayAnswer = new ArrayList();
        ansDto.setIdProject(req.getIdProject());
        try{
            ansDto.setIdQuestion(req.getIdQuestion());
            if(ansDto.getIdQuestion().equals("")){
                ansDto.setAnswerId(100);
            }else{
                ansDto.setAnswerId(0);
            }
        }catch(Exception ex){
            ansDto.setAnswerId(100);
            log.log(Level.WARNING, "{0} Validatate req", ex);
        }
        
        arrayAnswer = ansDAO.readMany(ansDto);
        errorQuery = arrayAnswer.get(0).getError();
        resp.setError(errorQuery);
        resp.setAnswers(arrayAnswer);
        log.info("***end getQuestionsController getAnswers***");
        return resp;
    }
}
