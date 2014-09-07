package Login;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.magapp.R;

public class AccountsAdapter extends BaseAdapter  {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	int selectedIndex = -1;
	
	public AccountsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {     
        return data.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }
    
    public void remove(int index){
    	data.remove(index);
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null) {        	
            vi =  inflater.inflate(R.layout.accounts_list_item, null);                
        }     
        
        TextView account_name = (TextView)vi.findViewById(R.id.account_name); // title
        TextView account_url = (TextView)vi.findViewById(R.id.url); // artist name
      
 
        HashMap<String, String> account = new HashMap<String, String>();
        account = data.get(position);
 
        // Setting all values in listview
        account_name.setText(account.get("name"));
        account_url.setText(account.get("url"));
        
        RadioButton r = (RadioButton)vi.findViewById(R.id.radio_list);
        if(selectedIndex == position){
            	r.setChecked(true);
            }
            else{
            	r.setChecked(false);
            }
      
        return vi;
    }
     
    
}
