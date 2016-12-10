/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
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
