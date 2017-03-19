package app.resmap.com.quickeats.adapters;

/**
 * Created by arwin on 3/18/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.resmap.com.quickeats.R;
import app.resmap.com.quickeats.SharedPreference;
import app.resmap.com.quickeats.activity.AccountSettings;
import app.resmap.com.quickeats.models.Account;
import app.resmap.com.quickeats.models.Validate;


public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Account> accountList;
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


    public SettingsAdapter(Context mContext, List<Account> accountList) {
        this.mContext = mContext;
        this.accountList = accountList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Account account = accountList.get(position);

        final String title = account.getTitle();
        final String value = account.getValue();
        holder.txtTitle.setText(title);
        holder.editValue.setText(value);

        sharedPreference = new SharedPreference();


        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(title.equals("First Name")){

                   showAlert(holder, "Firstname",
                            AccountSettings.PREFS_FIRST_NAME, AccountSettings.PREFS_FIRST);

                }else if(title.equals("Last Name")){

                    showAlert(holder, "Firstname",
                            AccountSettings.PREFS_LAST_NAME, AccountSettings.PREFS_LAST);
                }else if(title.equals("Mobile")){

                    showAlert(holder, "Mobile",
                            AccountSettings.PREFS_MOBILE_NAME, AccountSettings.PREFS_MOBILE);

                }else if(title.equals("Email")){

                    showAlert(holder, "Email",
                            AccountSettings.PREFS_EMAIL_NAME, AccountSettings.PREFS_EMAIL);

                }else if(title.equals("New Password")){

                    showPasswordAlert(holder,
                            AccountSettings.PREFS_PASSWORD_NAME, AccountSettings.PREFS_PASSWORD);
                }


            }


        });

    }

    private String showPasswordAlert(final MyViewHolder holder, final String prefName, final String prefValue){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        alertDialog.setView(dialogView);

        final EditText editNew = (EditText) dialogView.findViewById(R.id.passwordNewDialog);
        final EditText editPass = (EditText) dialogView.findViewById(R.id.passwordConf);

        final Validate validate = new Validate();

        // On pressing Settings button
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int which) {

                boolean checkPassword = validate.password(editPass.getText().toString(), editNew.getText().toString());

                if(!checkPassword){
                    Toast.makeText(mContext, "Password mismatch", Toast.LENGTH_SHORT).show();
                }else {
                    password = editPass.getText().toString();

                    sharedPreference.removeValue(mContext, prefName, prefValue);
                    sharedPreference.save(mContext, prefName, prefValue, password);

                    holder.editValue.setText(password);
                }

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
        return password;
    }

    private String showAlert(final MyViewHolder holder, String title, final String prefName, final String prefValue){


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_edit, null);
        alertDialog.setView(dialogView);

        final TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        final EditText edit = (EditText) dialogView.findViewById(R.id.commonDialog);

        txtTitle.setText(title);

        // On pressing Settings button
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                String myValue = edit.getText().toString();

                sharedPreference.removeValue(mContext, prefName, prefValue);
                sharedPreference.save(mContext, prefName, prefValue, myValue);

                setData(myValue);

                holder.editValue.setText(myValue);

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


        return getData();

    }


    @Override
    public int getItemCount() {
        return accountList.size();
    }
}

