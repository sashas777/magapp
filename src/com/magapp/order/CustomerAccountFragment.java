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

public class CustomerAccountFragment extends Fragment {

    public View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_account_info, null);
        String customer_name = getArguments().getString("customer_name");
        String customer_email = getArguments().getString("customer_email");
        String customer_group = getArguments().getString("customer_group");

        TextView CustomerGroup = (TextView) rootView.findViewById(R.id.customer_group);
        TextView CustomerName = (TextView) rootView.findViewById(R.id.customer_name);
        TextView Email = (TextView) rootView.findViewById(R.id.customer_email);

        CustomerName.setText(customer_name);
        Email.setText(customer_email);
        CustomerGroup.setText(customer_group);

        return rootView;
    }

}
