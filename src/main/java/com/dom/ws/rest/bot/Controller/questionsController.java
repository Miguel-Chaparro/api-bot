/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.answerDAO;
import com.dom.ws.rest.bot.DAO.customerWhatsappDAO;
import com.dom.ws.rest.bot.DAO.questionsDAO;
import com.dom.ws.rest.bot.DAO.raspiDAO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.customerWhatsappDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.DTO.raspiDTO;
import com.dom.ws.rest.bot.DTO.recordSurveyDTO;
import com.dom.ws.rest.bot.Request.questionsAnswersReq;
import com.dom.ws.rest.bot.Response.answerResp;
import com.dom.ws.rest.bot.vo.msgError;
import com.dom.ws.rest.bot.vo.questionsVO;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
        projectController project = new projectController();
        customerWhatsappDTO dto = new customerWhatsappDTO();
        answerResp statesResp = new answerResp();
        questionsVO question = new questionsVO();
        dto = whatsappData(req.getWhatsappId(), req.getIdFrom());

        switch (dto.getError().getCode()) {
            case 1:
                int projectDefaultid = project.getProjectIdDefault(req.getIdFrom());
                dto.setIdQuestions("1");
                dto.setIdWhatsapp(req.getWhatsappId());
                dto.setIdFrom(req.getIdFrom());
                dto.setIdCustomer("");
                dto.setName("");
                dto.setIdProject(projectDefaultid);
                dto.setPendingState(0);
                statesResp.setError(updateQuestionCustomer(dto, true));
                question = buildNextQuestion(dto, 1, true, false, false, new msgError(0, ""));
                statesResp.setQuestion(question);
                break;
            case 0:
                statesResp = response(dto, req.getAnswer());
                error = statesResp.getError();
                break;
            default:
                error.setCode(-1);
                error.setMessage(
                        "🤖Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
                break;
        }
        
        statesResp.setError(error);
        statesResp.setWhatsapp(req.getWhatsappId());
        logg.log(Level.INFO, "{0}*** End questionsController whatsappData ***", statesResp.getError().getMessage());
        logg.info("*** End questionsController whatsappData ***");
        return statesResp;
    }

    private customerWhatsappDTO whatsappData(String Whatsapp, String idFrom) {
        logg.info("*** Start questionsController whatsappData ***");
        customerWhatsappDAO customerWap = new customerWhatsappDAO();
        customerWhatsappDTO reqDTO = new customerWhatsappDTO();
        customerWhatsappDTO respDTO = new customerWhatsappDTO();
        reqDTO.setIdWhatsapp(Whatsapp);
        reqDTO.setIdFrom(idFrom);
        respDTO = customerWap.readOne(reqDTO);
        logg.info("*** End questionsController whatsappData ***");
        return respDTO;

    }

    private answerResp response(customerWhatsappDTO req, String option) {
        logg.info("*** Start questionsController response ***");
        List<answerDTO> answerList = new ArrayList<>();
        answerDTO val = new answerDTO();
        answerResp resp = new answerResp();
        questionsVO question = new questionsVO();
        Date date = new Date();
        List<raspiDTO> listRaspiDTO = new ArrayList<>();
        raspiDTO dto = new raspiDTO();
        boolean flg = false;
        // sumale 3 horas a la fecha actual
        date.setTime(date.getTime() - (1000 * 60 * 60 * 3));
        Timestamp ts = new Timestamp(date.getTime());
        logg.info("Fecha actual: " + ts.getTime() + " Fecha de la ultima pregunta: " + ts);
        logg.info("Fecha req: " + req.getDate().getTime() + " Fecha de la ultima pregunta: " + req.getDate());
        if (req.getDate() == null || req.getDate().getTime() < ts.getTime()) {
            logg.info("Valida si la fecha es mayor a la actual");
            val.setError(new msgError(2, ""));
            if (req.getFlg_devices() > 0 && req.getPendingState() == 3) {
                req.setIdQuestions("0");
                req.setPendingState(2);
            } else if (req.getPendingState() != 2) {
                req.setIdQuestions("1");
                option = "0";
                flg = true;
            }
            updateQuestionCustomer(req, false);
        }
        if (req.getFlg_devices() > 0 && req.getPendingState() == 2 && req.getIdQuestions().equals("0")) {
            logg.info("Valida si el usuario tiene dispositivos");
            listRaspiDTO = readRaspi(req.getIdWhatsapp());
            answerList = convertListRaspiDTOtoListAnswerDTO(listRaspiDTO);
            req.setPendingState(3);
            updateQuestionCustomer(req, false);
            val.setError(new msgError(0, "OK"));
            question.setQuestionDesc(
                    "🤖 ¡Hola *" + req.getName()
                            + "!* Estos son los dispositivos que tienes registrados en el sistema, Digita el número de uno de ellos para continuar ...");
            question.setOptions(answerList);
        } else if (req.getFlg_devices() > 0 && req.getPendingState() == 3 && req.getIdQuestions().equals("0")) {
            logg.info("Valida si el usuario tiene dispositivos y selecciono correctamente");
            dto = compareDevices(req.getIdWhatsapp(), option);
            logg.info("Respuesta de compareDevices: " + dto.toString());
            if (dto.getError().getCode() == 0) {
                logg.info("Valida si el usuario tiene dispositivos y selecciono correctamente");
                req.setIdQuestions("1");
                req.setPendingState(2);
                req.setDevices(dto.getRaspi());
                resp.setRaspi(dto.getRaspi());
                val.setError(new msgError(0, "OK"));
                updateQuestionCustomer(req, false);
                question = buildNextQuestion(req, -1, false, flg, true, new msgError(3, ""));
            } else {
                val.setError(new msgError(0, ""));
                logg.info("Valida si el usuario tiene dispositivos y selecciono incorrectamente");
                listRaspiDTO = readRaspi(req.getIdWhatsapp());
                answerList = convertListRaspiDTOtoListAnswerDTO(listRaspiDTO);
                question.setQuestionDesc(dto.getError().getMessage());
                question.setOptions(answerList);
            }
        } else {
            answerList = validateOptions(req, option);
            question.setOptions(answerList);
            val = answerList.get(0);
            resp.setCommand(val.getError().getMessage());
            if (val.getError().getCode() == 1 && req.getIdQuestions().length() < 2) {
                req.setIdQuestions("1.");
                question = buildNextQuestion(req, val.getAnswerId(), false, flg, false, val.getError());
            } else if (val.getError().getCode() == 1 && req.getPendingState() == 2) {
                listRaspiDTO = readRaspi(req.getIdWhatsapp());
                answerList = convertListRaspiDTOtoListAnswerDTO(listRaspiDTO);
                req.setPendingState(3);
                req.setIdQuestions("0");
                updateQuestionCustomer(req, false);
                resp.setError(new msgError(0, "OK"));
                question.setQuestionDesc(
                        "🤖 ¡Hola *" + req.getName()
                                + "!* Estos son los dispositivos que tienes registrados en el sistema, Digita el número de uno de ellos para continuar ...");
                question.setOptions(answerList);
            } else {
                switch (val.getError().getCode()) {
                    case 0:
                        question = buildNextQuestion(req, val.getAnswerId(), false, flg, false, val.getError());
                        break;
                    case 1:
                        question = buildNextQuestion(req, val.getAnswerId(), false, flg, false, val.getError());
                        break;
                    case 2:
                        resp.setRaspi(req.getDevices());
                        resp.setCommand(val.getError().getMessage());
                        resp.setFlgCommand("1");
                        question.setQuestionDesc(
                                "🤖 *" + req.getName()
                                        + "!* Nos estamos Comunicando con el Dispositivo *" + req.getDevices()
                                        + "*, favor espera un momento mientras *"
                                        + question.getOptions().get(0).getAnswerDesc() + "* ...");
                        List<answerDTO> listOption = new ArrayList<answerDTO>();
                        listOption.add(question.getOptions().get(0));
                        question.setOptions(listOption);

                        break;
                    case 3:
                        resp.setRaspi(req.getDevices());
                        question = buildNextQuestion(req, val.getAnswerId(), false, flg, true, val.getError());
                        break;
                    case 4:
                        question.setQuestionDesc(val.getError().getMessage());
                        break;
                    case 5:
                        question = buildNextQuestion(req, val.getAnswerId(), false, flg, false, val.getError());
                        break;
                    case 6:
                        resp.setError(new msgError(6, "OK"));
                        question.setQuestionDesc(
                                "🤖 *" + req.getName()
                                        + "!* Gracias por utilizar nuestro servicio, En estos momentos un asesor estará atendiendo a tu solicitud ... Pronto te contactaremos");
                        break;

                    default:
                        resp.setError(new msgError(7, "Error"));
                        question.setQuestionDesc(
                                "🤖 *" + req.getName()
                                        + "!* Lamentamos informarte que no hemos podido procesar tu solicitud, favor intenta de nuevo ...");
                        break;
                }

            }

        }
        
        recordSurvey(req, option);

        resp.setQuestion(question);
        resp.setError(val.getError());
        // val = answerList.get(0);
        logg.info("question: " + question.toString());
        logg.info("Respuesta: " + resp.toString());
        logg.info("*** End questionsController Response ***");
        return resp;
    }

    private void recordSurvey(customerWhatsappDTO customer, String dialog){
        logg.info("*** Start questionsController recordSurvey ***");
        // tratar de convertir el dialogo en un int, si no es posible, se envia -1 en una variable int
        int option = -1;
        try {
            option = Integer.parseInt(dialog);
            dialog = "";
        } catch (NumberFormatException e) {
            logg.info("Error al convertir el dialogo en un int");
        }
        recordSurveyController record = new recordSurveyController();
        recordSurveyDTO recordDto = new recordSurveyDTO();
        recordDto = record.convertQuestionDTOToRecordSurveyDTO(customer, option ,dialog);
        record.createRecordSurvey(recordDto);
        logg.info("*** End questionsController recordSurvey ***");
    }

    private questionsVO buildNextQuestion(customerWhatsappDTO req, int option, boolean newRegister, boolean flagBack,
            boolean flagDevice,
            msgError error) {
        logg.info("*** Start questionsController buildNextQuestion ***");
        logg.log(Level.INFO, "{0}***  buildNextQuestion FIRST***\n---", req.getIdQuestions());
        questionsVO response = new questionsVO();
        questionsDAO dao = new questionsDAO();
        questionsDTO dto = new questionsDTO();
        questionsDTO questionDto = new questionsDTO();
        List<answerDTO> answerList = new ArrayList<>();
        answerDTO optionDto = new answerDTO();
        answerDAO ansdao = new answerDAO();
        String questionId = "";
        if (req.getIdQuestions() == "0") {
            logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "Opción 1: " + req.getIdQuestions());
            dto.setIdQuestions("1");
        } else {
            logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "Opción 2: " + req.getIdQuestions());
            dto.setIdQuestions(req.getIdQuestions());
        }
        optionDto.setAnswerId(0);
        dto.setIdFrom(req.getIdFrom());
        dto.setIdProject(req.getIdProject());
        questionDto = dao.readOne(dto);
        //Guardar aca
      
        logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "Opción: " + req.getIdQuestions());
        if (option == 0) {
            questionId = req.getIdQuestions().substring(0, req.getIdQuestions().length() - 2);
        } else {
            if (newRegister) {
                questionId = option + "";
            } else if (questionDto.getNextQuestion() == 0 && option != 0 && !flagDevice) {
                questionId = req.getIdQuestions() + "." + option;
            } else if (questionDto.getNextQuestion() > 0) {
                questionId = questionDto.getNextQuestion() + "";
            } else {
                questionId = req.getIdQuestions();
            }
        }
        if (questionId == null || questionId.equals("")) {
            optionDto.setIdQuestion("1");
            optionDto.setIdProject(req.getIdProject());
            optionDto.setAnswerId(0);
            optionDto.setIdFrom(req.getIdFrom());
            answerList = ansdao.readMany(optionDto);
            req.setIdQuestions("1");
            response.setIdQuestion("1");
            if (!flagBack) {
                response.setQuestionDesc(
                        " *" + req.getName() + "!* Para continuar debes seleccionar algunas de estas opciones:");
            } else {
                response.setQuestionDesc(message(req.getName(), questionDto));
            }

        } else {
            optionDto.setIdQuestion(questionId);
            optionDto.setIdProject(req.getIdProject());
            optionDto.setAnswerId(0);
            optionDto.setIdFrom(req.getIdFrom());
            dto.setIdQuestions(questionId);
            dto.setIdProject(req.getIdProject());
            dto.setIdFrom(req.getIdFrom());
            answerList = ansdao.readMany(optionDto);
            questionDto = dao.readOne(dto);
            response.setQuestionDesc(message(req.getName(), questionDto));
            response.setIdQuestion(questionId);
            req.setIdQuestions(questionId);
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
        // Aca va el siwtch case para validar las preguntas
        logg.log(Level.INFO, "{0}***  Message 2***\n", "preview: -" + preview.getIdQuestions() + "-");

        if (null != question) {
            if (question.split("##").length > 1) {
                logg.log(Level.INFO, "{0}***  Message 2 If true***\n", "preview: " + preview.getIdQuestions());
                question = question.replaceAll("##", data);
            }

        } else {
            question = "🤖Lo siento, encontraste un fallo,  --- Se esta cargando nuevamente *La Matrix*... \n No es facíl";
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
            response.setMessage(
                    "🤖Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
        }
        logg.info("*** End questionsController updateQuestionCustomer ***");
        return response;

    }

    private List<answerDTO> validateOptions(customerWhatsappDTO dto, String option) {
        logg.info("*** Start questionsController validateOptions ***");
        answerDAO dao = new answerDAO();
        answerDTO req = new answerDTO();
        List<answerDTO> answerList = new ArrayList<>();
        req.setIdQuestion(dto.getIdQuestions());
        req.setAnswerId(0);
        req.setIdProject(dto.getIdProject());
        req.setIdFrom(dto.getIdFrom());
        msgError rta = new msgError();
        answerList = dao.readMany(req);
        int indicator = -1;
        String command = "", pending = "";
        logg.info("*** dto ***" + dto.getIdWhatsapp());
        if (!"".equals(dto.getCommand())) {
            command = dto.getCommand() + "-";
        }
        if (dto.getPendingDescription() != "") {
            pending = dto.getPendingDescription() + " ";
        }
        try {
            indicator = Integer.parseInt(option);
            rta.setCode(-1);
            rta.setMessage(
                    "🤖! *" + dto.getName()
                            + "!* La opción ingresada no es valida, Estoy aprendiendo día a día con el fin de garantizar un mejor servicio");
            if (indicator == 0) {
                logg.info("*** INDICADOR 0 ***");

                rta.setCode(1);
                rta.setMessage("Back");
            } else {
                for (answerDTO q : answerList) {
                    logg.log(Level.INFO, "{0}\n", "for: " + q.getAnswerId() + "\n" + indicator);
                    if (q.getAnswerId() == indicator) {
                        rta.setCode(0);
                        logg.log(Level.INFO, "{0}\n", "for - IF: " + q.getIdQuestion());
                        if (q.getFlgEnd() == 1 && q.getFlgCommand() == 1) {
                            rta.setCode(2);
                            rta.setMessage(command + q.getCommand());
                            dto.setCommand("");
                            dto.setIdQuestions("0");
                            dto.setPendingState(2);
                            updateQuestionCustomer(dto, false);
                        } else if (q.getFlgEnd() == 0 && q.getFlgCommand() == 1) {
                            rta.setCode(3);
                            rta.setMessage("");
                            dto.setCommand(command + q.getCommand());
                            updateQuestionCustomer(dto, false);
                        } else if (q.getFlgEnd() == 1 && q.getFlgCommand() == 0) {
                            rta.setCode(4);
                            dto.setIdQuestions("1");
                            updateQuestionCustomer(dto, false);
                            rta.setMessage(
                                    "🤖 *" + dto.getName()
                                            + "!* Gracias por utilizar nuestros servicios!, Recurda que puedes escribinos en cualquier momento... 🤗🤗🤗");
                        } else if (q.getFlgEnd() == 2) {
                            rta.setCode(5);
                            rta.setMessage("");
                            dto.setPendingDescription(pending + q.getAnswerDesc());
                            updateQuestionCustomer(dto, false);
                        } else if (q.getFlgEnd() == 3) {
                            rta.setCode(6);
                            rta.setMessage(pending + q.getAnswerDesc());
                            dto.setPendingDescription("");
                            dto.setIdQuestions("1");
                            dto.setPendingState(0);
                            updateQuestionCustomer(dto, false);
                        } else {
                            rta.setCode(0);
                            rta.setMessage("");
                        }
                        req = q;
                        break;
                    }

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
                rta.setMessage(
                        "🤖! *" + dto.getName() + "!* Estoy aprendiendo a leer... por favor ingresa solo digitos");
            }

        }
        /*
         * if (!flgOK) {
         * rta.setCode(-1);
         * rta.
         * setMessage("🤖Ups! Lo siento esta opción ingresada no es valida, Estoy aprendiendo día a día con el fin de garantizar un mejor servicio"
         * );
         * } else {
         * String updateQuestion = dto.getIdQuestions() + "." + option;
         * req.setIdQuestion(updateQuestion);
         * dto.setIdQuestions(updateQuestion);
         * updateQuestionCustomer(dto, false);
         * 
         * }
         */
        req.setAnswerId(indicator);
        req.setError(rta);
        answerList.add(0, req);
        logg.info("*** End questionsController validaOptions ***");
        return answerList;

    }

    private raspiDTO compareDevices(String idChat, String idRaspi) {
        logg.info("*** Start questionsController compareDevices ***");

        raspiDTO req = new raspiDTO();
        int idDevice;
        List<raspiDTO> raspiList = new ArrayList<>();
        try {
            idDevice = Integer.parseInt(idRaspi);
            req.setIdDevices(idDevice);
            raspiList = readRaspi(idChat);
            req.setError(new msgError(1,
                    "🤖Ups! Lo siento esta opción ingresada no es valida --- _Elige tu opción ingresando el identificador númerico (Sólo dígitos)_"));
            for (raspiDTO r : raspiList) {
                if (r.getIdDevices() == idDevice) {

                    req = r;
                    req.setError(new msgError(0, "Ok"));
                    logg.info("*** Dispositivo Elegido = " + r.getIdDevices() + " nombre: " + r.getRaspi() + " ***");
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            logg.info("*** Error: " + ex.getMessage() + " ***");
            req.setError(new msgError(-1,
                    "🤖 Ups!  Aún no reconozco textos, por favor ingresa solo digitos --- _Elige tu opción ingresando el identificador númerico (Sólo dígitos)_"));
        }
        logg.info("*** End questionsController compareDevices ***");
        return req;
    }

    private List<raspiDTO> readRaspi(String idChat) {
        logg.info("*** Start questionsController readRaspi ***");
        raspiDAO dao = new raspiDAO();
        raspiDTO req = new raspiDTO();
        req.setIdChat(idChat);
        List<raspiDTO> raspiList = new ArrayList<>();
        raspiList = dao.readMany(req);
        logg.info("*** End questionsController readRaspi ***");
        return raspiList;
    }

    private List<answerDTO> convertListRaspiDTOtoListAnswerDTO(List<raspiDTO> listRaspiDTO) {
        logg.info("*** Start questionsController convertListRaspiDTOtoListAnswerDTO ***");
        List<answerDTO> listAnswerDTO = new ArrayList<>();
        for (raspiDTO raspiDTO : listRaspiDTO) {
            answerDTO answerDTO = new answerDTO();
            answerDTO.setAnswerId(raspiDTO.getIdDevices());
            answerDTO.setAnswerDesc(raspiDTO.getRaspi());
            listAnswerDTO.add(answerDTO);
        }
        logg.info("" + listAnswerDTO.size());
        logg.info("*** End questionsController convertListRaspiDTOtoListAnswerDTO ***");
        return listAnswerDTO;
    }
}
