/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ATDataGrid.servlets;

import java.util.Map;

/**
 *
 * @author GB001894
 */
public class RequestRecord {
    String operation;
    String uid_reference;
    Map<String, Object> uniqueKey;
    Map<String, Object> record;
}
