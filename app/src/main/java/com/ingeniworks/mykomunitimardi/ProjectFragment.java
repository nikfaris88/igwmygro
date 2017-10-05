package com.ingeniworks.mykomunitimardi;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingeniworks.mykomunitimardi.adapter.SectionedProjectAdapter;
import com.ingeniworks.mykomunitimardi.model.Enrollment;
import com.ingeniworks.mykomunitimardi.model.Project;
import com.ingeniworks.mykomunitimardi.model.Section;
import com.ingeniworks.mykomunitimardi.model.User;
import com.ingeniworks.mykomunitimardi.utils.NetworkCheck;
import com.ingeniworks.mykomunitimardi.utils.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static com.ingeniworks.mykomunitimardi.MainActivity.mRoleId;
import static com.ingeniworks.mykomunitimardi.MainActivity.mToken;
import static com.ingeniworks.mykomunitimardi.SelectProject.isChat;

/**
 * Created by nikfaris on 20/09/2017.
 * List of projects related to that user.
 */

public class ProjectFragment extends Fragment {

    private View rootView;
    private ArrayList<Section> listSections;
    private SectionedProjectAdapter mAdapter;
    private NetworkCheck mNetworkCheck;
    private Webservice mWebService;
    private String[] users;
    private TextView txtNoSignal;
    private ImageView imgNoData;
    private RecyclerView mRecyclerView;
    private RelativeLayout rlNoAnnounceChild;

    public ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rv_main_sectioned, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        getProjectDataSet();
        if (listSections.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            rlNoAnnounceChild.setVisibility(View.GONE);
            mAdapter.setProjectBySection(listSections);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
            mRecyclerView.addItemDecoration(itemDecoration);
            mAdapter.setOnItemClickListener(new SectionedProjectAdapter.MyClickListener() {
                @Override
                public void onItemClick(int sectionIndex, int itemIndex, View v) {
                    Project project = mAdapter.getItem(sectionIndex, itemIndex);
                    Intent intent = new Intent(getContext(), ProjectDetails.class);
                    System.out.println("projectID : " + project.getProject_id());
                    intent.putExtra("project_id", project.getProject_id());
                    intent.putExtra("enrollment", users);
                    startActivity(intent);
                }
            });
        } else {
            mRecyclerView.setVisibility(View.GONE);
            rlNoAnnounceChild.setVisibility(View.VISIBLE);
            imgNoData.setImageResource(R.drawable.error);
            txtNoSignal.setText(getString(R.string.strNoData));
        }


        return rootView;
    }


    private void init() {
        mNetworkCheck = new NetworkCheck(getActivity());
        mWebService = new Webservice(getActivity());

        txtNoSignal = (TextView) rootView.findViewById(R.id.txtNoSignal);
        imgNoData = (ImageView) rootView.findViewById(R.id.imgNoData);
        rlNoAnnounceChild = (RelativeLayout) rootView.findViewById(R.id.rlNoAnnounceChild);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        StickyHeaderLayoutManager mLayoutManager = new StickyHeaderLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SectionedProjectAdapter(getActivity());
        isChat = mRoleId == 1;

    }

    @Override
    public void onResume() {

        super.onResume();
    }


    private ArrayList<Section> getProjectDataSet() {

        try {

            if (mNetworkCheck.isNetworkConnected()) {
                String result = getProjectData();
                System.out.println("result list project: " + result);
                if (result != null) {
                    try {
                        JSONObject objResult = new JSONObject(result);
                        if (objResult.getInt("status") > 0) {
                            //TODO read project data
                            JSONArray arrData = objResult.getJSONArray("data");
                            JSONObject objData;

                            JSONArray arrProjects;
                            JSONObject objProject;

                            int projectId, categoryId;
                            String projectTitle, categoryName;
                            String agency = "";
                            Project project;
                            Section section;

                            JSONArray arrEnrolls;
                            JSONObject objEnroll;
                            JSONObject objUser;

                            Enrollment enroll;
                            User user;

                            listSections = new ArrayList<>();
                            if (arrData.length() > 0) {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                rlNoAnnounceChild.setVisibility(View.GONE);
                                for (int i = 0; i < arrData.length(); i++) {
                                    //TODO add section
                                    objData = arrData.getJSONObject(i);
                                    section = new Section();
                                    section.setSection_id(objData.getInt("id"));
                                    section.setSection_title(objData.getString("name"));
                                    arrProjects = objData.getJSONArray("projects");
                                    ArrayList<Project> listProjects = new ArrayList<>();

                                    for (int x = 0; x < arrProjects.length(); x++) {
                                        project = new Project();

                                        objProject = arrProjects.getJSONObject(x);
                                        projectId = objProject.getInt("id");
                                        categoryId = objProject.getInt("project_category_id");
                                        projectTitle = objProject.getString("name");

                                        JSONObject objProjectCategory = objProject.getJSONObject("project_category");
                                        categoryName = objProjectCategory.getString("name");

                                        project.setProject_id(projectId);
                                        project.setProject_title(projectTitle);
                                        project.setProject_categoryId(categoryId);
                                        project.setProject_categoryName(categoryName);

                                        arrEnrolls = objProject.getJSONArray("enrolls");

                                        ArrayList<Enrollment> listEnrollments = new ArrayList<>();
                                        users = new String[arrEnrolls.length()];
                                        for (int e = 0; e < arrEnrolls.length(); e++) {
                                            enroll = new Enrollment();
                                            user = new User();
                                            objEnroll = arrEnrolls.getJSONObject(e);
                                            objUser = objEnroll.getJSONObject("user");

                                            user.setName(objUser.getString("name"));
                                            enroll.setUser(user);
                                            users[e] = objUser.getString("name");
                                            listEnrollments.add(enroll);
                                        }

                                        project.setEnrollmentArrayList(listEnrollments);
                                        project.setAgency(agency);
                                        project.setSection_id(objData.getInt("id"));
                                        listProjects.add(project);

                                    }
                                    section.setProjects(listProjects);
                                    listSections.add(section);
                                }
                            } else {
                                mRecyclerView.setVisibility(View.GONE);
                                rlNoAnnounceChild.setVisibility(View.VISIBLE);
                                imgNoData.setImageResource(R.drawable.error);
                                txtNoSignal.setText(getString(R.string.strNoData));
                            }
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                            rlNoAnnounceChild.setVisibility(View.VISIBLE);
                            imgNoData.setImageResource(R.drawable.error);
                            txtNoSignal.setText(getString(R.string.strNoData));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    rlNoAnnounceChild.setVisibility(View.VISIBLE);
                    imgNoData.setImageResource(R.drawable.error);
                    txtNoSignal.setText(getString(R.string.strErrTechnicalIssue));
                }
            } else {
                mRecyclerView.setVisibility(View.GONE);
                rlNoAnnounceChild.setVisibility(View.VISIBLE);
                imgNoData.setImageResource(R.drawable.no_signal_internet);
                txtNoSignal.setText(getString(R.string.strErrConnection));
            }
        } catch (SocketException | SocketTimeoutException | NetworkErrorException e) {
            e.printStackTrace();
        }
        return listSections;
    }

    // get feed data
    //TODO Asynctask getProjectDataa
    private String getProjectData() {

        String projectData = "";
        try {
            projectData = mWebService.getProjectData(mToken, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return projectData;

    }

}
