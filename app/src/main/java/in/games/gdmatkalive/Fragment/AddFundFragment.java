package in.games.gdmatkalive.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import in.games.gdmatkalive.Activity.MainActivity;
import in.games.gdmatkalive.Config.Module;
import in.games.gdmatkalive.Interfaces.GetAppSettingData;
import in.games.gdmatkalive.Model.IndexResponse;
import in.games.gdmatkalive.R;
import in.games.gdmatkalive.Util.ConnectivityReceiver;
import in.games.gdmatkalive.Util.LoadingBar;
import in.games.gdmatkalive.Util.SessionMangement;

import static in.games.gdmatkalive.Config.BaseUrls.URL_GET_GATEWAY;
import static in.games.gdmatkalive.Config.BaseUrls.URL_REQUEST;
import static in.games.gdmatkalive.Config.Constants.KEY_EMAIL;
import static in.games.gdmatkalive.Config.Constants.KEY_ID;
import static in.games.gdmatkalive.Config.Constants.KEY_MOBILE;
import static in.games.gdmatkalive.Config.Constants.KEY_NAME;

//
public class AddFundFragment extends Fragment implements View.OnClickListener, PaymentResultListener {
SwipeRefreshLayout swipe;
CircleImageView civ_logo;
TextView tv_message,tv_whatsapp,tv_wallet;
EditText et_points;
LinearLayout lin_whatsapp;
Button btn_add;
SessionMangement sessionMangement;
Module module;
String pnts,wallet_amt="",withdr_no="",minAmount="";
LoadingBar loadingBar;
String themeColor,desc,imageUrl,requestStatus,gatewayStatus;
    public AddFundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_fund, container, false);
        ((MainActivity)getActivity()).setTitles("Add Fund");
        wallet_amt = ((MainActivity)getActivity()).getWallet();
        initView(root);
        getGatewaySetting();
        return root;
    }

    private void initView(View root) {

        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        swipe = root.findViewById(R.id.swipe);
        civ_logo = root.findViewById(R.id.civ_logo);
        tv_message = root.findViewById(R.id.tv_message);
        tv_whatsapp = root.findViewById(R.id.tv_whatsapp);
        tv_wallet = root.findViewById(R.id.tv_wallet);
        et_points = root.findViewById(R.id.et_points);
        lin_whatsapp = root.findViewById(R.id.lin_whatsapp);
        btn_add = root.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(this);
        lin_whatsapp.setOnClickListener(this);
        tv_wallet.setText(wallet_amt);
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                tv_whatsapp.setText(model.getWithdraw_no());
                withdr_no =model.getWithdraw_no();
                minAmount = model.getMin_amount();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_add:
                onValidatingData();
                break;
            case R.id.lin_whatsapp:
                module.whatsapp(withdr_no,"Hello! Admin ");
                break;
        }
    }

    private void onValidatingData(){

        int points=0;

            if(TextUtils.isEmpty(et_points.getText().toString()))
            {
                et_points.setError("Enter Some Points");
                return;
            }
            else
            {
                points=Integer.parseInt(et_points.getText().toString().trim());

                if(points<Integer.parseInt(minAmount))
                {
                    module.fieldRequired("Minimum Range for points is "+ minAmount);
                }
                else
                {
                    String user_id = sessionMangement.getUserDetails().get(KEY_ID);
                    pnts=String.valueOf(points);
                    if (ConnectivityReceiver.isConnected()) {
                        if(gatewayStatus.equals("0")){
                            saveInfoIntoDatabase(user_id, pnts, requestStatus, "Add","");
                        }else if(gatewayStatus.equals("1")){
                            startPayment(2);
                        }

                    } else
                    {
                       module.noInternet();
                    }
                }
            }
        }
    private void saveInfoIntoDatabase(final String user_id, final String points, final String st,String type,String trans_id) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("points",points);
        params.put("request_status",st);
        params.put("type",type);
        params.put("trans_id",trans_id);
        params.put("w_type","");

        Log.e("TAG", "saveInfoIntoDatabase: "+params.toString() );
        module.postRequest(URL_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("add_fund",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp=obj.getBoolean("responce");
                    if(resp)
                    {
                        module.successToast(""+obj.getString("message"));
                        loadingBar.dismiss();
                        Intent i = new Intent(getActivity(),MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        module.errorToast(""+obj.getString("error"));
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
      }
    public void getGatewaySetting(){
        HashMap<String,String> params=new HashMap<>();

        module.postRequest(URL_GET_GATEWAY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("gatway",response.toString());
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject object = arr.getJSONObject(0);
                    imageUrl=object.getString("icon");
                    themeColor=object.getString("theme_color");
                    desc=object.getString("description");
                    requestStatus=object.getString("request_status");
                    gatewayStatus=object.getString("gateway_status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
            }
        });

        }


    public void startPayment(int amt) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
       //checkout.setKeyID("rzp_live_s7AZi7HtHIO5Ff");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.app_logo);

        /**
         * Reference to current activity
         */
        Activity activity=getActivity();
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            options.put("name", sessionMangement.getUserDetails().get(KEY_NAME));
            options.put("description", desc);
            options.put("image", imageUrl);
//            options.put("order_id", "order_DBJOWzybf0sJbdsds");//from response of step 3.
            options.put("theme.color", themeColor);
            options.put("currency", "INR");
            options.put("amount", (amt*100));//pass amount in currency subunits
//            options.put("amount", (500));//pass amount in currency subunits
            options.put("prefill.email", sessionMangement.getUserDetails().get(KEY_EMAIL));
            options.put("prefill.contact",sessionMangement.getUserDetails().get(KEY_MOBILE));
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout"+ e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("TAG", "onPaymentSuccess: "+s.toString() );
//        common.showToast("Payment Success");
        saveInfoIntoDatabase(sessionMangement.getUserDetails().get(KEY_ID),pnts,requestStatus,"add","");
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "onPaymentError: "+s );
        module.errorToast("Payment Failed. Try again later");
    }

}