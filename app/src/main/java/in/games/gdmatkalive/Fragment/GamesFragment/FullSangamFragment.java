package in.games.gdmatkalive.Fragment.GamesFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Adapter.TableAdapter;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Model.TableModel;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.list_input_data.fullSangam;
import static in.games.gdmatkalive.Config.list_input_data.halfSangam;


public class FullSangamFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_selectDate;
TextView tv_date;
Dialog dialog;
TextView tv_date1,tv_date2,tv_date3,txtDate_id;
AutoCompleteTextView auto_openPanna,auto_closePanna;
EditText et_points;
Button btn_add,btn_submit;
Module module;
TextView tv;

String w_amount="";
ListView list_table;
TableAdapter tableAdaper;
List<TableModel> list;
SessionMangement sessionMangement;
String betdate="";
private String matka_id,e_time,s_time ,matka_name , game_id , game_name ,type = "open" ,game_date="",title="";

    public FullSangamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_sangam, container, false);
        initView(view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, fullSangam);
        auto_closePanna.setAdapter(arrayAdapter);
        auto_openPanna.setAdapter(arrayAdapter);


        return view;
    }

    private void initView(View view) {

        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        title = getArguments().getString("title");

        ((MainActivity)getActivity()).setTitles(title+" ("+matka_name+")");

        w_amount = ((MainActivity) getActivity()).getWallet();
        module = new Module(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        list=new ArrayList<>();
        list_table=view.findViewById(R.id.list_table);
       // lin_selectDate = view.findViewById(R.id.lin_selectDate);
        tv_date = view.findViewById(R.id.tv_date);
        auto_openPanna = view.findViewById(R.id.auto_openPanna);
        auto_closePanna = view.findViewById(R.id.auto_closePanna);
        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);
        btn_submit = view.findViewById(R.id.btn_submit);
//        tv_date.setOnClickListener(this);
        //lin_selectDate.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        module.getCurrentDate(tv_date);

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId())
        {
            case R.id.btn_add:
                onValidation();
                break;
            case R.id.tv_date:
                module.setDateDialog (dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
                break;
            case R.id.btn_submit:
                placedBid();
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    private void placedBid() {

        int er = list.size();
        if (er <= 0) {
            String message = "Please Add Some Bids";
            module.fieldRequired(message);
            return;
        } else {

            try {
                int amt = 0;
//                ArrayList list_digits = new ArrayList();
//                ArrayList list_type = new ArrayList();
//                ArrayList list_points = new ArrayList();
                int rows = list.size();

                for (int i = 0; i < rows; i++) {


                    TableModel tableModel = list.get(i);
//                    String asd = tableModel.getDigits();
//                    String d_all[] = asd.split("-");
//                    String d0 = d_all[0].toString();
//                    String d1 = d_all[1].toString();

                    String asd1 = tableModel.getPoints().toString();
//                    String asd2 = tableModel.getType().toString();
//                    int b = 1;
//                    if (asd2.equals("full sangam")) {
//                        b = 0;
//                    } else {
//                        b = 0;
//                    }

                    amt = amt + Integer.parseInt(asd1);

//                    char quotes = '"';
//                    list_digits.add(quotes + d0 + quotes);
//                    list_points.add(asd1);
//                    list_type.add(quotes + d1 + quotes);

                }


//                String id = sessionMangement.getUserDetails().get(KEY_ID);

                    String date = betdate.substring(0,10);

//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("points", list_points);
//                jsonObject.put("digits", list_digits);
//                jsonObject.put("bettype", list_type);
//                jsonObject.put("user_id", id);
//                jsonObject.put("matka_id", matka_id);
//                jsonObject.put("date", date);
//                jsonObject.put("game_id", game_id);
//
//                JSONArray jsonArray = new JSONArray();
//                jsonArray.put(jsonObject);

                int wallet_amount = Integer.parseInt(w_amount);
                if (wallet_amount < amt) {
                    module.errorToast("Insufficient Amount");
                } else {
                    module.setBidsDialog(Integer.parseInt(w_amount),list,matka_id,date,game_id,w_amount,matka_name,btn_submit,s_time,e_time);
                }
            } catch (Exception err) {
                err.printStackTrace();
                module.errorToast("Err" + err.getMessage());
            }

        }
    }

    private void onValidation() {
        int betType;
        betType=getBetType(getASandC(s_time,e_time));
        if (betType==1)
        {
            module.errorToast ("Bidding is closed for today !");
        }
        else {
            betdate = tv_date.getText ( ).toString ( );
            String openPanna = auto_openPanna.getText ( ).toString ( );
            String point = et_points.getText ( ).toString ( );
            String closePanna = auto_closePanna.getText ( ).toString ( );


            if (betdate.equalsIgnoreCase ("Select Date")) {
                module.fieldRequired ("Date Required");
            } else if (openPanna.isEmpty ( )) {

                auto_openPanna.setError ("Panna Required");
            } else if (!Arrays.asList (fullSangam).contains (openPanna)) {

                module.errorToast ("This is invalid panna");
                auto_openPanna.setText ("");
                auto_openPanna.requestFocus ( );
                return;

            } else if (closePanna.isEmpty ( )) {

                auto_closePanna.setError ("Panna Required");
            } else if (!Arrays.asList (fullSangam).contains (closePanna)) {
                module.errorToast ("This is invalid panna");
                auto_closePanna.setText ("");
                auto_closePanna.requestFocus ( );
                return;
            } else if (et_points.getText ( ).toString ( ).isEmpty ( )) {

                et_points.setError ("Point Required");
                et_points.requestFocus ( );
            } else {
                int points = Integer.parseInt (et_points.getText ( ).toString ( ).trim ( ));

                if (points < 10) {

                    et_points.setError ("Minimum Biding amount is 10");
                    et_points.requestFocus ( );
                    return;
                } else if (points > Integer.parseInt (w_amount)) {
                    module.errorToast ("Insufficient Amount");
                } else {
                    int num = 1;
                    for (int n = 0; n < list.size ( ); n++) {
                        num = num + 1;
                    }
                    String number = String.valueOf (num);
                    module.addData (number, "FULL SANGAM", openPanna + "-" + closePanna, point, "open", list, tableAdaper, list_table, btn_submit);
                    clearData ( );
                    auto_openPanna.requestFocus ( );
                }

            }
        }
        }
    private void clearData() {
        auto_openPanna.setText ("");
        auto_closePanna.setText ("");
        et_points.setText("");
    }
    public long[] getASandC(String startTime,String endTime){
        long[] tArr=new long[2];
        Date date=new Date();
        java.text.SimpleDateFormat sim=new java.text.SimpleDateFormat ("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat format = new java.text.SimpleDateFormat ("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;
        tArr[0]=as;
        tArr[1]=c;
        return tArr;
    }
    public int getBetType(long[] tArr) {
        // as<0 => open,close
        //c>0 =>nothing but biding closed
        //else=>close
        long as = tArr[0];
        long c = tArr[1];
        if (as < 0) {
            return 2;
        } else if (c > 0) {
            return 0;
        } else {
            return 1;
        }
    }
}