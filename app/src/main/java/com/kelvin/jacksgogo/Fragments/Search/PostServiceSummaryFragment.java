package com.kelvin.jacksgogo.Fragments.Search;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelvin.jacksgogo.Activities.Search.PostedServiceActivity;
import com.kelvin.jacksgogo.CustomView.Views.JGGAlertView;
import com.kelvin.jacksgogo.CustomView.Views.PostServiceTabView;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Utils.API.JGGAPIManager;
import com.kelvin.jacksgogo.Utils.API.JGGURLManager;
import com.kelvin.jacksgogo.Utils.Global;
import com.kelvin.jacksgogo.Utils.JGGAppManager;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGAppointmentModel;
import com.kelvin.jacksgogo.Utils.Responses.JGGPostAppResponse;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import co.lujun.androidtagview.TagContainerLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kelvin.jacksgogo.Fragments.Search.PostServiceSummaryFragment.PostEditStatus.EDIT;
import static com.kelvin.jacksgogo.Fragments.Search.PostServiceSummaryFragment.PostEditStatus.POST;
import static com.kelvin.jacksgogo.Utils.Global.JGGBudgetType.fixed;
import static com.kelvin.jacksgogo.Utils.Global.JGGBudgetType.from;
import static com.kelvin.jacksgogo.Utils.JGGTimeManager.appointmentNewDate;

