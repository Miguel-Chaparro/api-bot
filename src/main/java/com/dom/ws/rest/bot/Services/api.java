/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Services;

import com.dom.ws.rest.bot.Controller.questionsController;
import com.dom.ws.rest.bot.Request.answerReq;
import com.dom.ws.rest.bot.Response.answerResp;
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

    public void questions(@Suspended final AsyncResponse asyncResponse, final answerReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doQuestions(request));
            }
        });
    }

    private answerResp doQuestions(answerReq request) {

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

    public void survey(@Suspended final AsyncResponse asyncResponse, final answerReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doSurvey(request));
            }
        });
    }

    private answerResp doSurvey(answerReq request) { 

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

    public void raspi (@Suspended final AsyncResponse asyncResponse, final answerReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doRaspi(request));
            }
        });
    }

    private answerResp doRaspi(answerReq request) {

        answerResp response = new answerResp();
        questionsController ctrl = new questionsController();
        response = ctrl.questionsBot(request);
        return response;
    }
}
