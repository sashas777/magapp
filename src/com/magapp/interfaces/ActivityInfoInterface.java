/*
 * Copyright (c) 2015.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.interfaces;

import java.util.ArrayList;

public interface ActivityInfoInterface {

    ArrayList getComments();
    /*@todo make all tasks in activity*/
    void setComments(Object[] comments_obj);
    /*@todo make all tasks in activity and not use it for order*/
    void setOrderIncrementId(String order_increment_id_val);
}