public class PostServiceSummaryFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private OnFragmentInteractionListener mListener;

    private ImageView imgCategory;
    private TextView lblCategory;
    private LinearLayout btnDescribe;
    private LinearLayout btnPrice;
    private LinearLayout btnTimeSlot;
    private LinearLayout btnAddress;
    private LinearLayout btnPostService;
    private TextView lblPostService;
    private TextView lblDescribeTitle;
    private TextView lblDescribeDesc;
    private TextView lblPrice;
    private TextView lblTimeSlot;
    private TextView lblAddress;
    private TagContainerLayout describeTagView;
    private AlertDialog alertDialog;

    private PostEditStatus editStatus;
    private String postedServiceID;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private ArrayList<String> attachmentURLs = new ArrayList<>();

    private ProgressDialog progressDialog;
    private PostServiceMainTabFragment fragment;

    public enum PostEditStatus {
        POST,
        EDIT,
        DUPLICATE
    }

    public void setEditStatus(PostEditStatus editStatus) {
        this.editStatus = editStatus;
    }

    public PostServiceSummaryFragment() {
        // Required empty public constructor
    }

    public static PostServiceSummaryFragment newInstance(String param1, String param2) {
        PostServiceSummaryFragment fragment = new PostServiceSummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        String postTime = appointmentNewDate(new Date());

        JGGAppointmentModel appointmentModel = JGGAppManager.getInstance().getSelectedAppointment();
        appointmentModel.setPostOn(postTime);
        JGGAppManager.getInstance().setSelectedAppointment(appointmentModel);

        mAlbumFiles = appointmentModel.getAlbumFiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_service_summary, container, false);

        initView(view);
        setDatas();

        return view;
    }

    private void initView(View view) {
        imgCategory = view.findViewById(R.id.img_category);
        lblCategory = view.findViewById(R.id.lbl_category_name);
        btnDescribe = view.findViewById(R.id.btn_post_main_describe);
        btnPrice = view.findViewById(R.id.btn_post_main_price);
        btnTimeSlot = view.findViewById(R.id.btn_post_main_time_slot);
        btnAddress = view.findViewById(R.id.btn_post_main_address);
        describeTagView = view.findViewById(R.id.post_service_main_tag_view);
        lblDescribeTitle = view.findViewById(R.id.lbl_post_describe_title);
        lblDescribeDesc = view.findViewById(R.id.lbl_post_describe_description);
        lblPrice = view.findViewById(R.id.lbl_post_main_price);
        lblTimeSlot = view.findViewById(R.id.lbl_post_main_time_slot);
        lblAddress = view.findViewById(R.id.lbl_post_main_address);
        btnPostService = view.findViewById(R.id.btn_post_service);
        lblPostService = view.findViewById(R.id.lbl_post_service);

        if (editStatus == EDIT) lblPostService.setText("Save Changes");
        btnDescribe.setOnClickListener(this);
        btnPrice.setOnClickListener(this);
        btnTimeSlot.setOnClickListener(this);
        btnAddress.setOnClickListener(this);
        btnPostService.setOnClickListener(this);
    }

    private void setDatas() {
        JGGAppointmentModel appointmentModel = JGGAppManager.getInstance().getSelectedAppointment();

        if (appointmentModel != null) {
            // Category
            Picasso.with(mContext)
                    .load(appointmentModel.getCategory().getImage())
                    .placeholder(null)
                    .into(imgCategory);
            lblCategory.setText(appointmentModel.getCategory().getName());
            // Describe
            lblDescribeTitle.setText(appointmentModel.getTitle());
            lblDescribeDesc.setText(appointmentModel.getDescription());
            // Tag
            String tags = appointmentModel.getTags();
            if (tags != null && tags.length() > 0) {
                String [] strings = tags.split(",");
                describeTagView.setTags(Arrays.asList(strings));
            }
            // Price
            String price = "";
            if (appointmentModel.getAppointmentType() == 1) {
                if (appointmentModel.getBudgetType() == fixed)
                    price = "Fixed $ " + appointmentModel.getBudget().toString();
                else if (appointmentModel.getBudgetType() == from)
                    price = "From $ " + appointmentModel.getBudgetFrom().toString()
                            + " "
                            + "to $ " + appointmentModel.getBudgetTo().toString();
                else
                    price = "No set";
            } else if (appointmentModel.getAppointmentType() >= 2) {
                price = String.valueOf(appointmentModel.getAppointmentType()) + " Services, ";
                if (appointmentModel.getBudget() != null)
                    price = price + "$ " + String.valueOf(appointmentModel.getBudget()) + " per service";
                else
                    price = "$ " + String.valueOf(appointmentModel.getBudget());
            }
            lblPrice.setText(price);
            // Address
            lblAddress.setText(appointmentModel.getAddress().getFullAddress());
        } else {
            lblDescribeTitle.setText("No title");
            lblDescribeDesc.setText("");
            describeTagView.removeAllTags();
            lblPrice.setText("No set");
            lblTimeSlot.setText("View Time Slots");
            lblAddress.setText("No set");
        }
    }

    private void onPostButtonClicked() {
        if (attachmentURLs.size() == 0) {
            uploadImage(0);
        } else {
            onPostService();
        }
    }

    private void uploadImage(final int index) {
        if (mAlbumFiles == null) {
            if (editStatus == POST)
                onPostService();
            else if (editStatus == EDIT)
                onEditService();
        } else {
            if (index < mAlbumFiles.size()) {
                String name = (String) mAlbumFiles.get(index).getPath();
                Uri imageUri = Uri.parse(new File(name).toString());
                File file = new File(String.valueOf(imageUri));

                // Parsing any Media type file
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                progressDialog = Global.createProgressDialog(mContext);

                JGGAPIManager manager = JGGURLManager.createService(JGGAPIManager.class, mContext);
                Call<JGGPostAppResponse> call = manager.uploadAttachmentFile(fileToUpload);
                call.enqueue(new Callback<JGGPostAppResponse>() {
                    @Override
                    public void onResponse(Call<JGGPostAppResponse> call, Response<JGGPostAppResponse> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess()) {
                                String url = response.body().getValue();
                                attachmentURLs.add(url);
                                uploadImage(index + 1);
                            } else {
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int statusCode = response.code();
                            Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JGGPostAppResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Request time out!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (editStatus == POST)
                    onPostService();
                else if (editStatus == EDIT)
                    onEditService();
            }
        }
    }

    private void onPostService() {
        progressDialog = Global.createProgressDialog(mContext);
        JGGAppointmentModel appointmentModel = JGGAppManager.getInstance().getSelectedAppointment();
        appointmentModel.setAttachmentURLs(attachmentURLs);
        JGGAppManager.getInstance().setSelectedAppointment(appointmentModel);

        JGGAPIManager manager = JGGURLManager.createService(JGGAPIManager.class, mContext);
        Call<JGGPostAppResponse> call = manager.postNewService(appointmentModel);

        Gson gson = new Gson();
        String str = gson.toJson(appointmentModel);

        call.enqueue(new Callback<JGGPostAppResponse>() {
            @Override
            public void onResponse(Call<JGGPostAppResponse> call, Response<JGGPostAppResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        postedServiceID = response.body().getValue();

                        JGGAppointmentModel model = JGGAppManager.getInstance().getSelectedAppointment();
                        model.setID(postedServiceID);
                        JGGAppManager.getInstance().setSelectedAppointment(model);

                        showAlertDialog();
                    } else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JGGPostAppResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Request time out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onEditButtonClicked() {
        if (attachmentURLs.size() == 0) {
            uploadImage(0);
        } else {
            onEditService();
        }
    }

    private void onEditService() {
        progressDialog = Global.createProgressDialog(mContext);
        JGGAPIManager manager = JGGURLManager.createService(JGGAPIManager.class, mContext);
        Call<JGGPostAppResponse> call = manager.editService(JGGAppManager.getInstance().getSelectedAppointment());
        call.enqueue(new Callback<JGGPostAppResponse>() {
            @Override
            public void onResponse(Call<JGGPostAppResponse> call, Response<JGGPostAppResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        postedServiceID = response.body().getValue();

                        JGGAppointmentModel model = JGGAppManager.getInstance().getSelectedAppointment();
                        model.setID(postedServiceID);
                        JGGAppManager.getInstance().setSelectedAppointment(model);

                        showAlertDialog();
                    } else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int statusCode  = response.code();
                    Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JGGPostAppResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Request time out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_post_service) {
            switch (editStatus) {
                case POST:
                    onPostButtonClicked();
                    //showAlertDialog();
                    break;
                case EDIT:
                    onEditButtonClicked();
//                    /showAlertDialog();
                    break;
                case DUPLICATE:
                    onPostButtonClicked();
                    //showAlertDialog();

                    /*Intent intent = new Intent(mContext, PostServiceActivity.class);
                    intent.putExtra(EDIT_STATUS, POST);
                    intent.putExtra(APPOINTMENT_TYPE, SERVICES);
                    startActivity(intent);*/
                    break;
                default:
                    break;
            }
            return;
        } else if (view.getId() == R.id.btn_post_main_describe) {
            fragment = PostServiceMainTabFragment.newInstance(PostServiceTabView.PostServiceTabName.DESCRIBE);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.post_service_container, fragment)
                    .addToBackStack("post_service")
                    .commit();
        } else if (view.getId() == R.id.btn_post_main_price) {
            fragment = PostServiceMainTabFragment.newInstance(PostServiceTabView.PostServiceTabName.TIME);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.post_service_container, fragment)
                    .addToBackStack("post_service")
                    .commit();
        } else if (view.getId() == R.id.btn_post_main_time_slot) {
            fragment = PostServiceMainTabFragment.newInstance(PostServiceTabView.PostServiceTabName.ADDRESS);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.post_service_container, fragment)
                    .addToBackStack("post_service")
                    .commit();
        } else if (view.getId() == R.id.btn_post_main_address) {
            fragment = PostServiceMainTabFragment.newInstance(PostServiceTabView.PostServiceTabName.REPORT);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.post_service_container, fragment)
                    .addToBackStack("post_service")
                    .commit();
        }
        fragment.setEditStatus(editStatus);
    }

    private void showAlertDialog() {
        String message = "Service reference no: "
                + '\n'
                + postedServiceID
                + '\n'
                + '\n'
                + "Our team will verify your submission and get back to you soon! ";
        JGGAlertView builder = new JGGAlertView(mContext,
                mContext.getResources().getString(R.string.alert_service_posted_title),
                message,
                false,
                "",
                R.color.JGGGreen,
                R.color.JGGGreen10Percent,
                mContext.getResources().getString(R.string.alert_view_service_button),
                R.color.JGGGreen);
        alertDialog = builder.create();
        builder.setOnItemClickListener(new JGGAlertView.OnItemClickListener() {
            @Override
            public void onDoneButtonClick(View view) {
                if (view.getId() == R.id.btn_alert_cancel)
                    alertDialog.dismiss();
                else {
                    alertDialog.dismiss();
                    Intent intent = new Intent(mContext, PostedServiceActivity.class);
                    intent.putExtra("is_post", true);
                    mContext.startActivity(intent);
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
