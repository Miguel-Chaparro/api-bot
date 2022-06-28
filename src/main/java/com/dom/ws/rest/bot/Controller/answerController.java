/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.answerDAO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Ch
 */
public class answerController {

    static final Logger log = Logger.getLogger(answerController.class.getName());

    public msgError updateCreateAnswers(answerDTO req) {
        log.info("*** Start answerController updateCreateAnswers ***");
        msgError response = new msgError();
        answerDAO dao = new answerDAO();
        int codeResp;
        String msjResponse = "";
        boolean statusUpdate;
        statusUpdate = dao.update(req);
        if (statusUpdate) {
            codeResp = 0;
            msjResponse = "Actualización Exitosa";
        } else {
            statusUpdate = dao.create(req);
            if (statusUpdate) {
                codeResp = 0;
                msjResponse = "Creación Exitosa";
            }else{
                codeResp = -1;
                msjResponse = "Upss Se presento un error en la creación de la respuesta";
            }
        }
        response.setCode(codeResp);
        response.setMessage(msjResponse);
        log.info("*** End answerController updateCreateAnswers ***");
        return response;
    }
}
