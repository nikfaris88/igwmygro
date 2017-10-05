package com.ingeniworks.mykomunitimardi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;

import com.ingeniworks.mykomunitimardi.adapter.ListAppAdapter;
import com.ingeniworks.mykomunitimardi.model.Apps;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

public class ListApplicationFragment extends Fragment {
    //    private static String LOG_TAG = "ListApplicationActivity";
    private GridView gvListApps;
    private View rootView;
    public static int gvListHeight;
    private ListAppAdapter mAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private Settings settings;


    public ListApplicationFragment() {

    }

    public ListApplicationFragment newInstance() {
        return new ListApplicationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.list_app_main_fragment, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
        getDataSet();

        return rootView;
    }

    private void init() {
//        gvListApps = (GridView) rootView.findViewById(R.id.gvListApps);
//        gvListHeight = gvListApps.getHeight();
//        gvListApps.layout

//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) rootView.getLayoutParams();
//        params.addRule(RelativeLayout.BELOW, R.id.appbar);
//        params.

//        .addRule(RelativeLayout.BELOW, R.id.appbar);
//
//        gvListApps.setLayoutParams(p);

        UltimateRecyclerView mRecyclerView = (UltimateRecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        mAdapter = new ListAppAdapter(getActivity(), getDataSet());
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.setLoadMoreView(LayoutInflater.from(this.getActivity())
                .inflate(R.layout.custom_bottom_progressbar, null));


        mRecyclerView.reenableLoadmore();

        mRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
//                        simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                        simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                        simpleRecyclerViewAdapter.insert("More " + moreNum++, simpleRecyclerViewAdapter.getAdapterItemCount());
//                        recyclerViewLayoutManager.scrollToPositionWithOffset(maxLastVisiblePosition,-1);
                        recyclerViewLayoutManager.scrollToPosition(maxLastVisiblePosition);

                    }
                }, 1000);
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new ListAppAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (mAdapter != null) {
//                    Apps apps = mAdapter.getItem(position);
//                    Intent intent = new Intent(getActivity(), FeedDetails.class);
//                    if (apps.getFeed_type() == 0) {
//                        intent.putExtra("msg_type", "Umum");
//                    } else {
//                        intent.putExtra("msg_type", "Private");
//                    }
//                    intent.putExtra("title", feed.getFeed_title());
//                    intent.putExtra("content", feed.getFeed_message());
//                    intent.putExtra("dateTime", "25 Julai 2017, 5:00 PM");
//                    intent.putExtra("createdBy", "MARDI");
//                    if (feed.getAttachments().size() > 0) {
//                        intent.putExtra("attachment", feed.getAttachments().get(position).getDrawables());
//                    }
//
//                    startActivity(intent);

//                System.out.println("position: " + position);

                    Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(mAdapter.getItem(position).getApp_package_name());
                    final String appPackageName = mAdapter.getItem(position).getApp_package_name(); // getPackageName() from Context or Activity object
                    if (i != null) {
                        getActivity().startActivity(i);
                    } else {
                        try {
                            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settings.getGoogleplayStorePath() + appPackageName)));
                        }
                    }

                    WebView webView = new WebView(v.getContext());
                    webView.loadUrl(mAdapter.getItem(position).getGoogle_play_link());
                }
            }
        });
    }

    private ArrayList<Apps> getDataSet() {

        ArrayList<Apps> appsArrayList = new ArrayList<>();
        Apps apps;
//        try {
//            if (networkCheck.isNetworkConnected()) {
//                String results = webservice.getAgencies();
            int i = 0;
            apps = new Apps();
            apps.setId(i);
            apps.setApp_name("Fertigasi Tomato");
            apps.setIcon(R.drawable.fertigasitomato);
            apps.setApp_package_name("my.gov.mardi.apptomato");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Buah Nadir");
            apps.setIcon(R.drawable.buahnadir);
            apps.setApp_package_name("my.gov.mardi.fruitsdirectory");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Padi Aerob");
            apps.setIcon(R.drawable.padiaerob);
            apps.setApp_package_name("my.gov.mardi.apppadiaerob");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Dr Cili");
            apps.setIcon(R.drawable.drcili);
            apps.setApp_package_name("my.gov.mardi.drcili");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("MyJagung");
            apps.setIcon(R.drawable.myjagung);
            apps.setApp_package_name("my.gov.mardi.myjagung");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Kelulut");
            apps.setIcon(R.drawable.kelulut);
            apps.setApp_package_name("my.gov.mardi.kelulut");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Tanaman Cendawan");
            apps.setIcon(R.drawable.cendawankelabu);
            apps.setApp_package_name("my.gov.mardi.teknologicendawan");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Fertigasi Cili");
            apps.setIcon(R.drawable.fertigasicili);
            apps.setApp_package_name("my.gov.mardi.appcili");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("MyKompos");
            apps.setIcon(R.drawable.mykompos);
            apps.setApp_package_name("my.gov.mardi.mykompos");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("MyPerosak Padi");
            apps.setIcon(R.drawable.myperosakpadi);
            apps.setApp_package_name("my.gov.mardi.perosakpadi");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("GreenPharmacy");
            apps.setIcon(R.drawable.greenpharmacy);
            apps.setApp_package_name("my.gov.mardi.greenpharmacy");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Ayam Kampung");
            apps.setIcon(R.drawable.ayamkampung);
            apps.setApp_package_name("my.gov.mardi.ayamkampung");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Lembu Brakmas");
            apps.setIcon(R.drawable.lembubrakmas);
            apps.setApp_package_name("my.gov.mardi.lembubrakmas");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Fertigasi Salad");
            apps.setIcon(R.drawable.fertigasisalad);
            apps.setApp_package_name("my.gov.mardi.appsalad");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Kambing Pedaging");
            apps.setIcon(R.drawable.kambingpedaging);
            apps.setApp_package_name("my.gov.mardi.kambingpedaging");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("OPF");
            apps.setIcon(R.drawable.jagungkalendar);
            apps.setApp_package_name("my.gov.mardi.opf");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Direktori Pertanian");
            apps.setIcon(R.drawable.direktoripertanian);
            apps.setApp_package_name("com.ionicframework.myagrodirv3696450");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Video");
            apps.setIcon(R.drawable.video);
            apps.setApp_package_name("com.ionicframework.myagrovideo115067");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Maklumat Penyakit dan Perosak");
            apps.setIcon(R.drawable.penyakitperosak);
            apps.setApp_package_name("com.ionicframework.pdiproduction458484");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Cuaca");
            apps.setIcon(R.drawable.cuaca);
            apps.setApp_package_name("com.ionicframework.myagrocuaca102586");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Latihan Kalendar");
            apps.setIcon(R.drawable.kalendarlatihan);
            apps.setApp_package_name("com.ionicframework.mytc490103");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Rekod Ladang");
            apps.setIcon(R.drawable.rekodladang);
            apps.setApp_package_name("com.ionicframework.farmrecord2812028");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Kalendar Komoditi");
            apps.setIcon(R.drawable.kalendarkomoditi);
            apps.setApp_package_name("com.ionicframework.commodityinformation238513");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Maklumat Pasaran");
            apps.setIcon(R.drawable.maklumatpasaran);
            apps.setApp_package_name("com.ionicframework.mi897877");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Maklumat Komoditi");
            apps.setIcon(R.drawable.maklumatkomoditi);
            apps.setApp_package_name("com.ionicframework.ciproduction564140");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Kalkulator");
            apps.setIcon(R.drawable.kalkulator);
            apps.setApp_package_name("com.ionicframework.cfcproduction947371");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("Info Banjir");
            apps.setIcon(R.drawable.infobanjir);
            apps.setApp_package_name("com.jkmt.ppbanjir");
            appsArrayList.add(apps);

            apps = new Apps();
            apps.setId(i++);
            apps.setApp_name("MyHealth");
            apps.setIcon(R.drawable.myhealth);
            apps.setApp_package_name("com.miamir.myapp.kkm");
            appsArrayList.add(apps);

//            gvListApps.setAdapter(new AppsGVAdapter(getActivity(), appsArrayList));
//
////            } else {
////                Toast.makeText(this, "No network connection. Please try again.", Toast.LENGTH_SHORT).show();
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return appsArrayList;

    }
}
