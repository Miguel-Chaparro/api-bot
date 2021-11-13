/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Conexion;

import java.util.List;

/**
 *
 * @author MIGUEL
 * @param <objetoDTO>
 */
public interface interfaces <objetoDTO> {

    public boolean create(objetoDTO dto);

    public boolean update(objetoDTO dto);

    public boolean delete(objetoDTO dto);

    public objetoDTO readOne(objetoDTO dto);

    public List<objetoDTO> readMany(objetoDTO dto);

    public List<objetoDTO> readAll();

}
