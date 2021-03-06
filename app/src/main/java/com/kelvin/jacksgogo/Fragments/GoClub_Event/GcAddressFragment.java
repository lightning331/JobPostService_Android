package com.kelvin.jacksgogo.Fragments.GoClub_Event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kelvin.jacksgogo.Activities.JGGMapViewActivity;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.GoClub_Event.JGGEventModel;
import com.kelvin.jacksgogo.Utils.Models.System.JGGAddressModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kelvin.jacksgogo.Utils.Global.APPOINTMENT_TYPE;
import static com.kelvin.jacksgogo.Utils.Global.EVENTS;
import static com.kelvin.jacksgogo.Utils.Global.REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GcAddressFragment extends Fragment implements TextWatcher {

    private Context mContext;

    @BindView(R.id.btn_share_location)              Button btnShareLocation;
    @BindView(R.id.btn_type_address)                Button btnTypeAddress;
    @BindView(R.id.btn_location)                    TextView btnLocation;

    @BindView(R.id.lbl_post_address_title)          TextView lblTitle;
    @BindView(R.id.ll_place)                        LinearLayout ll_place;
    @BindView(R.id.txt_post_address_place_name)     EditText txtPlaceName;
    @BindView(R.id.ll_unit)                         LinearLayout ll_unit;
    @BindView(R.id.txt_post_address_unit)           EditText txtUnit;
    @BindView(R.id.ll_street)                       LinearLayout ll_street;
    @BindView(R.id.txt_post_address_street)         EditText txtStreet;
    @BindView(R.id.ll_postcode)                     LinearLayout ll_postcode;
    @BindView(R.id.txt_post_address_postcode)       EditText txtPostCode;
    @BindView(R.id.ll_coordinate)                   LinearLayout ll_coordinate;
    @BindView(R.id.lbl_coordinate)                  TextView lblCoordinate;
    @BindView(R.id.btn_next)                        Button btnNext;

    private JGGEventModel creatingEvent;
    private JGGAddressModel mAddress;
    private ArrayList<JGGAddressModel> address = new ArrayList<>();

    private boolean isTypeAddress = false;

    private static final String TAG = GcAddressFragment.class.getSimpleName();
    public GcAddressFragment() {
        // Required empty public constructor
    }

    public static GcAddressFragment newInstance(String type) {
        GcAddressFragment fragment = new GcAddressFragment();
        Bundle args = new Bundle();
        args.putString(APPOINTMENT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gc_address, container, false);
        ButterKnife.bind(this, view);

        creatingEvent = JGGAppManager.getInstance().getSelectedEvent();
        address = creatingEvent.getAddress();

        initView();
        setData();

        return view;
    }

    private void initView() {
        btnLocation.setVisibility(View.GONE);

        ll_place.setVisibility(View.GONE);
        ll_unit.setVisibility(View.GONE);
        ll_street.setVisibility(View.GONE);
        ll_postcode.setVisibility(View.GONE);
        ll_coordinate.setVisibility(View.GONE);

        btnNext.setClickable(false);
        btnNext.setVisibility(View.GONE);

        txtUnit.addTextChangedListener(this);
        txtStreet.addTextChangedListener(this);
        txtPostCode.addTextChangedListener(this);
    }

    private void setData() {
        if (mAddress == null) {}
        else {
            isTypeAddress = false;
            onClickTypeAddress();
            mAddress = address.get(0);
            txtPlaceName.setText(mAddress.getPlaceName());
            txtUnit.setText(mAddress.getUnit());
            txtStreet.setText(mAddress.getStreet());
            txtPostCode.setText(mAddress.getPostalCode());
            lblCoordinate.setText(String.valueOf(mAddress.getLat()) + "° N, " + String.valueOf(mAddress.getLon()) + "° E");
        }
    }

    private void onYellowButtonColor(Button button) {
        button.setBackgroundResource(R.drawable.yellow_background);
        button.setTextColor(ContextCompat.getColor(mContext, R.color.JGGBlack));
    }

    private void onPurpleButtonColor(Button button) {
        button.setBackgroundResource(R.drawable.purple_border_background);
        button.setTextColor(ContextCompat.getColor(mContext, R.color.JGGPurple));
    }

    @OnClick(R.id.btn_share_location)
    public void onClickShareLocation() {
        listener.onNextButtonClick();
    }

    @OnClick(R.id.btn_type_address)
    public void onClickTypeAddress() {
        isTypeAddress = !isTypeAddress;
        if (isTypeAddress) {
            this.onYellowButtonColor(btnTypeAddress);
            btnShareLocation.setVisibility(View.GONE);
            btnLocation.setVisibility(View.VISIBLE);

            ll_place.setVisibility(View.VISIBLE);
            ll_unit.setVisibility(View.VISIBLE);
            ll_street.setVisibility(View.VISIBLE);
            ll_postcode.setVisibility(View.VISIBLE);
            ll_coordinate.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnShareLocation.setVisibility(View.VISIBLE);
            this.onPurpleButtonColor(btnTypeAddress);
            btnLocation.setVisibility(View.GONE);

            ll_place.setVisibility(View.GONE);
            ll_unit.setVisibility(View.GONE);
            ll_street.setVisibility(View.GONE);
            ll_postcode.setVisibility(View.GONE);
            ll_coordinate.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_location)
    public void onClickLocation() {
        Intent intent = new Intent(mContext, JGGMapViewActivity.class);
        intent.putExtra(APPOINTMENT_TYPE, EVENTS);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        mAddress.setPlaceName(txtPlaceName.getText().toString());
        mAddress.setUnit(txtUnit.getText().toString());
        mAddress.setStreet(txtStreet.getText().toString());
        mAddress.setPostalCode(txtPostCode.getText().toString());

        address.add(mAddress);

        creatingEvent.setAddress(address);
        JGGAppManager.getInstance().setSelectedEvent(creatingEvent);

        listener.onNextButtonClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Gson gson = new Gson();
                String result=data.getStringExtra("result");
                Log.d(TAG, result);
                mAddress = gson.fromJson(result, JGGAddressModel.class);
                setData();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (txtUnit.length() > 0
                && txtStreet.length() > 0
                && txtPostCode.length() > 0) {
            btnNext.setBackgroundResource(R.drawable.purple_background);
            btnNext.setTextColor(ContextCompat.getColor(mContext, R.color.JGGWhite));
            btnNext.setClickable(true);
        } else {
            btnNext.setBackgroundResource(R.drawable.grey_background);
            btnNext.setTextColor(ContextCompat.getColor(mContext, R.color.JGGGrey3));
            btnNext.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // TODO : Next Click Listener
    private GcTimeFragment.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onNextButtonClick();
    }

    public void setOnItemClickListener(GcTimeFragment.OnItemClickListener listener) {
        this.listener = listener;
    }

}
