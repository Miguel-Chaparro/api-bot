package com.dom.ws.rest.bot.Controller;

import java.util.logging.Logger;

import com.dom.ws.rest.bot.DAO.recordSurveyDAO;
import com.dom.ws.rest.bot.DTO.customerWhatsappDTO;
import com.dom.ws.rest.bot.DTO.recordSurveyDTO;
import com.dom.ws.rest.bot.vo.msgError;

public class recordSurveyController {
    static final Logger logger = Logger.getLogger(recordSurveyController.class.getName());

    // crea un metodo para guardar la respuesta de la encuesta y retorne un objeto
    // de tipo recordSurveyDTO

    public recordSurveyDTO createRecordSurvey(recordSurveyDTO req) {
        logger.info("***start recordSurveyController createRecordSurvey***");
        recordSurveyDTO dto, response = new recordSurveyDTO();
        recordSurveyDAO dao = new recordSurveyDAO();
        msgError error = new msgError();
        boolean statusCreate = false, statusError = false;
        statusCreate = dao.create(req);

        if (statusCreate) {
            dto = req;
            response = dao.readOne(dto);
        } else if (!statusError) {
            error.setCode(-10);
            error.setMessage("Upss... No fue posible crear el proyecto, Favor contacta el Administrador");
            response.setError(error);
        }
        logger.info("***end recordSurveyController createRecordSurvey***");
        return response;
    }

    // crea un metodo para convertir el objeto de entrada questionDTO y CustomerWhatsappDto, retorne un objeto de tipo recordSurveyDTO
    
    public recordSurveyDTO convertQuestionDTOToRecordSurveyDTO(customerWhatsappDTO customer, int option, String openAnswer) {
        logger.info("***start recordSurveyController convertQuestionDTOToRecordSurveyDTO***");
        recordSurveyDTO response = new recordSurveyDTO();
        response.setAnswer(option);
        response.setIdFrom(customer.getIdFrom());
        response.setIdQuestion(customer.getIdQuestions());
        response.setIdProject(customer.getIdProject());
        response.setOpenAnswer(openAnswer);
        response.setMultiAnswer("");
        response.setIdWhastapp(customer.getIdWhatsapp());
        logger.info("***end recordSurveyController convertQuestionDTOToRecordSurveyDTO***");
        return response;
    }





}
