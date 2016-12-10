/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.order;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.magapp.main.R;

public class MainInfoFragment extends Fragment {

    public View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_info, null);
        String card_title = getArguments().getString("card_title");
        String ip_address = getArguments().getString("ip");
        String order_status = getArguments().getString("order_status");
        String store_name = getArguments().getString("store_name");
        String created_at = getArguments().getString("created_at");

        TextView OrderNum = (TextView) rootView.findViewById(R.id.title1);
        TextView CreatedAt = (TextView) rootView.findViewById(R.id.created_at);
        TextView Status = (TextView) rootView.findViewById(R.id.order_status);
        TextView Ip = (TextView) rootView.findViewById(R.id.ip);
        TextView Store = (TextView) rootView.findViewById(R.id.store);

        OrderNum.setText(card_title);
        CreatedAt.setText(created_at);
        Status.setText(order_status);
        Ip.setText(ip_address);
        Store.setText(store_name);

        return rootView;
    }

}
