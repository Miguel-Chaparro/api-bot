/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Services;

import com.dom.ws.rest.bot.Controller.getQuestionsController;
import com.dom.ws.rest.bot.Controller.projectController;
import com.dom.ws.rest.bot.Controller.questionsController;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.Request.answerReq;
import com.dom.ws.rest.bot.Request.questionsAnswersReq;
import com.dom.ws.rest.bot.Request.projectsReq;
import com.dom.ws.rest.bot.Response.answerResp;
import com.dom.ws.rest.bot.Response.getAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsResp;
import com.dom.ws.rest.bot.Response.projectsResp;
import java.util.concurrent.ExecutorService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author MIGUEL
 */
@Path("/AccessApi")
@Provider
public class api {

    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/domBot")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void questions(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doQuestions(request));
            }
        });
    }

    private answerResp doQuestions(questionsAnswersReq request) {

        answerResp response = new answerResp();
        questionsController ctrl = new questionsController();
        response = ctrl.questionsBot(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/surveyBot")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void survey(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doSurvey(request));
            }
        });
    }

    private answerResp doSurvey(questionsAnswersReq request) { 

        answerResp response = new answerResp();
        questionsController ctrl = new questionsController();
        response = ctrl.questionsBot(request);
        return response;
    }
    
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/raspi")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void raspi (@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doRaspi(request));
            }
        });
    }

    private answerResp doRaspi(questionsAnswersReq request) {

        answerResp response = new answerResp();
        questionsController ctrl = new questionsController();
        response = ctrl.questionsBot(request);
        return response;
    }
    
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getProjects")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void getProjects (@Suspended final AsyncResponse asyncResponse, final projectsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetProjects(request));
            }
        });
    }

    private projectsResp doGetProjects (projectsReq request) {

        projectsResp response = new projectsResp();
        projectController ctrl = new projectController();
        response = ctrl.getProjectUser(request);
        return response;
    }
    
    
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getQuestionsAnswers")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void getQuestionsAnswer (@Suspended final AsyncResponse asyncResponse, final projectsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetQuestionsAnswer(request));
            }
        });
    }

    private getQuestionsAnswerResp doGetQuestionsAnswer(projectsReq request) {

        getQuestionsAnswerResp response = new getQuestionsAnswerResp();
        getQuestionsController ctrl = new getQuestionsController();
        response = ctrl.getQuestionAnswer(request);
        return response;
    }
    
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getQuestions")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void getQuestions (@Suspended final AsyncResponse asyncResponse, final projectsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetQuestions(request));
            }
        });
    }

    private getQuestionsResp doGetQuestions(projectsReq request) {

        getQuestionsResp response = new getQuestionsResp();
        getQuestionsController ctrl = new getQuestionsController();
        response = ctrl.getQuestions(request);
        return response;
    }
    
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getAnswers")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void getAnswers (@Suspended final AsyncResponse asyncResponse, final answerReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetAnswer(request));
            }
        });
    }

    private getAnswerResp doGetAnswer(answerReq request) {

        getAnswerResp response = new getAnswerResp();
        getQuestionsController ctrl = new getQuestionsController();
        response = ctrl.getAnswers(request);
        return response;
    }
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createProject")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void createProyect (@Suspended final AsyncResponse asyncResponse, final projectDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateProject(request));
            }
        });
    }

    private projectDTO doCreateProject(projectDTO request) {

        projectDTO response = new projectDTO();
        projectController ctrl = new projectController();
        response = ctrl.createProjects(request);
        return response;
    }
}
