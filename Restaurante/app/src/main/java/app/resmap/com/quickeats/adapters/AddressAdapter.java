package app.resmap.com.quickeats.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import app.resmap.com.quickeats.R;
import app.resmap.com.quickeats.SharedPreference;
import app.resmap.com.quickeats.models.Account;
import app.resmap.com.quickeats.models.Address;

/**
 * Created by arwin on 3/19/17.
 */


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private Context mContext;
    private List<Address> accountList;
    private String data = "";
    private SharedPreference sharedPreference;
    private String password;

    public void setData(String data) {
        this.data = data;
    }

    public String getData(){
        return data;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, editValue;
        Button editBtn;
        CardView pwd;

        public MyViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.title);
            editValue = (TextView) view.findViewById(R.id.value);
            editBtn = (Button) view.findViewById(R.id.edit);

        }
    }


    public AddressAdapter(Context mContext, List<Address> accountList) {
        this.mContext = mContext;
        this.accountList = accountList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Address address = accountList.get(position);

        final String title = address.getNickname();
        final String value = address.getValue();
        //final String de = address.getValue();
        holder.txtTitle.setText(title);
        holder.editValue.setText(value);

        sharedPreference = new SharedPreference();


        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(title.equals("First Name")){
//
//                    showAlert(holder, "Firstname",
//                            AccountSettings.PREFS_FIRST_NAME, AccountSettings.PREFS_FIRST);
//
//                }else if(title.equals("Last Name")){
//
//                    showAlert(holder, "Firstname",
//                            AccountSettings.PREFS_LAST_NAME, AccountSettings.PREFS_LAST);
//                }else if(title.equals("Mobile")){
//
//                    showAlert(holder, "Mobile",
//                            AccountSettings.PREFS_MOBILE_NAME, AccountSettings.PREFS_MOBILE);
//
//                }else if(title.equals("Email")){
//
//                    showAlert(holder, "Email",
//                            AccountSettings.PREFS_EMAIL_NAME, AccountSettings.PREFS_EMAIL);
//
//                }else if(title.equals("New Password")){
//
//                    showPasswordAlert(holder,
//                            AccountSettings.PREFS_PASSWORD_NAME, AccountSettings.PREFS_PASSWORD);
//                }


            }


        });

    }


    @Override
    public int getItemCount() {
        return accountList.size();
    }
}


