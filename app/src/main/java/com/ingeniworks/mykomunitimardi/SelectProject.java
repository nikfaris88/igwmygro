package com.ingeniworks.mykomunitimardi;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

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

import static com.ingeniworks.mykomunitimardi.MainActivity.mToken;

public class SelectProject extends AppCompatActivity {
    private ArrayList<Section> listSections;
    private NetworkCheck mNetworkCheck;
    private Webservice mWebService;
    public static boolean isChat = false;
    public static int projectId = 0;
    public static String projectTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_project_sectioned);
        init();

    }

    private void init() {
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.sessionString), Context.MODE_PRIVATE);
        mToken = mSharedPreferences.getString("token", "");
        mNetworkCheck = new NetworkCheck(this);
        mWebService = new Webservice(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.strListProjects));
        }

        isChat = false;

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        StickyHeaderLayoutManager mLayoutManager = new StickyHeaderLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        final SectionedProjectAdapter mAdapter = new SectionedProjectAdapter(SelectProject.this);
        getProjectDataSet();
        mAdapter.setProjectBySection(listSections);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mAdapter.setOnItemClickListener(new SectionedProjectAdapter.MyClickListener() {
            @Override
            public void onItemClick(int sectionIndex, int itemIndex, View v) {
                Project project = mAdapter.getItem(sectionIndex, itemIndex);
                projectId = project.getProject_id();
                projectTitle = project.getProject_title();
                finish();
            }
        });
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

                            int projectId = 0, categoryId;
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
                                    for (int e = 0; e < arrEnrolls.length(); e++) {
                                        enroll = new Enrollment();
                                        user = new User();
                                        objEnroll = arrEnrolls.getJSONObject(e);
                                        objUser = objEnroll.getJSONObject("user");

                                        user.setName(objUser.getString("name"));
                                        enroll.setUser(user);
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

//                            for (int s = 0; s < listSections.size(); s++) {
//                                System.out.println("section: " + listSections.get(s).getSection_title());
//                                for (int p = 0; p < listSections.get(s).getProjects().size(); p++) {
//                                    System.out.println("project: " + listSections.get(s).getProjects().get(p).getProject_title());
//                                }
//                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
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
            System.out.println("mToken: " + mToken);
            projectData = mWebService.getProjectData(mToken, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return projectData;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

}
