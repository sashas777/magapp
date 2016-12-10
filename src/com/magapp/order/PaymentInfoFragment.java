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

public class PaymentInfoFragment extends Fragment {

    public View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_payment_info, null);
        String payment_info = getArguments().getString("payment_info");
        String currency_info = getArguments().getString("currency_info");

        TextView PaymentInfo = (TextView) rootView.findViewById(R.id.payment_info);
        TextView CurrencyInfo = (TextView) rootView.findViewById(R.id.currency_info);

        PaymentInfo.setText(payment_info);
        CurrencyInfo.setText(currency_info);

        return rootView;
    }

}
