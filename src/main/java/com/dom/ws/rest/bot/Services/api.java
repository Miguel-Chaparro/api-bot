/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Services;

import com.dom.ws.rest.bot.Controller.answerController;
import com.dom.ws.rest.bot.Controller.createQuestionController;
import com.dom.ws.rest.bot.Controller.devicesConfigController;
import com.dom.ws.rest.bot.Controller.getQuestionsController;
import com.dom.ws.rest.bot.Controller.projectController;
import com.dom.ws.rest.bot.Controller.questionsController;
import com.dom.ws.rest.bot.DAO.ProfileDAO;
import com.dom.ws.rest.bot.DAO.UserDAO;
import com.dom.ws.rest.bot.DTO.ProfileDTO;
import com.dom.ws.rest.bot.DTO.UserDTO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.DTO.raspiDTO;
import com.dom.ws.rest.bot.Request.AssignProfileRequest;
import com.dom.ws.rest.bot.Request.answerReq;
import com.dom.ws.rest.bot.Request.questionsAnswersReq;
import com.dom.ws.rest.bot.Request.createProjectsReq;
import com.dom.ws.rest.bot.Request.createQuestionsReq;
import com.dom.ws.rest.bot.Request.raspiReq;
import com.dom.ws.rest.bot.Response.LoginResponse;
import com.dom.ws.rest.bot.Response.answerResp;
import com.dom.ws.rest.bot.Response.createQuestionsResp;
import com.dom.ws.rest.bot.Response.getAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsResp;
import com.dom.ws.rest.bot.Response.projectsResp;
import com.dom.ws.rest.bot.Response.raspiResp;
import com.dom.ws.rest.bot.vo.msgError;
import com.google.firebase.auth.FirebaseToken;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
     * agregar cookie para autenticacion
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/domBot")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void questions(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request,@CookieParam("session") String session) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
           /*      try {
                    questionsController controller = new questionsController();
                    getQuestionsAnswerResp response = controller.questions(request,session);
                    asyncResponse.resume(response);
                } catch (Exception e) {
                    msgError error = new msgError();
                    error.setError(e.getMessage());
                    asyncResponse.resume(error);
                } */
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

    public void raspi(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request) {
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

    public void getProjects(@Suspended final AsyncResponse asyncResponse, final createProjectsReq request, @Context ContainerRequestContext requestContext) {
         FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
         
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                // Agregar el ID del usuario a la petición
                request.setIdUser(user.getUid());
                asyncResponse.resume(doGetProjects(request));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });

    }
    private projectsResp doGetProjects(createProjectsReq request) {

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

    public void getQuestionsAnswer(@Suspended final AsyncResponse asyncResponse, final createProjectsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetQuestionsAnswer(request));
            }
        });
    }

    private getQuestionsAnswerResp doGetQuestionsAnswer(createProjectsReq request) {

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

    public void getQuestions(@Suspended final AsyncResponse asyncResponse, final createProjectsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetQuestions(request));
            }
        });
    }

    private getQuestionsResp doGetQuestions(createProjectsReq request) {

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

    public void getAnswers(@Suspended final AsyncResponse asyncResponse, final answerReq request) {
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

    public void createProyect(@Suspended final AsyncResponse asyncResponse, final projectDTO request) {
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

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createUpdateManyQuestions")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void createManyQuestions(@Suspended final AsyncResponse asyncResponse, final createQuestionsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateManyQuestions(request));
            }
        });
    }

    private createQuestionsResp doCreateManyQuestions(createQuestionsReq request) {

        createQuestionsResp response = new createQuestionsResp();
        createQuestionController ctrl = new createQuestionController();
        response = ctrl.createQuestions(request);
        return response;
    }
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createUpdateOneQuestion")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void createOneQuestion(@Suspended final AsyncResponse asyncResponse, final questionsDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateOneQuestions(request));
            }
        });
    }

    private msgError doCreateOneQuestions(questionsDTO request) {

        msgError response = new msgError();
        createQuestionController ctrl = new createQuestionController();
        response = ctrl.createUpdateQuestions(request);
        return response;
    }
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createUpdateOneAnswer")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void createOneAnswer(@Suspended final AsyncResponse asyncResponse, final answerDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateOneAnswer(request));
            }
        });
    }

    private msgError doCreateOneAnswer(answerDTO request) {

        msgError response = new msgError();
        answerController ctrl = new answerController();
        response = ctrl.updateCreateAnswers(request);
        return response;
    }
    
        /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/updateProject")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void updateProject (@Suspended final AsyncResponse asyncResponse, final projectDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doUpdateProject(request));
            }
        });
    }

    private msgError doUpdateProject (projectDTO request) {

        msgError response = new msgError();
        projectController ctrl = new projectController();
        response = ctrl.updateProject(request);
        return response;
    }
    
    
        /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/configDevices")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void configDevices (@Suspended final AsyncResponse asyncResponse, final raspiReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doConfigDevices(request));
            }
        });
    }

    private raspiResp doConfigDevices (raspiReq request) {
        raspiResp response = new raspiResp();
        devicesConfigController ctrl = new devicesConfigController();
        response = ctrl.getConfigDevice(request);
        return response;
    }
    
        /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createConfigDevices")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})

    public void createConfigDevices (@Suspended final AsyncResponse asyncResponse, final raspiDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateConfigDevices(request));
            }
        });
    }

    private msgError doCreateConfigDevices(raspiDTO request) {

        msgError response = new msgError();
        devicesConfigController ctrl = new devicesConfigController();
        response = ctrl.createConfigDevices(request);
        return response;
    }

    /**
     * Endpoint de login que utiliza la información del token de Firebase
     * @param asyncResponse
     * @param requestContext Contexto de la petición que contiene el token decodificado
     */
    @POST
    @Path(value = "/login")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public void login(@Suspended final AsyncResponse asyncResponse, @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doLogin(decodedToken));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });
    }

    private LoginResponse doLogin(FirebaseToken decodedToken) {
        LoginResponse response = new LoginResponse();
        UserDTO userDTO = new UserDTO();
        
        // Llenar el DTO con la información del token
        userDTO.setId(decodedToken.getUid());
        userDTO.setEmail(decodedToken.getEmail());
        userDTO.setDisplayName(decodedToken.getName());
        userDTO.setPhotoUrl(decodedToken.getPicture());
        userDTO.setEmailVerified(decodedToken.isEmailVerified());
        userDTO.setProviderId(decodedToken.getIssuer());
        userDTO.setCreationTime(new Timestamp(System.currentTimeMillis()));
        userDTO.setLastSignInTime(new Timestamp(System.currentTimeMillis()));
        
        UserDAO userDAO = new UserDAO();
        ProfileDAO profileDAO = new ProfileDAO();
        boolean exists = userDAO.exists(userDTO.getId());
        
        if (!exists) {
            boolean created = userDAO.create(userDTO);
            response.setNewUser(true);
            response.setCreated(created);
            
            // Si es un nuevo usuario, asignar perfil por defecto (por ejemplo, cliente)
            if (created) {
                profileDAO.assignProfileToUser(userDTO.getId(), 3); // ID 3 = Cliente por defecto
            }
        } else {
            response.setNewUser(false);
        }
        
        // Obtener los perfiles del usuario
        List<ProfileDTO> userProfiles = profileDAO.getUserProfiles(userDTO.getId());
        response.setProfiles(userProfiles);
        
        return response;
    }

    /**
     * Endpoint para asignar perfiles a usuarios (solo administradores)
     * @param asyncResponse
     * @param request
     * @param requestContext
     */
    @POST
    @Path(value = "/assignProfile")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public void assignProfile(@Suspended final AsyncResponse asyncResponse, 
                            final AssignProfileRequest request,
                            @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doAssignProfile(request, decodedToken.getUid()));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });
    }

    private Response doAssignProfile(AssignProfileRequest request, String adminUserId) {
        ProfileDAO profileDAO = new ProfileDAO();
        
        // Verificar si el usuario que hace la petición es administrador
        if (!profileDAO.isUserAdmin(adminUserId)) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(new msgError(-1, "Solo los administradores pueden asignar perfiles"))
                .build();
        }
        
        // Intentar asignar el perfil
        boolean success = profileDAO.assignProfileToUser(request.getUserId(), request.getProfileId());
        
        if (success) {
            return Response.ok(new msgError(0, "Perfil asignado correctamente")).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new msgError(-1, "No se pudo asignar el perfil"))
                .build();
        }
    }

    /**
     * Endpoint para consultar usuarios registrados (solo administradores)
     * @param asyncResponse
     * @param requestContext
     */
    @GET
    @Path(value = "/users")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public void getUsers(@Suspended final AsyncResponse asyncResponse,
                        @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doGetUsers(decodedToken.getUid()));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });
    }

    private Response doGetUsers(String adminUserId) {
        ProfileDAO profileDAO = new ProfileDAO();
        
        // Verificar si el usuario que hace la petición es administrador
        if (!profileDAO.isUserAdmin(adminUserId)) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(new msgError(-1, "Solo los administradores pueden consultar usuarios"))
                .build();
        }
        
        // Obtener la lista de usuarios
        UserDAO userDAO = new UserDAO();
        List<UserDTO> users = userDAO.readAll();
        
        return Response.ok(users).build();
    }
}
